package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import ru.waxera.beeLib.utils.message.Message;

import java.util.HashMap;

public class SinglePageInterface extends ContainerInterface{
    private HashMap<Integer, Slot> items = new HashMap<>();

    public SinglePageInterface(Player holder, String title, int size, boolean items_moving) {
        super(holder, title, size);
        this.items_moving = items_moving;
    }

    public void setItem(Integer index, ItemStack itemStack, Action action){
        if(this.bg_slots.contains(index)) { Message.error(null, "Couldn't add an item to slot " + index + "! There is background item in it."); return; }
        if(index >= this.inventory.getSize()){ Message.error(null, "The specified item index exceeds the interface size (" + index + " >= " + this.inventory.getSize() + ")"); return;}
        Slot slot = new Slot(itemStack, action);
        this.items.put(index, slot);
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
        if(index == -1){ Message.error(null, "Inventory filling error: There are no available slots in the inventory."); return; }
        this.setItem(index, itemStack, action);
    }

    private void initSlots(){
        for(Integer index : items.keySet()){
            this.inventory.setItem(index, items.get(index).getItemStack());
        }
    }

    @Override
    public void playerClickAction(InventoryClickEvent e){
        if(e.getClickedInventory().getType() != InventoryType.CHEST) { e.setCancelled(true); return; }

        Player player = (Player) e.getWhoClicked();
        Integer index = e.getSlot();

        if(!this.items_moving || this.bg_slots.contains(index)) e.setCancelled(true);

        if(player == null) { Message.error(null, "Error when performing an action: The player is null."); return; }
        if(index >= inventory.getSize()){ Message.error(null, "Error when performing an action: The specified item index exceeds the interface size ("
                + index + " >= " + this.inventory.getSize() + ")"); return; }
        Slot slot = items.getOrDefault(index, null);
        if(slot == null) return;
        slot.execute(player);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        SinglePageInterface clone = (SinglePageInterface) super.clone();

        HashMap<Integer, Slot> temp = new HashMap<>();
        for (Integer key : this.items.keySet()) {
            Slot originalSlot = this.items.get(key);
            temp.put(key, new Slot(originalSlot.getItemStack().clone(), originalSlot.getAction()));
        }
        clone.items = temp;

        return clone;
    }
}
