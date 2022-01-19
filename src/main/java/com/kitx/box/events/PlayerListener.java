package com.kitx.box.events;

import com.kitx.box.Box;
import com.kitx.box.config.Config;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.utils.ColorUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Objects;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.isCancelled() || event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        Box.getInstance().getBlockPlaceLocations().add(event.getBlockPlaced());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Box.getInstance().getStatContainer().addPlayer(player);
        event.setJoinMessage(ColorUtil.getHex("&8(&a+&8) &7" + player.getName()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnterPortal(PlayerChangedWorldEvent event) {
        event.getPlayer().teleport(Config.AFK_LOCATION);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnterPortal(EntityPortalEnterEvent event) {
        event.getEntity().teleport(Config.AFK_LOCATION);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) return;
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            PlayerData playerData =  Box.getInstance().getStatContainer().get(player);
            PlayerData damagerData = Box.getInstance().getStatContainer().get(damager);
            playerData.getCombatTag().resetTime();
            damagerData.getCombatTag().resetTime();

            if(!playerData.getNewbieProtection().isFinished() || !damagerData.getNewbieProtection().isFinished())  {
                event.setCancelled(true);

                damagerData.getPlayer().sendMessage(!damagerData.getNewbieProtection().isFinished() ?
                        "\247cYou cannot attack that person! You have newbie protection."
                        : "\247cThat person cannot be attacked because of newbie protection.");
            }
        } else if(event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Player player = (Player) event.getEntity();
            if(((Projectile) event.getDamager()).getShooter() instanceof Player) {
                Player damager = ((Player) Objects.requireNonNull(((Projectile) event.getDamager()).getShooter())).getPlayer();

                PlayerData playerData =  Box.getInstance().getStatContainer().get(player);
                assert damager != null;
                PlayerData damagerData = Box.getInstance().getStatContainer().get(damager);
                playerData.getCombatTag().resetTime();
                damagerData.getCombatTag().resetTime();

                if(!playerData.getNewbieProtection().isFinished() || !damagerData.getNewbieProtection().isFinished())  {
                    event.setCancelled(true);

                    damagerData.getPlayer().sendMessage(!damagerData.getNewbieProtection().isFinished() ?
                            "\247cYou cannot attack that person! You have newbie protection."
                            : "\247cThat person cannot be attacked because of newbie protection.");
                }
            }
        }
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        final PlayerData player = Box.getInstance().getStatContainer().get(event.getPlayer());

        String[] args = event.getMessage().split(" ");
        if(!player.getCombatTag().isFinished()) {
            if(args[0].contains("/spawn") || args[0].contains("/l")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(Config.SPAWN_LOCATION);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        try {
            final String killed = event.getEntity().getName();
            final String killer = event.getEntity().getKiller().getName();
            final String health = Math.round(event.getEntity().getKiller().getHealth() / 2) + "&c❤";

            event.setDeathMessage(
                    ColorUtil.getHex(
                    String.format(
                            "&c%s <#a5a5a5>w<#aaaaaa>a<#afafaf>s <#b3b3b3>k<#b8b8b8>i<#bdbdbd>l<#c2c2c2>l<#c7c7c7>e<#cbcbcb>d <#d0d0d0>b<#d5d5d5>y %s &7[&c%s&7]",
                            killed, killer, health
                    ))
            );

            final PlayerData entity = Box.getInstance().getStatContainer().get(event.getEntity());
            final PlayerData killerData = Box.getInstance().getStatContainer().get(event.getEntity().getKiller());
            killerData.setKills(killerData.getKills() + 1);
            entity.setDeaths(entity.getDeaths() + 1);
        } catch (NullPointerException ignored) {
            event.setDeathMessage("");
            final PlayerData entity = Box.getInstance().getStatContainer().get(event.getEntity());
            entity.setDeaths(entity.getDeaths() + 1);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        PlayerData data = Box.getInstance().getStatContainer().get(event.getPlayer());
        if(!data.getCombatTag().isFinished()) {

        }
        Box.getInstance().getStatContainer().removePlayer(player);
        event.setQuitMessage(ColorUtil.getHex("&8(&c-&8) &7" + player.getName()));
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

    @EventHandler(priority = EventPriority.MONITOR)
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
            if (!data.getCooldown().isFinished(2)) {
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
