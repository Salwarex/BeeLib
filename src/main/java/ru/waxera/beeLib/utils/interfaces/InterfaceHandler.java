package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InterfaceHandler implements Listener {

//    public void itemClick(InventoryClickEvent e){ //ITL
//        if(e.getView().getTitle() == null || e.getCurrentItem() == null) return;
//
//        String title = e.getView().getTitle();
//        ContainerInterface containerInterface = InterfaceTemplatesList.get(title);
//        if(containerInterface == null) return;
//        if(!containerInterface.isDefaultInterfaceHandler()) return;
//        containerInterface.playerClickAction(e); //Maybe should be parsed to SPI or MPI
//    }
    @EventHandler
    public void itemClick(InventoryClickEvent e){ //IOL
        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();
        ContainerInterface containerInterface = InterfaceOpenedList.get(player);
        if(containerInterface == null) return;
        if(!containerInterface.isDefaultInterfaceHandler()) return;
        containerInterface.playerClickAction(e);
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e){ //IOL
        Player player = (Player) e.getPlayer();
        if(InterfaceOpenedList.contains(player)) InterfaceOpenedList.remove(player);
    }
}