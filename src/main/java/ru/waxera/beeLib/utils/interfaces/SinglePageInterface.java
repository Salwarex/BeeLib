package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.waxera.beeLib.utils.message.Message;

import java.util.HashMap;

public class SinglePageInterface extends ContainerInterface{
    private HashMap<Integer, Slot> items;

    public SinglePageInterface(Player holder, String title, int size, boolean items_moving) {
        super(holder, title, size);
        this.items_moving = items_moving;
    }

    public void setItem(Integer index, ItemStack itemStack, Action action){
        if(items.containsKey(index)) { Message.error("Couldn't add an item to slot " + index + "! There is another item in it."); return; }
        if(this.bg_slots.contains(index)) { Message.error("Couldn't add an item to slot " + index + "! There is background item in it."); return; }
        if(index >= this.inventory.getSize()){ Message.error("The specified item index exceeds the interface size (" + index + " >= " + this.inventory.getSize() + ")"); return;}
        Slot slot = new Slot(itemStack, action);
        items.put(index, slot);
        initSlots();
    }

    public void addItem(ItemStack itemStack, Action action){
        Integer index = -1;
        for(int i = 0; i < this.inventory.getSize(); i++){
            if(this.inventory.getItem(i) == null){
                index = i;
                break;
            }
        }
        if(index == -1){ Message.error("Inventory filling error: There are no available slots in the inventory."); return; }
        this.setItem(index, itemStack, action);
    }

    private void initSlots(){
        for(Integer index : items.keySet()){
            this.inventory.setItem(index, items.get(index).getItemStack());
        }
    }

    @Override
    public void playerClickAction(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        Integer index = e.getSlot();

        if(!this.items_moving || this.bg_slots.contains(index)) e.setCancelled(true);

        if(player == null) { Message.error("Error when performing an action: The player is null."); return; }
        if(index >= inventory.getSize()){ Message.error("Error when performing an action: The specified item index exceeds the interface size ("
                + index + " >= " + this.inventory.getSize() + ")"); return; }
        Slot slot = items.getOrDefault(index, null);
        slot.execute(player);
    }
}
