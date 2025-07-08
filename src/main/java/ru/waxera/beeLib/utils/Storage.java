package ru.waxera.beeLib.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.waxera.beeLib.BeeLib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Storage {
    private File file;
    private FileConfiguration config;

    public Storage(String name, String dir){
        if(dir != null && !dir.isEmpty()){ //subfolder files
            File folder = new File(BeeLib.getPlugin().getDataFolder(), dir);
            if(!folder.exists()){
                folder.mkdirs();
            }
            file = new File(folder, name);
        }
        else{
            file = new File(BeeLib.getPlugin().getDataFolder(), name);
        }

        try{
            if(!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e){
            throw new RuntimeException("Failed to create file", e);
        }
        config = YamlConfiguration.loadConfiguration(file);
        checkUpdates(name, dir);
    }

    public FileConfiguration getConfig(){
        return config;
    }
    public void save(){
        try {
            config.save(file);
        } catch (IOException e){throw new RuntimeException("Failed to save!", e);
        }
    }

    public static void checkUpdates(String fileName, String subFolder) {
        File configFile = (subFolder != null && !subFolder.isEmpty())
                ? new File(BeeLib.getPlugin().getDataFolder(), subFolder + File.separator + fileName)
                : new File(BeeLib.getPlugin().getDataFolder(), fileName);

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String resourcePath = (subFolder != null && !subFolder.isEmpty())
                ? subFolder + "/" + fileName
                : fileName;

        InputStream resourceStream = BeeLib.getPlugin().getResource(resourcePath);

        if (resourceStream == null) {
            BeeLib.getPlugin().getLogger().severe("Resource not found: " + resourcePath);
            return;
        }

        FileConfiguration resourceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(resourceStream));

        int currentVersion = config.getInt("version");
        int resourceVersion = resourceConfig.getInt("version");

        if (currentVersion < resourceVersion) {
            for (String key : resourceConfig.getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, resourceConfig.get(key));
                }
            }
            config.set("version", resourceConfig.getInt("version"));
            try {
                config.save(configFile);
                BeeLib.getPlugin().getLogger().info("Updated " + fileName + " to version " + resourceVersion);
            } catch (IOException e) {
                BeeLib.getPlugin().getLogger().severe("Failed to save updated config: " + e.getMessage());
            }
        }
    }
}

