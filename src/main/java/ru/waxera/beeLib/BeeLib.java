package ru.waxera.beeLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.waxera.beeLib.utils.Language;
import ru.waxera.beeLib.utils.LanguageManager;
import ru.waxera.beeLib.utils.interfaces.InterfaceHandler;

import java.util.HashMap;

public final class BeeLib extends JavaPlugin{
    private static HashMap<String, Boolean> softDeps = new HashMap<>();
    private static BeeLib instance;

    @Override
    public void onEnable(){
        instance = this;
        new LanguageManager(instance, new Language[]{Language.ENGLISH, Language.RUSSIAN});
        checkDependecies();
    }

    public static void setPlugin(final JavaPlugin plugin, Language[] languages){
        Bukkit.getPluginManager().registerEvents(new InterfaceHandler(), plugin);
        new LanguageManager(plugin, languages);
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

    public static BeeLib getInstance(){
        return instance;
    }
}
