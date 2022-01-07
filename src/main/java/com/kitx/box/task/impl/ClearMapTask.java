package com.kitx.box.task.impl;

import com.kitx.box.Box;
import com.kitx.box.mine.Mine;
import com.kitx.box.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ClearMapTask extends Task {


    @Override
    public void init() {

    }

    private void resetMines() {
        Box.getInstance().getMineContainer().getMines().forEach(Mine::clean);
    }
}
