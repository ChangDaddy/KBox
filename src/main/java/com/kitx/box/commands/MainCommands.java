package com.kitx.box.commands;

import com.kitx.box.Box;
import com.kitx.box.config.Config;
import com.kitx.box.stats.StatPlayer;
import me.gleeming.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MainCommands {
    @Command(names = {"spawn"}, playerOnly = true, description = "Teleports the player to spawn")
    public void onCommand(Player player) {
        StatPlayer statPlayer = Box.getInstance().getStatContainer().getStatPlayerMap().get(player.getUniqueId());
        if(statPlayer.getCombatTag().isFinished()) {
            player.teleport(Config.SPAWN_LOCATION);
        }
    }
}
