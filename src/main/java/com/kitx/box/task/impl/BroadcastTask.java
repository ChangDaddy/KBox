package com.kitx.box.task.impl;

import com.kitx.box.Box;
import com.kitx.box.config.Config;
import com.kitx.box.stats.StatPlayer;
import com.kitx.box.task.Task;
import com.kitx.box.utils.DefaultFontInfo;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BroadcastTask extends Task {
    private final static int CENTER_PX = 154;
    private int counter;

    @Override
    public void init() {
        FileConfiguration config = Box.getInstance().getConfig();

        new BukkitRunnable() {

            @Override
            public void run() {
                counter++;
                if (counter > Config.broadcasts) {
                    counter = 1;
                }
                List<String> messages = config.getStringList("announcements.announcement" + counter);
                for (String message : messages) {

                    for (StatPlayer data : Box.getInstance().getStatContainer().getStatPlayerMap().values()) {
                        if (message.contains("[center]")) {
                            sendCenteredMessage(data.getPlayer(), message.replace("[center]", ""));
                        } else {
                            data.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                        data.saveData();
                    }

                }
            }
        }.runTaskTimerAsynchronously(Box.getInstance(), 0, Config.BROADCAST_DELAY);
    }

    public void sendCenteredMessage(Player player, String message) {
        if (message == null || message.equals(""))
            player.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;


        for (char c : message.toCharArray()) {
            if (c == '�') {
                previousCode = true;
                continue;
            } else if (previousCode == true) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else
                    isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb + message);
    }
}
