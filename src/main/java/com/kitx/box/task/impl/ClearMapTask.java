package com.kitx.box.task.impl;

import com.kitx.box.Box;
import com.kitx.box.mine.Mine;
import com.kitx.box.task.Task;
import com.kitx.box.utils.ColorUtil;
import com.kitx.box.utils.CountDown;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ClearMapTask extends Task {


    @Override
    public void init() {
        Bukkit.getScheduler().runTaskTimer(Box.getInstance(), () -> {
            // World reset here

            CountDown resetCooldown = Box.getInstance().getNextReset();
            resetCooldown.countDown();;

            if(resetCooldown.getSeconds() == 10) {
                Bukkit.broadcastMessage(ColorUtil.getHex("&7[<#0064fb>&lM<#135dfc>&li<#2656fc>&ln<#384ffd>&le<#4b48fd>&ls&7] &fMap reset in 10 seconds!"));
            }

            if(resetCooldown.isFinished()) {
                resetMines();
                resetMap();
                resetCooldown.resetTime();
            }

            // Mine reset heree

            for(Mine mine : Box.getInstance().getMineContainer().getMines()) {
                mine.getReset().countDown();

                if(mine.getReset().getSeconds() == 10) {
                    Bukkit.broadcastMessage(ColorUtil.getHex("&7[<#0064fb>&lM<#135dfc>&li<#2656fc>&ln<#384ffd>&le<#4b48fd>&ls&7] &fMine &b" + mine.getName() + " &fwill be resetting in &b10s"));
                }

                if(mine.getReset().isFinished()) {
                    mine.clean();
                    mine.getReset().resetTime();
                }
            }
        }, 0, 20);
    }


    private void resetMap() {
        Box.getInstance().getBlockPlaceLocations().forEach(block -> block.setType(Material.AIR));
    }

    private void resetMines() {
        Box.getInstance().getMineContainer().getMines().forEach(Mine::clean);
    }
}
