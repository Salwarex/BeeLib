package ru.waxera.beeLib.utils.gui.hotbar;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.data.storages.fileStorage.FileStorage;
import ru.waxera.beeLib.utils.message.Message;

public class HoldingItems {
    private final String owner_name;
    private ItemStack[] list = new ItemStack[9];
    private static FileStorage holding = BeeLib.getHolding();

    public HoldingItems(Player player){
        this.owner_name = player.getName();
        PlayerInventory inventory = player.getInventory();
        for(int i = 0; i < 9; i++){
            list[i] = inventory.getItem(i);
        }
        save();
    }

    public HoldingItems(String owner_name, ItemStack[] list){
        this.owner_name = owner_name;
        this.list = list;
        save();
    }

    public HoldingItems(String owner_name){
        this.owner_name = owner_name;
    }

    public ItemStack getItem(int i){
        if(i < 0 || i > 8) {  Message.error(null, "&cHoldingItems contains items with indexes in [0, 8]!"); return null;}
        return list[i];
    }


    public void restore(){
        Player player = Bukkit.getPlayer(this.owner_name);
        if(player == null) { Message.error(null, "&cPlayer " + owner_name + " is offline!"); return;}
        int i = 0;
        PlayerInventory inventory = player.getInventory();
        for(ItemStack item : list){
            inventory.setItem(i, item);
            i++;
        }
        holding.getConfig().set(owner_name, null);
        holding.save();
    }

    private void save(){
        int i = 0;
        for(ItemStack item : list){
            holding.getConfig().set(owner_name + ".inventory._" + i, item);
            i++;
        }
        holding.save();
    }

    public void load(){
        ConfigurationSection section = holding.getConfig().getConfigurationSection(owner_name + ".inventory");
        if(section == null) { Message.error(null, "&cCan't find saves for player " + owner_name); return;}
        for(int i = 0; i < 9; i++){
            list[i] = section.getItemStack("_" + i);
        }
    }
}
