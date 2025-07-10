package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import ru.waxera.beeLib.utils.message.Message;

import java.util.Collections;
import java.util.HashMap;

public class MultiPageInterface extends ContainerInterface{
    private HashMap<Integer, Slot> content = new HashMap<>();
    private HashMap<Integer, Slot> multipage_content = new HashMap<>();
    private ItemStack prev_mat_button;
    private ItemStack next_mat_button;

    private int max_pages = -1;

    public MultiPageInterface(Player holder, String title, int page_size, boolean items_moving, ItemStack prev_page, ItemStack next_page) {
        super(holder, title, page_size);
        this.items_moving = items_moving;
        this.prev_mat_button = prev_page;
        this.next_mat_button = next_page;
    }

    private void createNavButtons(Integer page){
        int next_page_slot = this.inventory.getSize() - 1;
        int prev_page_slot = this.inventory.getSize() - 9;

        multipage_content.remove(next_page_slot);
        multipage_content.remove(prev_page_slot);

        if(page != max_pages){
            this.setItem(next_page_slot, next_mat_button, (player, obj) -> {
                this.open(player, page + 1);
            }, true);
        }
        if(page != 0){
            this.setItem(prev_page_slot, prev_mat_button, (player, obj) -> {
                this.open(player, page - 1);
            }, true);
        }
    }

    public void setItem(Integer index, ItemStack itemStack, Action action, boolean multipage){
        Slot slot = new Slot(itemStack, action);
        if(!multipage) {
            if(this.bg_slots.contains(index % this.inventory.getSize()) || this.multipage_content.containsKey(index % this.inventory.getSize())) { Message.error(null, "Couldn't add an item to slot " + index + "! There is multipage item in it."); return; }
            content.put(index, slot);
        }
        else {
            multipage_content.put(index, slot);
        }

        if (content.isEmpty()) {
            max_pages = 0;
        } else {
            max_pages = (int) Math.floor((double) Collections.max(content.keySet()) / this.inventory.getSize());
        }
    }

    public void addItem(ItemStack itemStack, Action action){
        Integer index = 0;
        while (this.content.containsKey(index)
                || this.bg_slots.contains(index % this.inventory.getSize())
                || this.multipage_content.containsKey(index % this.inventory.getSize())) {
            index++;
        }
        setItem(index, itemStack, action, false);
    }

    public void open(Player player, Integer page){
        this.inventory.clear();
        this.setBackground(this.bg_slots, this.background);
        createNavButtons(page);

        for(Integer mp_content_index : multipage_content.keySet()){
            this.inventory.setItem(mp_content_index, multipage_content.get(mp_content_index).getItemStack());
        }

        int this_page_max_slot = (this.inventory.getSize() * (page + 1)) - 1;
        int this_page_min_slot = this.inventory.getSize() * page;

        for(Integer content_index: content.keySet()){
            if(content_index >= this_page_min_slot && content_index <= this_page_max_slot){
                int slot_index = content_index - (this.inventory.getSize() * page);

                this.inventory.setItem(slot_index, content.get(content_index).getItemStack());
            }
        }
        player.openInventory(this.inventory);
        InterfaceOpenedList.put(player, this);
    }

    @Override
    public void open(Player player){
        this.open(player, 0);
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
        Slot slot = content.getOrDefault(index, null);
        slot = slot == null ? multipage_content.getOrDefault(index, null) : slot;
        if(slot == null) return;
        slot.execute(player, e);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        MultiPageInterface clone = (MultiPageInterface) super.clone();

        clone.content = new HashMap<>();
        for (Integer key : this.content.keySet()) {
            Slot originalSlot = this.content.get(key);
            clone.content.put(key, new Slot(originalSlot.getItemStack().clone(), originalSlot.getAction()));
        }

        clone.multipage_content = new HashMap<>();
        for (Integer key : this.multipage_content.keySet()) {
            Slot originalSlot = this.multipage_content.get(key);
            clone.multipage_content.put(key, new Slot(originalSlot.getItemStack().clone(), originalSlot.getAction()));
        }

        clone.max_pages = this.max_pages;

        return clone;
    }

}
