package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface Action {
    void run(Player player, Object object);
}
