package com.kitx.box.stats;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class DataContainer {
    private final Map<UUID, PlayerData> statPlayerMap = new HashMap<>();

    public void addPlayer(Player player) {
        for (PlayerData data : statPlayerMap.values()) {
            if (data.isVanished() && !player.hasPermission("core.staff")) {
                player.hidePlayer(data.getPlayer());
            }
            player.setPlayerListName(player.getDisplayName());
        }

        statPlayerMap.put(player.getUniqueId(), new PlayerData(player));
    }

    public void removePlayer(Player player) {
        statPlayerMap.get(player.getUniqueId()).saveData();
        statPlayerMap.remove(player.getUniqueId());
    }

    public void save() {
        for(PlayerData statPlayer : statPlayerMap.values()) {
            statPlayer.saveData();
        }
    }

    public void load() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            addPlayer(player);
        }
    }

    public PlayerData get(final Player player) {
        return statPlayerMap.get(player.getUniqueId());
    }
}
