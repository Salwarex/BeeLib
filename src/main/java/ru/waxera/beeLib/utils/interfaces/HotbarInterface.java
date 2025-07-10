package ru.waxera.beeLib.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.Storage;
import ru.waxera.beeLib.utils.message.Message;

@Deprecated
public class HotbarInterface {
    private Slot[] slots;
    private ItemStack[] defaultItems = new ItemStack[9];
    private Storage keepItemsFile;

    public HotbarInterface(Plugin plugin, Slot[] slots, String keepItemsFileName){
        this.slots = slots;
        if(slots.length != 9) { Message.error(null, "The number of slots in HotbarInterface should be 9. Fill the extra slots with pacifiers from Material.AIR! (" + slots.length + ")"); return;}
        this.keepItemsFile = new Storage(keepItemsFileName, "hotbar-keeps", BeeLib.getInstance());
    }

    public void open(Player player){
        PlayerInventory inventory = player.getInventory();
        for(int i = 0; i < 9; i++){
            defaultItems[i] = inventory.getItem(i);
            keepItemsFile.getConfig().set(player.getName() + ".inventory", defaultItems[i]);
        }
        keepItemsFile.save();

        //продолжить
    }

    public void setDefaultItems(ItemStack[] stacks){
        if(stacks.length != 9) { Message.error(null,"The number of saved items should be 9."); return;}
        this.defaultItems = stacks;
    }

}
