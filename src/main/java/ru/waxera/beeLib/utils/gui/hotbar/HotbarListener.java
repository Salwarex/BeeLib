package ru.waxera.beeLib.utils.gui.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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
        e.setCancelled(true);
        HotbarInterface hotbarInterface = HotbarInterfaceOpenedList.get(player);
        int i = getSlot(e.getItem(), player);
        if(i < 0 || i > 8) return;
        hotbarInterface.execute(i, e);
    }

    @EventHandler
    public void onBlock(BlockPlaceEvent e){
        Player player = e.getPlayer();
        if(!HotbarInterfaceOpenedList.contains(player)) return;
        e.setCancelled(true);
        HotbarInterface hotbarInterface = HotbarInterfaceOpenedList.get(player);
        int i = getSlot(e.getItemInHand(), player);
        if(i < 0 || i > 8) return;
        hotbarInterface.execute(i, e);
    }

    private int getSlot(ItemStack item, Player player){
        PlayerInventory playerInventory = player.getInventory();
        for(int i = 0; i < 9; i++){
            if(item == null) return -1;
            if(playerInventory.getItem(i) != null){
                if(item.getType() == playerInventory.getItem(i).getType()) return i;
            }
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
