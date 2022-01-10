package com.kitx.box.mine;


import com.kitx.box.Box;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.utils.CountDown;
import com.kitx.box.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.io.BukkitObjectInputStream;

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
                mines.add(Mine.deserialize(load.getString(mine)));
                System.out.println(mine);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void create(final String name, final int delay, boolean broadcast, PlayerData data) {
        final Location pos1 = data.getPos1();
        final Location pos2 = data.getPos2();
        
        final int topBlockX = (Math.max(pos1.getBlockX(), pos2.getBlockX()));
        final int bottomBlockX = (Math.min(pos1.getBlockX(), pos2.getBlockX()));

        final int topBlockY = (Math.max(pos1.getBlockY(), pos2.getBlockY()));
        final int bottomBlockY = (Math.min(pos1.getBlockY(), pos2.getBlockY()));

        final int topBlockZ = (Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
        final int bottomBlockZ = (Math.min(pos1.getBlockZ(), pos2.getBlockZ()));

        final World world = pos1.getWorld();

        final List<MappedBlock> mappedBlocks = new ArrayList<>();

        for (int x = bottomBlockX; x <= topBlockX; x++) {

            for (int z = bottomBlockZ; z <= topBlockZ; z++) {

                for (int y = bottomBlockY; y <= topBlockY; y++) {

                    assert world != null;
                    Block block = world.getBlockAt(x, y, z);
                    mappedBlocks.add(new MappedBlock(new SerializedLocation(block.getLocation().getWorld().getName(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), block.getType().name()));
                }
            }
        }

        mines.add(new Mine(name, mappedBlocks, new CountDown(delay), broadcast));
    }

    public void saveMines() {
        try {
            File file = new File(plugin.getDataFolder(), "mines.yml");
            if(!file.exists()) file.createNewFile();

            YamlConfiguration load = YamlConfiguration.loadConfiguration(file);
            for(Mine mine : mines) {
                load.set(mine.getName(), mine.serialize());
            }

            load.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
