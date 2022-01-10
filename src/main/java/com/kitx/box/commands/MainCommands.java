package com.kitx.box.commands;

import com.kitx.box.Box;
import com.kitx.box.commands.gui.KitGui;
import com.kitx.box.config.Config;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.utils.ColorUtil;
import com.kitx.box.utils.MathUtil;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import me.gleeming.command.Command;
import me.gleeming.command.paramter.Param;
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

    @Command(names = {"stats"}, description = "Checks for player's stats", playerOnly = true)
    public void statsCommand(Player player, @Param(name = "player", required = false) Player target) {
        final PlayerData data = Box.getInstance().getStatContainer().get(target != null ? target : player);

        String kdr = "∞";
        try {
            kdr = String.valueOf(MathUtil.round((double) data.getKills() / data.getDeaths(), 1));
        } catch (Exception ignored) {
            // Ignored
        }

        player.sendMessage(ColorUtil.translate("&a" + data.getPlayer().getName() + "'s stats"));
        player.sendMessage(ColorUtil.translate("&a - Kills: " + data.getKills()));
        player.sendMessage(ColorUtil.translate("&a - Deaths: " + data.getDeaths()));
        player.sendMessage(ColorUtil.translate("&a - KS: " + data.getKillStreak()));
        player.sendMessage(ColorUtil.translate("&a - KDR: " + kdr));
    }

    @Command(names = {"spawn"}, description = "Teleports player to spawn", playerOnly = true)
    public void spawnCommand(Player player) {
        PlayerData data = Box.getInstance().getStatContainer().get(player);
        if(data.getCombatTag().isFinished()) {
            player.teleport(Config.SPAWN_LOCATION);
        } else {
            player.sendMessage(ChatColor.RED + "You are in combat!");
        }
    }

    @Command(names = {"kit", "kits"}, description = "Receive kits and perks", playerOnly = true)
    public void kitCommand(Player player) {
        PlayerData data = Box.getInstance().getStatContainer().get(player);
        new KitGui(data);
    }

    @Command(names = {"dailyreward"}, description = "Gives you the daily reward!", playerOnly = true)
    public void dailyReward(Player player) {
        PlayerData statPlayer = Box.getInstance().getStatContainer().get(player);
        final boolean reward = System.currentTimeMillis() - statPlayer.getLastDailyReward() > 8.64e+7;
        SGMenu menu = Box.getInstance().getSpiGUI().create("&a&lDaily Reward", 3);

        final double hours = (System.currentTimeMillis() - statPlayer.getLastDailyReward()) / 3.6e+6;

        final int hoursLeft = (int) (24 - hours);

        SGButton button = new SGButton(
                new ItemBuilder(reward ? Material.CHEST : Material.RED_STAINED_GLASS_PANE)
                        .name("&5&lDaily Reward")
                        .lore(
                                reward ? "&b&lClick to receive daily reward!" : "&cYou have already received your daily reward",
                                reward ? "" : "&cYou must wait: " + hoursLeft + " hours."
                        )
                        .build()
         ).withListener(event -> {
            if (reward) {
                player.sendMessage(ChatColor.GREEN + "You received your daily reward!");
                statPlayer.setLastDailyReward(System.currentTimeMillis());
                player.closeInventory();
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + hoursLeft + " hours to get a new reward!");
            }
        });

        menu.setButton(13, button);
        menu.setAutomaticPaginationEnabled(false);

        player.openInventory(menu.getInventory());
    }
}
