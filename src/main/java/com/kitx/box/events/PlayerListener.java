package com.kitx.box.events;

import com.kitx.box.Box;
import com.kitx.box.config.Config;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.utils.ColorUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.isCancelled()) return;
        Box.getInstance().getBlockPlaceLocations().add(event.getBlockPlaced());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Box.getInstance().getStatContainer().addPlayer(player);
        player.teleport(Config.SPAWN_LOCATION);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        Box.getInstance().getStatContainer().removePlayer(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("\247aMine Creator")) {
                PlayerData player = Box.getInstance().getStatContainer().getStatPlayerMap().get(event.getPlayer().getUniqueId());
                player.setPos1(event.getBlock().getLocation());
                player.getPlayer().sendMessage("\2477Set position 1");
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                sendMessageWhenReady(player);
                event.setCancelled(true);
            }
        } catch (NullPointerException ignored) {
            // ignored
        }
    }

    private void sendMessageWhenReady(PlayerData player) {
        if(player.getPos1() != null && player.getPos2() != null) {
            player.getPlayer().sendMessage("\247aMine position set! Do /mine setup [name] [MATERIAL]");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            try {
                if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("\247aMine Creator")) {
                    PlayerData player = Box.getInstance().getStatContainer().getStatPlayerMap().get(event.getPlayer().getUniqueId());
                    player.setPos2(event.getClickedBlock().getLocation());
                    player.getPlayer().sendMessage("\2477Set position 2");
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    sendMessageWhenReady(player);
                }
            } catch (NullPointerException ignored) {
                // ignored
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(event.getPlayer());
        String prefix = user.getCachedData().getMetaData().getPrefix();
        String suffix = user.getCachedData().getMetaData().getSuffix();
        PlayerData data = Box.getInstance().getStatContainer().get(event.getPlayer());
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";
        if (!event.getPlayer().hasPermission("core.vip")) {
            if (!data.getCooldown().hasCooldown(2)) {
                p.sendMessage(ColorUtil.getHex(String.format("&cThere is a &4%s &csecond chat delay!", data.getCooldown().getSeconds())));
                event.setCancelled(true);
            }
        }
        if (p.hasPermission("core.white")) {
            event.setMessage(ColorUtil.getHex(event.getMessage()));
            event.setFormat(ColorUtil.getHex(prefix + "" + p.getName() + suffix + ": &r") + "%2$s");
        } else {
            event.setFormat(ColorUtil.getHex(prefix + p.getName() + suffix + ": &7") + "%2$s");

        }
    }

}
