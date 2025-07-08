package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Slot {
    private ItemStack itemStack;
    private Action action;

    public Slot(ItemStack is, Action ac){
        this.itemStack = is;
        this.action = ac;
    }

    public void execute(Player player){
        if (action == null) return;
        action.run(player);
    }

    public ItemStack getItemStack(){
        return this.itemStack;
    }

    public Action getAction(){
        return this.action;
    }
}
