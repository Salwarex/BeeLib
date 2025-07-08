package ru.waxera.beeLib.utils;

import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.utils.message.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class LanguageManager {
    private static HashMap<Plugin, LanguageManager> plugin_list;
    private final Plugin plugin;
    private ArrayList<Language> languages = new ArrayList<>();
    private HashMap<Language, Storage> lang = new HashMap<>();

    public LanguageManager(Plugin plugin, Language[] languages){
        if(plugin_list.containsKey(plugin)){
            this.plugin = null;
            Message.error(null, "Currently, the LanguageManager for the " + plugin.getName() + " plugin already exists.");
            return;
        }
        this.plugin = plugin;
        plugin_list.put(this.plugin, this);

        for(Language language : languages){
            lang.put(language, new Storage(language.getFilename() + ".yml", "languages", plugin));
        }
    }

    public Storage getLanguage(Language language){
        return lang.get(language);
    }
    public Language nowLanguage(){
        String shortname = plugin.getConfig().getString("language");
        if(shortname != null){
            Language language = Language.getLanguage(shortname);
            if (language != null) {return language;}
        }
        return Language.ENGLISH;
    }

    public static LanguageManager getInstance(Plugin plugin){
        return plugin_list.getOrDefault(plugin, null);
    }
}
