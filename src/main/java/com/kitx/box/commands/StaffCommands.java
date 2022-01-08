package com.kitx.box.commands;

import com.kitx.box.Box;
import com.kitx.box.config.Config;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.utils.LocationUtil;
import me.gleeming.command.Command;
import me.gleeming.command.paramter.Param;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
            if(!target.isOnline()) {
                player.sendMessage(ChatColor.RED + "That player does not exist!");
            }
            target.setGameMode(GameMode.CREATIVE);
            player.sendMessage("\247aSet gamemode to creative for " + target.getName());
        }
    }

    @Command(names = {"vanish"}, permission = "core.admin")
    public void vanish(final Player player, @Param(name = "player", required = false) Player target) {
        if(target != null) {
            PlayerData data = Box.getInstance().getStatContainer().get(target);
            data.vanish();
            player.sendMessage(data.isVanished() ?
                    "\247aVanished " + target.getName() :
                    "\247cUnvanished " + target.getName());
        } else {
            PlayerData data = Box.getInstance().getStatContainer().get(player);
            data.vanish();
            player.sendMessage(data.isVanished() ?
                    "\247aVanished":
                    "\247cUnvanished");
        }
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
}
