package ru.waxera.beeLib.utils.gui.container;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.waxera.beeLib.utils.specials.items.ItemStackBuilder;
import ru.waxera.beeLib.utils.specials.string.StringUtils;
import ru.waxera.beeLib.utils.message.Message;

import java.util.ArrayList;

/**
 * Implementation of a graphical user interface (GUI) for interaction
 * between server players and a BeeLib-dependent plugin using Container interface.
 *
 * @version 1
 * @since v1.0
 * @author Salwarex
 */

public abstract class ContainerInterface implements Cloneable{
    protected Inventory inventory;
    protected ArrayList<Integer> bg_slots = new ArrayList<>();
    protected Material background = null;
    protected String title;
    protected boolean items_moving = false;
    private boolean DEFAULT_INTERFACE_HANDLER = true;

    public ContainerInterface(Player holder, String title, int size){
        this.title = StringUtils.format(title, null);
        this.inventory = Bukkit.createInventory(holder, size, this.title);
    }

    public void setBackground(ArrayList<Integer> bg_slots_, Material background_){
        this.bg_slots = bg_slots_;
        this.background = background_;
        initBackground();
    }

    public void customInterfaceHandlerFlag(){ DEFAULT_INTERFACE_HANDLER = false; }
    public boolean isDefaultInterfaceHandler(){ return DEFAULT_INTERFACE_HANDLER; }

    private void initBackground(){
        ItemStackBuilder cis = new ItemStackBuilder(background, 1);
        cis.setName(null, "&a");
        for(Integer slot : bg_slots){
            if(slot >= inventory.getSize()){
                Message.error(null, "Error in determining the background slot of the interface: (" + slot  + " >= " + inventory.getSize() + ")");
                return;
            }
            inventory.setItem(slot, cis.get());
        }
    }

    public void playerClickAction(InventoryClickEvent e) {}

    public String getTitle(){
        return this.title;
    }
    public void open(Player player){
        player.openInventory(inventory);
        InterfaceOpenedPool.getInstance().add(player, this);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ContainerInterface clone = (ContainerInterface) super.clone();

        Inventory clonedInventory = Bukkit.createInventory(null, this.inventory.getSize(), this.title);
        for (int i = 0; i < this.inventory.getSize(); i++) {
            ItemStack item = this.inventory.getItem(i);
            if (item != null) {
                clonedInventory.setItem(i, item.clone());
            }
        }
        clone.inventory = clonedInventory;

        clone.bg_slots = new ArrayList<>(this.bg_slots);

        clone.background = this.background;
        clone.title = this.title;
        clone.items_moving = this.items_moving;
        clone.DEFAULT_INTERFACE_HANDLER = this.DEFAULT_INTERFACE_HANDLER;

        return clone;
    }
}
