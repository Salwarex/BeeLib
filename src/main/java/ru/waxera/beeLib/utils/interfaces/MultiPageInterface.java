package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.waxera.beeLib.utils.message.Message;

import java.util.Collections;
import java.util.HashMap;

public class MultiPageInterface extends ContainerInterface{
    private static HashMap<Player, Integer> pages = new HashMap<>();
    private HashMap<Integer, Slot> content;
    private HashMap<Integer, Slot> multipage_content;

    public MultiPageInterface(Player holder, String title, int page_size, boolean items_moving, ItemStack next_page, ItemStack prev_page) {
        super(holder, title, page_size);
        this.items_moving = items_moving;

        int next_page_slot = page_size - 1;
        int prev_page_slot = page_size - 9;
        int max_pages = (int) Math.ceil((double) Collections.max(content.keySet()) / page_size);

        this.setItem(next_page_slot, next_page, (player) -> {
            if(!pages.containsKey(player)){
                pages.put(player, 0);
            }
            int now_page = pages.get(player);
            if(now_page == max_pages) return;
            pages.replace(player, now_page + 1);
            open(player);
        }, true);
        this.setItem(prev_page_slot, prev_page, (player -> {
            if(!pages.containsKey(player)){
                pages.put(player, 0);
            }
            int now_page = pages.get(player);
            if(now_page == 0) return;
            pages.replace(player, now_page-1);
            open(player);
        }), true);
    }

    public void setItem(Integer index, ItemStack itemStack, Action action, boolean multipage){
        if(content.containsKey(index)) { Message.error("Couldn't add an item to slot " + index + "! There is another item in it."); return; }
        Slot slot = new Slot(itemStack, action);
        if(!multipage) content.put(index, slot);
        else {
            if(this.bg_slots.contains(index)) { Message.error("Couldn't add an item to slot " + index + "! There is background item in it."); return; }
            multipage_content.put(index, slot);
        }
    }

    public void addItem(ItemStack itemStack, Action action){
        Integer index = -1;
        while (this.content.containsKey(index)
                || this.bg_slots.contains(index)
                || this.multipage_content.containsKey(index)) {
            index++;
        }
        setItem(index, itemStack, action, false);
    }

    @Override
    public void open(Player player){
        if(!pages.containsKey(player)) pages.put(player, 0);
        int page = pages.get(player);

        for(Integer mp_content_index : multipage_content.keySet()){
            this.inventory.setItem(mp_content_index, multipage_content.get(mp_content_index).getItemStack());
        }

        int this_page_max_slot = (this.inventory.getSize() * (page + 1)) - 1;
        int this_page_min_slot = this.inventory.getSize() * page;

        for(Integer content_index: content.keySet()){
            if(content_index >= this_page_min_slot && content_index <= this_page_max_slot){
                this.inventory.setItem(content_index, content.get(inventory).getItemStack());
            }
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
        Slot slot = content.getOrDefault(index, null);
        slot = slot == null ? multipage_content.getOrDefault(index, null) : slot;
        slot.execute(player);
    }

}
