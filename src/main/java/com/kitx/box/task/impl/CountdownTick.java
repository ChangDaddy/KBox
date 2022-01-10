package com.kitx.box.task.impl;

import com.kitx.box.Box;
import com.kitx.box.config.Config;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.task.Task;
import com.kitx.box.utils.WorldGuardUtils;
import com.sk89q.worldguard.WorldGuard;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

public class CountdownTick extends Task {

    @Override
    public void init() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Box.getInstance(), () -> {
            for (PlayerData player : Box.getInstance().getStatContainer().getStatPlayerMap().values()) {
                if (!player.getCombatTag().isFinished()) {
                    player.getCombatTag().countDown();
                    player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("\247cCombat: " + player.getCombatTag().getSeconds()));
                }

                if(WorldGuardUtils.isAFK(player.getPlayer())) {
                    player.getAfkZone().countDown();
                    player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("\247aNext Reward: " + player.getAfkZone().convertTime()));
                }

                if(player.getAfkZone().isFinished()) {
                    Bukkit.getScheduler().runTask(Box.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crates givekey " + player.getPlayer().getName() + " mine 1"));
                    player.getAfkZone().resetTime();
                    player.getPlayer().sendMessage("\247aYou have received a crate key for being afk!");
                }

                if(!player.getNewbieProtection().isFinished()) {
                    player.getNewbieProtection().countDown();
                }
            }
        }, 0, 20);
    }
}
