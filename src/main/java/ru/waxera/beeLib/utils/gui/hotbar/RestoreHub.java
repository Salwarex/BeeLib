package ru.waxera.beeLib.utils.gui.hotbar;

import org.bukkit.configuration.ConfigurationSection;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.data.storages.file.FileStorage;
import ru.waxera.beeLib.utils.message.Message;

import java.util.HashMap;
import java.util.Set;

public class RestoreHub {
    private static FileStorage holding = BeeLib.getHolding();
    private static HashMap<String, HoldingItems> list = new HashMap<>();

    public RestoreHub(){
        ConfigurationSection section = holding.getConfig().getConfigurationSection("");
        if(section == null) { Message.send(null, "No recovery records were found."); return;}
        Set<String> keys = section.getKeys(false);
        Message.send(null, "Found " + keys.size() + " recovery records.");
        for(String nick : keys){
            HoldingItems items = new HoldingItems(nick);
            items.load();
            list.put(nick, items);
            Message.send(null, "Loaded " + nick + "'s recovery record. It will be recovered on " + nick + " enter.");
        }
    }

    public static HoldingItems get(String name){
        if(list.containsKey(name)){
            HoldingItems result = list.get(name);
            list.remove(name);
            return result;
        }
        return null;
    }
}
