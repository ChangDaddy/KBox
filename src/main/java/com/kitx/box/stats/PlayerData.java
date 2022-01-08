package com.kitx.box.stats;

import com.kitx.box.Box;
import com.kitx.box.utils.Cooldown;
import com.kitx.box.utils.CountDown;
import com.kitx.box.utils.MathUtil;
import com.kitx.box.utils.scoreboardapi.FastBoard;
import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {
    private final Player player;
    private final UUID uuid;

    private final FastBoard board; // Scoreboard for the stats

    private final CountDown combatTag = new CountDown(30);
    private final Cooldown cooldown = new Cooldown();
    private int kills, deaths, coins, killStreak;
    private Location pos1, pos2;
    private final String prefix, rank;
    private boolean vanished;
    private long lastDailyReward;

    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        board = new FastBoard(player);
        loadData();

        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        this.prefix = user.getCachedData().getMetaData().getPrefix();
        this.rank = user.getPrimaryGroup();
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
            setLastDailyReward(load.getLong("dailyReward"));
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
            load.set("dailyReward", lastDailyReward);

            try {
                load.save(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateScoreboard() {
        player.setPlayerListName(player.getDisplayName());

        String kdr = "∞";
        try {
            kdr = String.valueOf(MathUtil.round((double) kills / deaths, 1));
        } catch (Exception ignored) {
            // Ignored
        }

        board.updateTitle("<#ffff55>&lK<#f7e64d>&li<#eecc44>&lt<#e6b33c>&lX <#dd9933>&l│ <#d5802b>&lB<#cc6622>&lo<#c44d1a>&lx <#bb3311>&lP<#b31a09>&lV<#aa0000>&lP");
        board.updateLine(0, "&8&m                 ");
        board.updateLine(1, "<#326BA0>┌&l" + player.getName() + " &7(" + Math.round(player.getHealth() / 2) + "&c❤&7)");
        board.updateLine(2, "<#326BA0>│ Rank: " + prefix);
        board.updateLine(3, "<#326BA0>│ Kills: &f" + kills);
        board.updateLine(3, "<#326BA0>│ Deaths: &f" + deaths);
        board.updateLine(4, "<#326BA0>│ KDR: &f" + kdr);
        board.updateLine(5, "");
        board.updateLine(6, "<#326BA0>┌&lSERVER");
        board.updateLine(7, "<#326BA0>│ TPS: &f" + 20);
        board.updateLine(8, "<#326BA0>│ Map Reset: &f" + Box.getInstance().getNextReset().convertTime());
        board.updateLine(9, "<#326BA0>│ IP: &fkitx.minehut.gg");
        board.updateLine(10, "&8&m                 ");
    }

    public void vanish() {
        vanished = !vanished;
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!player.hasPermission("core.staff"))  {
                if(vanished) {
                    player.hidePlayer(this.player);
                } else {
                    player.showPlayer(this.player);
                }
            }
        }
    }
}
