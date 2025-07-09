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

    private int page = 0;
    private int max_pages = -1;

    public MultiPageInterface(Player holder, String title, int page_size, boolean items_moving, ItemStack prev_page, ItemStack next_page) {
        super(holder, title, page_size);
        this.items_moving = items_moving;
        this.prev_mat_button = prev_page;
        this.next_mat_button = next_page;

        createNavButtons();
    }

    private void createNavButtons(){
        int next_page_slot = this.inventory.getSize() - 1;
        int prev_page_slot = this.inventory.getSize() - 9;

        if(page != max_pages){
            this.setItem(next_page_slot, next_mat_button, (player) -> {
                if(page == max_pages) return;
                MultiPageInterface clone = null;
                try{ clone = (MultiPageInterface) this.clone(); }
                catch (CloneNotSupportedException e) { e.printStackTrace(); }
                if(clone != null) clone.page += 1;

                assert clone != null;
                clone.open(player);
            }, true);
        }
        if(page != 0){
            this.setItem(prev_page_slot, prev_mat_button, (player -> {
                if(page == 0) return;
                MultiPageInterface clone = null;
                try{ clone = (MultiPageInterface) this.clone(); }
                catch (CloneNotSupportedException e) { e.printStackTrace(); }
                if(clone != null) clone.page -= 1;

                assert clone != null;
                clone.open(player);
            }), true);
        }
    }

    public void setItem(Integer index, ItemStack itemStack, Action action, boolean multipage){
        Slot slot = new Slot(itemStack, action);
        if(!multipage) {
            if(this.bg_slots.contains(index)) { Message.error(null, "Couldn't add an item to slot " + index + "! There is background item in it."); return; }
            content.put(index, slot);
        }
        else multipage_content.put(index, slot);

        if (content.isEmpty()) {
            max_pages = 0;
        } else {
            max_pages = (int) Math.floor((double) Collections.max(content.keySet()) / this.inventory.getSize());
        }
    }

    public void addItem(ItemStack itemStack, Action action){
        Integer index = 0;
        while (this.content.containsKey(index)
                || this.bg_slots.contains(index)
                || this.multipage_content.containsKey(index)) {
            index++;
        }
        setItem(index, itemStack, action, false);
    }

    @Override
    public void open(Player player){
        for(Integer mp_content_index : multipage_content.keySet()){
            this.inventory.setItem(mp_content_index, multipage_content.get(mp_content_index).getItemStack());
        }

        int this_page_max_slot = (this.inventory.getSize() * (page + 1)) - 1;
        int this_page_min_slot = this.inventory.getSize() * page;

        Message.send(null, "(" + this_page_min_slot + " " + this_page_max_slot + ")");
        for(Integer content_index: content.keySet()){
            Message.send(null, "? " + content_index);
            if(content_index >= this_page_min_slot && content_index <= this_page_max_slot){
                Message.send(null, "+ " + content_index);
                int slot_index = content_index - (this.inventory.getSize() * page);

                this.inventory.setItem(slot_index, content.get(content_index).getItemStack());
            }
        }
        player.openInventory(this.inventory);
        InterfaceOpenedList.put(player, this);
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
        slot.execute(player);
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
