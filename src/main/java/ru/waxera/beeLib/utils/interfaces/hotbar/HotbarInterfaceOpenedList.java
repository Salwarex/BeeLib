package ru.waxera.beeLib.utils.interfaces.hotbar;

import org.bukkit.entity.Player;

import java.util.HashMap;

@Deprecated
public class HotbarInterfaceOpenedList {
    private static HashMap<Player, HotbarInterface> list = new HashMap<>();

    public static void put(Player player, HotbarInterface hotbarInterface){
        if(list.containsKey(player)) list.replace(player, hotbarInterface);
        else list.put(player, hotbarInterface);
    }
    public static HotbarInterface get(Player player){
        return list.getOrDefault(player, null);
    }
    public static void remove(Player player){
        list.remove(player);
    }
    public static boolean contains(Player player){
        return list.containsKey(player);
    }
}
