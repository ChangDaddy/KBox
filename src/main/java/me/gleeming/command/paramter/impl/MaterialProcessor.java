package me.gleeming.command.paramter.impl;

import me.gleeming.command.paramter.Processor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class MaterialProcessor implements Processor {
    @Override
    public Object process(CommandSender sender, String supplied) {
        try {
            return Material.valueOf(supplied);
        } catch(Exception ex) {
            sender.sendMessage(ChatColor.RED + "The value you entered '" + supplied + "' is an invalid material.");
            return null;
        }
    }
}
