package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import ru.waxera.beeLib.utils.ItemStackBuilder;
import ru.waxera.beeLib.utils.StringUtils;
import ru.waxera.beeLib.utils.message.Message;

import java.util.ArrayList;

public class ContainerInterface {
    protected Inventory inventory;
    protected ArrayList<Integer> bg_slots = new ArrayList<>();
    protected Material background = null;
    protected String title;
    protected boolean items_moving = false;

    public ContainerInterface(Player holder, String title, int size){
        this.title = StringUtils.format(title);
        this.inventory = Bukkit.createInventory(holder, size, this.title);
        InterfaceTemplatesList.add(this);
    }

    public void setBackground(ArrayList<Integer> bg_slots_, Material background_){
        this.bg_slots = bg_slots_;
        this.background = background_;
        initBackground();
    }

    private void initBackground(){
        ItemStackBuilder cis = new ItemStackBuilder(background, 1);
        cis.setName("&a");
        for(Integer slot : bg_slots){
            if(slot >= inventory.getSize()){
                Message.error("Error in determining the background slot of the interface: (" + slot  + " >= " + inventory.getSize() + ")");
                return;
            }
            inventory.setItem(slot, cis.get());
        }
    }

//    protected void saveTemplate(){
//        TemplatesList.add(this);
//    }

    public void playerClickAction(InventoryClickEvent e) {}

    public String getTitle(){
        return this.title;
    }
    public void open(Player player){
        player.openInventory(inventory);
    }
}
