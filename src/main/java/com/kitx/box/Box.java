package com.kitx.box;

import com.kitx.box.config.Config;
import com.kitx.box.file.FileManager;
import com.kitx.box.mine.MineContainer;
import com.kitx.box.stats.StatContainer;
import com.kitx.box.task.TaskBuilder;
import com.samjakob.spigui.SpiGUI;
import lombok.Getter;
import me.gleeming.command.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class Box extends JavaPlugin {
    private static Box instance;
    private SpiGUI spiGUI;
    private StatContainer statContainer;
    private MineContainer mineContainer;

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
        new FileManager();
        new Config().loadConfig();
        new TaskBuilder();
        statContainer = new StatContainer();
        spiGUI = new SpiGUI(this);
        mineContainer = new MineContainer();
        handleBukkit();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mineContainer.saveMines();
        super.onDisable();
    }

    public static Box getInstance() {
        return instance;
    }

    private void handleBukkit() {
        CommandHandler.registerCommands("com.kitx.box.commands", this);
    }
}
