package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class InterfaceOpenedList {
    private static HashMap<Player, ContainerInterface> list = new HashMap<>();

    public static void put(Player player, ContainerInterface containerInterface){
        if(list.containsKey(player)) list.replace(player, containerInterface);
        else list.put(player, containerInterface);
    }
    public static ContainerInterface get(Player player){
        return list.getOrDefault(player, null);
    }
    public static void remove(Player player){
        list.remove(player);
    }
    public static boolean contains(Player player){
        return list.containsKey(player);
    }
}
