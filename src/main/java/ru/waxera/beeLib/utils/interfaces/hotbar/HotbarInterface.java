package ru.waxera.beeLib.utils.interfaces.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.utils.interfaces.Slot;
import ru.waxera.beeLib.utils.message.Message;


public class HotbarInterface {
    private final Player holder;
    private Slot[] slots;
    private HoldingItems holdingItems;

    public HotbarInterface(Plugin plugin, Player holder, Slot[] slots){
        this.holder = holder;
        if(slots.length != 9) { Message.error(null, "&cSlots list length can't different from 9");}
        this.slots = slots;
    }

    public void setHoldingItems(HoldingItems holdingItems){
        this.holdingItems = holdingItems;
    }

    public void open(){
        PlayerInventory inventory = this.holder.getInventory();
        for(int i = 0; i < 9; i++){
            inventory.setItem(i, slots[i].getItemStack());
        }
        HotbarInterfaceOpenedList.put(holder, this);
    }

    public void close(){
        holdingItems.restore();
        HotbarInterfaceOpenedList.remove(holder);
    }

    public void execute(int index, Event e){
        Slot slot = slots[index];
        if(slot == null) return;
        slot.execute(holder, e);
    }

}
