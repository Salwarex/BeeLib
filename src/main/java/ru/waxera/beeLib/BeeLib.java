package ru.waxera.beeLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.waxera.beeLib.utils.command.BeeLibCommand;
import ru.waxera.beeLib.utils.specials.language.Language;
import ru.waxera.beeLib.utils.specials.language.LanguageManager;
import ru.waxera.beeLib.utils.data.pools.file.FileStorage;
import ru.waxera.beeLib.utils.data.BeeLibDataHandler;
import ru.waxera.beeLib.utils.gui.container.ContainerInterfaceHandler;
import ru.waxera.beeLib.utils.gui.hotbar.HotbarListener;
import ru.waxera.beeLib.utils.gui.hotbar.RestoreHub;
import ru.waxera.beeLib.utils.gui.questionnaire.QuestionnaireListener;
import ru.waxera.beeLib.utils.player.PlayerDataListener;
import ru.waxera.beeLib.utils.preferences.beeLibPrefs.BeeLibPreferences;
import ru.waxera.beeLib.utils.preferences.beeLibPrefs.BeeLibPreferencesKeys;

import java.util.HashMap;

public final class BeeLib extends JavaPlugin{
    private static HashMap<String, Boolean> softDeps = new HashMap<>();
    private static BeeLib instance;
    private static FileStorage holding;
    private static BeeLibDataHandler dataHandler;
    private static BeeLibPreferences preferences;

    @Override
    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        new LanguageManager(instance, new Language[]{Language.ENGLISH, Language.RUSSIAN});

        preferences = new BeeLibPreferences(this.getConfig());
        holding = new FileStorage("holding.yml", "hotbar-interface", BeeLib.getInstance());
        dataHandler = new BeeLibDataHandler();
        if((Boolean) preferences.get(BeeLibPreferencesKeys.ALLOW_PLAYER_DATA_KEEPING)){
            dataHandler.initPlayerPool();
        }
        checkDependecies();
        new RestoreHub();

        this.registerInteraction();
    }

    private void registerInteraction(){
        Bukkit.getPluginManager().registerEvents(new ContainerInterfaceHandler(), this);
        Bukkit.getPluginManager().registerEvents(new QuestionnaireListener(), this);
        Bukkit.getPluginManager().registerEvents(new HotbarListener(), this);
        if((Boolean) preferences.get(BeeLibPreferencesKeys.ALLOW_PLAYER_DATA_KEEPING)){
            Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), this);
        }
        new BeeLibCommand();
    }

    public static void setPlugin(final JavaPlugin plugin, Language[] languages){
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
    public static BeeLibDataHandler getDataHandler(){
        return dataHandler;
    }
}
