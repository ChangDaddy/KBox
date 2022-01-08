package com.kitx.box.task.impl;

import com.kitx.box.Box;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.task.Task;
import org.bukkit.Bukkit;

public class CountdownTick extends Task {

    @Override
    public void init() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Box.getInstance(), () -> {
            for(PlayerData player : Box.getInstance().getStatContainer().getStatPlayerMap().values()) {
                if(!player.getCombatTag().isFinished()) player.getCombatTag().countDown();
            }
            }, 0, 20);
    }
}
