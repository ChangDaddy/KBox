package com.kitx.box.mine;


import com.kitx.box.Box;
import com.kitx.box.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MineContainer {
    private final Box plugin = Box.getInstance();
    @Getter private final List<Mine> mines = new ArrayList<>();

    public MineContainer() {
        loadMines();
    }

    private void loadMines() {
        try {
            File file = new File(plugin.getDataFolder(), "mines.yml");
            if (!file.exists()) file.createNewFile();

            YamlConfiguration load = YamlConfiguration.loadConfiguration(file);
            for(String mine : load.getKeys(false)) {
                final String name = load.getString(mine + ".name");
                final Location pos1 = LocationUtil.parseToLocation(load.getString(mine + ".pos1"));
                final Location pos2 = LocationUtil.parseToLocation(load.getString(mine + ".pos2"));
                final Material block = Material.valueOf(load.getString(mine + ".block"));
                mines.add(new Mine(name, pos1, pos2, block));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMines() {
        try {
            File file = new File(plugin.getDataFolder(), "mines.yml");
            if(!file.exists()) file.createNewFile();

            YamlConfiguration load = YamlConfiguration.loadConfiguration(file);
            for(Mine mine : mines) {
                load.set(mine.getName() + ".name", mine.getName());
                load.set(mine.getName() + ".pos1", LocationUtil.parseToString(mine.getPos1()));
                load.set(mine.getName() + ".pos2", LocationUtil.parseToString(mine.getPos2()));
                load.set(mine.getName() + ".block", mine.getBlock().name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
