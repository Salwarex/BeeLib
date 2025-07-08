package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InterfaceHandler implements Listener {

    @EventHandler
    public void itemClick(InventoryClickEvent e){
        String title = e.getView().getTitle();
        ContainerInterface containerInterface = InterfaceTemplatesList.get(title);
        if(containerInterface == null) return;
        if(!containerInterface.isDefaultInterfaceHandler()) return;
        containerInterface.playerClickAction(e); //Maybe should be parsed to SPI or MPI
    }
}