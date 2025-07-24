package ru.waxera.beeLib.utils.data.storages.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.BeeLib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileStorage {
    private final Plugin plugin;
    private File file;
    private FileConfiguration config;

    public FileStorage(String name, String dir, Plugin plugin){
        this.plugin = plugin == null ? BeeLib.getInstance() : plugin;
        if(dir != null && !dir.isEmpty()){ //subfolder files
            File folder = new File(this.plugin.getDataFolder(), dir);
            if(!folder.exists()){
                folder.mkdirs();
            }
            file = new File(folder, name);
        }
        else{
            file = new File(this.plugin.getDataFolder(), name);
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

    public void checkUpdates(String fileName, String subFolder) {
        File configFile = (subFolder != null && !subFolder.isEmpty())
                ? new File(this.plugin.getDataFolder(), subFolder + File.separator + fileName)
                : new File(this.plugin.getDataFolder(), fileName);

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String resourcePath = (subFolder != null && !subFolder.isEmpty())
                ? subFolder + "/" + fileName
                : fileName;

        InputStream resourceStream = this.plugin.getResource(resourcePath);

        if (resourceStream == null) {
            this.plugin.getLogger().severe("Resource not found: " + resourcePath);
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
                this.plugin.getLogger().info("Updated " + fileName + " to version " + resourceVersion);
            } catch (IOException e) {
                this.plugin.getLogger().severe("Failed to save updated config: " + e.getMessage());
            }
        }
    }
}

