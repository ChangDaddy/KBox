package com.kitx.box.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@UtilityClass
public class ItemUtils {

    public ItemStack createItem(Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
