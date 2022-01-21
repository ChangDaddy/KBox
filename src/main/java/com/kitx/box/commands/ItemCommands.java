package com.kitx.box.commands;

import com.kitx.box.utils.ColorUtil;
import me.gleeming.command.Command;
import me.gleeming.command.paramter.Param;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCommands {

    @Command(names = {"enchant"}, description = "Enchants the item you are holding!", playerOnly = true, permission = "core.admin")
    public void commandEnchant(Player player, @Param(name = "enchant") Enchantment enchantment, @Param(name = "level") int level) {
        ItemStack item = player.getItemInUse();
        if(item == null) {
            player.sendMessage(ColorUtil.getHex("&cYou are not holding an item!"));
            return;
        }
        player.getInventory().remove(item);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }
}
