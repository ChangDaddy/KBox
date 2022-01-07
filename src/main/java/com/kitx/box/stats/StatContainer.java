package com.kitx.box.stats;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class StatContainer {
    private final Map<UUID, StatPlayer> statPlayerMap = new HashMap<>();

    public void addPlayer(Player player) {
        statPlayerMap.put(player.getUniqueId(), new StatPlayer(player));
    }

    public void removePlayer(Player player) {
        statPlayerMap.remove(player.getUniqueId());
    }

    public void save() {
        for(StatPlayer statPlayer : statPlayerMap.values()) {
            statPlayer.saveData();
        }
    }
}
