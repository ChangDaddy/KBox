package com.kitx.box.commands;

import com.kitx.box.Box;
import com.kitx.box.config.Config;
import com.kitx.box.stats.PlayerData;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import me.gleeming.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainCommands {

    @Command(names = {"spawn"}, playerOnly = true, description = "Teleports the player to spawn")
    public void onCommand(Player player) {
        PlayerData statPlayer = Box.getInstance().getStatContainer().getStatPlayerMap().get(player.getUniqueId());
        if (statPlayer.getCombatTag().isFinished()) {
            player.teleport(Config.SPAWN_LOCATION);
        }
    }

    @Command(names = {"dailyreward"}, description = "Gives you the daily reward!", playerOnly = true)
    public void dailyReward(Player player) {
        PlayerData statPlayer = Box.getInstance().getStatContainer().get(player);
        final boolean reward = System.currentTimeMillis() - statPlayer.getLastDailyReward() > 8.64e+7;
        SGMenu menu = Box.getInstance().getSpiGUI().create("&a&lDaily Reward", 3);

        final double hours = (System.currentTimeMillis() - statPlayer.getLastDailyReward()) / 3.6e+6;

        SGButton button = new SGButton(
                new ItemBuilder(reward ? Material.CHEST : Material.RED_STAINED_GLASS_PANE)
                        .name("&5&lDaily Reward")
                        .lore(reward ? "&b&lClick to receive daily reward!" : "&cYou have already received your daily reward")
                        .lore(reward ? "" : "&cYou must wait: " + hours + " hours.")
                        .build()
        ).withListener(event -> {
            if (reward) {
                player.sendMessage(ChatColor.GREEN + "You received your daily reward!");
                statPlayer.setLastDailyReward(System.currentTimeMillis());
                player.closeInventory();
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + hours + " hours to get a new reward!");
            }
        });

        menu.setButton(13, button);

        player.openInventory(menu.getInventory());
    }
}
