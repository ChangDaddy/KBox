package com.kitx.box;

import com.kitx.box.config.Config;
import com.kitx.box.events.PlayerListener;
import com.kitx.box.mine.MineContainer;
import com.kitx.box.stats.DataContainer;
import com.kitx.box.tag.TagContainer;
import com.kitx.box.task.TaskBuilder;
import com.kitx.box.utils.CountDown;
import com.samjakob.spigui.SpiGUI;
import lombok.Getter;
import me.gleeming.command.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Box extends JavaPlugin {
    private static Box instance;
    private SpiGUI spiGUI;
    private DataContainer statContainer;
    private MineContainer mineContainer;
    private TagContainer tagContainer;
    private final CountDown nextReset = new CountDown(1800);
    private final List<Block> blockPlaceLocations = new ArrayList<>();

    @Override
    public void onLoad() {
        instance = this;
        final File f = new File(getDataFolder(), "config.yml");
        if (!f.exists()) {
            saveResource("config.yml", true);

        }
        super.onLoad();
    }

    @Override
    public void onEnable() {
        new Config().loadConfig();
        statContainer = new DataContainer();
        spiGUI = new SpiGUI(this);
        mineContainer = new MineContainer();
        tagContainer = new TagContainer();
        new TaskBuilder();
        handleBukkit();
        statContainer.load();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mineContainer.saveMines();
        statContainer.save();
        tagContainer.saveTags();
        Box.getInstance().getBlockPlaceLocations().forEach(block -> block.setType(Material.AIR));
        super.onDisable();
    }

    public static Box getInstance() {
        return instance;
    }

    private void handleBukkit() {
        CommandHandler.registerCommands("com.kitx.box.commands", this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(tagContainer, this);
    }
}
