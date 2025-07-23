package ru.waxera.beeLib.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@FunctionalInterface
public interface Action {
    void run(Player player, Event e);
}
