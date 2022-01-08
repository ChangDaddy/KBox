package com.kitx.box.task.impl;


import com.kitx.box.Box;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.task.Task;
import org.bukkit.Bukkit;

public class Scoreboard extends Task {

    @Override
    public void init() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Box.getInstance(), () -> Box.getInstance().getStatContainer().getStatPlayerMap().values()
                .forEach(PlayerData::updateScoreboard), 0, 10);
    }
}
