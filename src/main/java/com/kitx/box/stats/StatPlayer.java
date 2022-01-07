package com.kitx.box.stats;

import com.kitx.box.Box;
import com.kitx.box.utils.Cooldown;
import com.kitx.box.utils.CountDown;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
public class StatPlayer {
    private final Player player;
    private final UUID uuid;

    private final CountDown combatTag = new CountDown(30);
    private int kills, deaths, coins, killStreak;
    private Location pos1, pos2;
    public StatPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public void loadData() {
        final File dir = new File(Box.getInstance().getDataFolder(), "data");

        final File player = new File(dir, getPlayer().getUniqueId() + ".yml");

        if (!player.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                player.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final YamlConfiguration load = YamlConfiguration.loadConfiguration(player);
            setKills(load.getInt("kills"));
            setKillStreak(load.getInt("killstreak"));
            setCoins(load.getInt("coins"));
            setDeaths(load.getInt("deaths"));
        }
    }

    public void saveData() {
        final File dir = new File(Box.getInstance().getDataFolder(), "data");

        if (!dir.exists()) //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();

        final File player = new File(dir, getPlayer().getUniqueId() + ".yml");

        if (!player.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                player.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final YamlConfiguration load = YamlConfiguration.loadConfiguration(player);
            load.set("deaths", deaths);
            load.set("kills", kills);
            load.set("coins", coins);
            load.set("killstreak", killStreak);

            try {
                load.save(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
