package ru.waxera.beeLib.utils.interfaces.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HotbarListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(!HotbarInterfaceOpenedList.contains(player)) return;
        HotbarInterface hotbarInterface = HotbarInterfaceOpenedList.get(player);
        int i = getSlot(e.getItem(), player);
        hotbarInterface.execute(i, e);
    }

    private int getSlot(ItemStack item, Player player){
        PlayerInventory playerInventory = player.getInventory();
        for(int i = 0; i < 9; i++){
            if(item == playerInventory.getItem(i)) return i;
        }
        return -1;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player player = e.getPlayer();
        if(!HotbarInterfaceOpenedList.contains(player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player player = e.getPlayer();
        if(!HotbarInterfaceOpenedList.contains(player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(!HotbarInterfaceOpenedList.contains(player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onRestore(PlayerJoinEvent e){
        Player player = e.getPlayer();
        HoldingItems holdingItems = RestoreHub.get(player.getName());
        if(holdingItems == null) return;
        holdingItems.restore();
    }
}
