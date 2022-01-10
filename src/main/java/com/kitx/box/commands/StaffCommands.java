package com.kitx.box.commands;

import com.kitx.box.Box;
import com.kitx.box.config.Config;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.utils.ColorUtil;
import com.kitx.box.utils.LocationUtil;
import com.samjakob.spigui.item.ItemBuilder;
import me.gleeming.command.Command;
import me.gleeming.command.paramter.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class StaffCommands {

    @Command(names = {"gmc", "creative"}, permission = "core.admin")
    public void creative(final Player player, @Param(name = "player", required = false) Player target) {
        if (target == null) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage("\247aSet gamemode to creative");
        } else {
            if (!target.isOnline()) {
                player.sendMessage(ChatColor.RED + "That player does not exist!");
            }
            target.setGameMode(GameMode.CREATIVE);
            player.sendMessage("\247aSet gamemode to creative for " + target.getName());
        }
    }

    @Command(names = {"setafkspawn"}, permission = "core.admin", playerOnly = true)
    public void setAfkSpawn(Player player) {
        Box.getInstance().getConfig().set("afkspawn", LocationUtil.parseToString(player.getLocation()));
        Config.AFK_LOCATION = player.getLocation();
        try {
            Box.getInstance().getConfig().save(new File(Box.getInstance().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.GREEN + "Set spawn location");
    }

    @Command(names = {"tag"}, permission = "core.admin", playerOnly = true)
    public void tagCommand(Player player, @Param(name = "material") Material material, @Param(name = "name") String name, @Param(name = "command", concated = true) String command) {
        Box.getInstance().getTagContainer().createTag(material.name(), name, command);
        player.getInventory().addItem(new ItemBuilder(Material.NAME_TAG)
                    .name(name)
                    .lore("&8Right click to be granted with the rank.")
                    .enchant(Enchantment.DURABILITY, 10)
                .build());
        player.sendMessage("\247aCreated tag.");
    }

    @Command(names = {"vanish"}, permission = "core.admin")
    public void vanish(final Player player, @Param(name = "player", required = false) Player target) {
        PlayerData data = Box.getInstance().getStatContainer().get(target != null ? target : player);
        data.vanish();
        player.sendMessage(target == null
                ? data.isVanished()
                ? "\247aVanished"
                : "\247cUnvanished"
                : ((data.isVanished()
                ? "\247aVanished "
                : "\247aUnvanished ") + target.getName())
        );
    }

    @Command(names = {"setspawn"}, permission = "core.admin", playerOnly = true)
    public void setSpawn(Player player) {
        Box.getInstance().getConfig().set("spawn", LocationUtil.parseToString(player.getLocation()));
        Config.SPAWN_LOCATION = player.getLocation();
        try {
            Box.getInstance().getConfig().save(new File(Box.getInstance().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.GREEN + "Set spawn location");
    }

    @Command(names = {"sc", "staffchat"}, permission = "core.staff", description = "Allows users to talk in staffchat")
    public void staffChatCommand(final CommandSender sender, @Param(name = "message", concated = true) String message) {
        Bukkit.broadcast(ColorUtil.getHex("&7[&cSC&7] &c" + sender.getName() + " » &f" + message), "core.staff");
    }
}
