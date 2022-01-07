package com.kitx.box.config;

import com.kitx.box.Box;
import com.kitx.box.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {


    public static String KILL_MESSAGE, RECEIVED_KIT;
    public static int broadcasts;
    public static long BROADCAST_DELAY;
    public static long CHAT_CD;
    public static Location SPAWN_LOCATION;

    public void loadConfig() {
        FileConfiguration config = Box.getInstance().getConfig();

        KILL_MESSAGE = config.getString("killMessage");
        RECEIVED_KIT = config.getString("receiveKit");
        BROADCAST_DELAY = config.getLong("broadcast-delay");
        CHAT_CD = config.getLong("chat-delay");
        SPAWN_LOCATION = LocationUtil.parseToLocation("spawn");
        for (String key : config.getConfigurationSection("announcements").getKeys(false)) {
            broadcasts++;
        }
    }
}
