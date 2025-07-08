package ru.waxera.beeLib.utils;


import de.tr7zw.nbtapi.NBTItem;
import jdk.jfr.Description;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.message.Message;

import java.util.ArrayList;

public class ItemStackBuilder {
    private ItemStack itemStack;

    public ItemStackBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(StringUtils.format(name));
        itemStack.setItemMeta(meta);
    }

    @Description("Only for PLAYER_HEAD material!")
    public void setHead(Player player){
        if(this.itemStack.getType() != Material.PLAYER_HEAD){ Message.error("Head Creation error: This item (" + this.itemStack.getType()
                + ") is not a player's head."); return; }
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        if(meta != null && player != null && player.hasPlayedBefore()){
            meta.setOwningPlayer(player);
            itemStack.setItemMeta(meta);
        }
    }

    public void setLore(String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        ArrayList<String> loreFin = new ArrayList<>();
        for (String str : lore) {
            loreFin.add(StringUtils.format(str));
        }
        meta.setLore(loreFin);
        itemStack.setItemMeta(meta);
    }

    public void setEnchantments(int[] levels, Enchantment... enchantments) {
        for (int i = 0; i < enchantments.length; i++) {
            itemStack.addUnsafeEnchantment(enchantments[i], levels[i]);
        }
    }

    public void hideAttributes() {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ARMOR_TRIM,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_STORED_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ITEM_SPECIFICS
        );
        itemStack.setItemMeta(meta);
    }

    public boolean setNBT(String key, String nbtString) {
        if (BeeLib.checkSoftDeps("nbtapi")) {
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setString(key, nbtString);
            itemStack = nbtItem.getItem();
            return true;
        }
        return false;
    }

    public static boolean setNBT(ItemStack itemStack, String key, String nbtString) {
        if (BeeLib.checkSoftDeps("nbtapi")) {
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setString(key, nbtString);
            itemStack = nbtItem.getItem();
            return true;
        }
        return false;
    }

    public static String getNBT(ItemStack itemStack, String key) {
        if (BeeLib.checkSoftDeps("nbtapi")) {
            NBTItem nbtItem = new NBTItem(itemStack);
            if (nbtItem.getKeys().contains(key)) {
                return nbtItem.getString(key);
            }
        }
        return null;
    }

    public ItemStack get() {
        return itemStack;
    }
}