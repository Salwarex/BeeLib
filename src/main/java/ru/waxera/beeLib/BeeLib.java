package ru.waxera.beeLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.waxera.beeLib.utils.Language;
import ru.waxera.beeLib.utils.LanguageManager;
import ru.waxera.beeLib.utils.data.storages.file.FileStorage;
import ru.waxera.beeLib.utils.data.LocalDataHandler;
import ru.waxera.beeLib.utils.gui.container.ContainerInterfaceHandler;
import ru.waxera.beeLib.utils.gui.hotbar.HotbarListener;
import ru.waxera.beeLib.utils.gui.hotbar.RestoreHub;
import ru.waxera.beeLib.utils.gui.questionnaire.QuestionnaireHandler;
import ru.waxera.beeLib.utils.player.PlayerDataListener;
import ru.waxera.beeLib.utils.player.PlayerDataStorage;
import ru.waxera.beeLib.utils.preferences.beeLibPrefs.BeeLibPreferences;
import ru.waxera.beeLib.utils.preferences.beeLibPrefs.BeeLibPreferencesKeys;

import java.util.HashMap;

public final class BeeLib extends JavaPlugin{
    private static HashMap<String, Boolean> softDeps = new HashMap<>();
    private static BeeLib instance;
    private static FileStorage holding;
    private static LocalDataHandler dataHandler;
    private static PlayerDataStorage playerDataStorage = null;
    private static BeeLibPreferences preferences;

    @Override
    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        preferences = new BeeLibPreferences(this.getConfig());

        holding = new FileStorage("holding.yml", "hotbar-interface", BeeLib.getInstance());
        if((Boolean) preferences.get(BeeLibPreferencesKeys.ALLOW_PLAYER_DATA_KEEPING)){
            playerDataStorage = dataHandler.getPlayerDataStorage();
        }
        dataHandler = new LocalDataHandler();
        new LanguageManager(instance, new Language[]{Language.ENGLISH, Language.RUSSIAN});
        checkDependecies();
        new RestoreHub();

        this.registerEvents();
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new ContainerInterfaceHandler(), this);
        Bukkit.getPluginManager().registerEvents(new QuestionnaireHandler(), this);
        Bukkit.getPluginManager().registerEvents(new HotbarListener(), this);
        if((Boolean) preferences.get(BeeLibPreferencesKeys.ALLOW_PLAYER_DATA_KEEPING)){
            Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), this);
        }
    }

    public static void setPlugin(final JavaPlugin plugin, Language[] languages){
//        Bukkit.getPluginManager().registerEvents(new ContainerInterfaceHandler(), plugin);
//        Bukkit.getPluginManager().registerEvents(new QuestionnaireHandler(), plugin);
//        Bukkit.getPluginManager().registerEvents(new HotbarListener(), plugin);
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
    public static BeeLibPreferences getPreferences(){
        return preferences;
    }
    public static FileStorage getHolding(){ return holding; }
    public static LocalDataHandler getDataHandler(){
        return dataHandler;
    }
    public static PlayerDataStorage getPlayerDataStorage(){ return playerDataStorage; }
}
