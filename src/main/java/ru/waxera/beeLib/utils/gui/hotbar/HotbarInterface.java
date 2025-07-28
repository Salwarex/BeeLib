package ru.waxera.beeLib.utils.gui.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.utils.gui.Slot;
import ru.waxera.beeLib.utils.message.Message;

/**
 * Implementation of a graphical user interface (GUI) for interaction
 * between server players and a BeeLib-dependent plugin using HotBar item's bar.
 *
 * @version 1
 * @since v1.1.1
 * @author Salwarex
 */

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
        HotbarInterfaceOpenedPool.getInstance().add(holder, this);
    }

    public void close(){
        holdingItems.restore();
        HotbarInterfaceOpenedPool.getInstance().remove(holder);
    }

    public void execute(int index, Event e){
        Slot slot = slots[index];
        if(slot == null) return;
        slot.execute(holder, e);
    }

}
