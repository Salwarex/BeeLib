package ru.waxera.beeLib.utils.gui.container;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ContainerInterfaceHandler implements Listener {

    @EventHandler
    public void itemClick(InventoryClickEvent e){
        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();
        ContainerInterface containerInterface = InterfaceOpenedPool.getInstance().get(player);
        if(containerInterface == null) return;
        if(!containerInterface.isDefaultInterfaceHandler()) return;
        containerInterface.playerClickAction(e);
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        if(InterfaceOpenedPool.getInstance().contains(player)) InterfaceOpenedPool.getInstance().remove(player);
    }
}