package ru.waxera.beeLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.waxera.beeLib.utils.Language;
import ru.waxera.beeLib.utils.Storage;
import ru.waxera.beeLib.utils.database.Database;

import java.util.HashMap;

public final class BeeLib{
    private static JavaPlugin plugin;
    private static HashMap<Language, Storage> lang;
    private static HashMap<String, Boolean> softDeps = new HashMap<>();

    public static JavaPlugin getPlugin(){
        return plugin;
    }

    public static void setPlugin(final JavaPlugin plugin){
        BeeLib.plugin = plugin;
        checkDependecies();
    }

    public void initLanguages(Language[] languages){
        for(Language language : languages){
            lang.put(language, new Storage(language.getFilename() + ".yml", "languages"));
        }
    }
    public static Storage getLanguage(Language language){
        return lang.get(language);
    }
    public static Language nowLanguage(){
        String shortname = plugin.getConfig().getString("language");
        if(shortname != null){
            Language language = Language.getLanguage(shortname);
            if (language != null) {return language;}
        }
        return Language.ENGLISH;
    }

    private static void checkDependecies(){
        Plugin nbtapi = Bukkit.getPluginManager().getPlugin("NBTAPI");
        softDeps.put("nbtapi", nbtapi != null && nbtapi.isEnabled());
    }

    public static boolean checkSoftDeps(String key){
        if(softDeps.containsKey(key)){
            return softDeps.get(key);
        }
        return false;
    }
}
