package com.kitx.box.commands;

import com.kitx.box.Box;
import com.kitx.box.mine.Mine;
import com.kitx.box.stats.StatPlayer;
import com.kitx.box.utils.ColorUtil;
import com.samjakob.spigui.item.ItemBuilder;
import me.gleeming.command.Command;
import me.gleeming.command.help.Help;
import me.gleeming.command.paramter.Param;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MineCommand implements Listener {

    @Help(names = {"mines"})
    public void help(CommandSender player) {
        player.sendMessage(ColorUtil.translate("&cUsage: /mines create, reset, list, setup [name] [material]"));
    }

    @Command(names = {"mines create"}, playerOnly = true, permission = "core.admin")
    public void create(Player player) {
        player.getInventory().addItem(
                new ItemBuilder(Material.STICK)
                .name("\247aMine Creator")
                .lore(
                        "&7Left click to set position",
                        "&7Right click to set position2"
                ).build()
        );
    }

    @Command(names = {"mines list"}, permission = "core.admin")
    public void list(CommandSender sender) {
        for(Mine mine : Box.getInstance().getMineContainer().getMines()) {
            sender.sendMessage("\247b" + mine.getName());
        }
    }

    @Command(names = {"mines setup"}, playerOnly = true, permission = "core.admin")
    public void list(Player p, @Param(name = "name") String name, @Param(name = "material") String material) {
        StatPlayer player = Box.getInstance().getStatContainer().getStatPlayerMap().get(p.getUniqueId());
        if(player.getPos1() != null && player.getPos2() != null) {
            player.getPlayer().sendMessage("\247aCreated mine!");
            try {
                Box.getInstance().getMineContainer().getMines().add(new Mine(name, player.getPos1(), player.getPos2(), Material.valueOf(material)));
                p.sendMessage("\247aCreated mine!");
            } catch (Exception e) {
                p.sendMessage("\247cMaterial invalid");
            }
        } else {
            p.sendMessage("\247cPosition is not set!");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("\247aMine Creator")) {
                StatPlayer player = Box.getInstance().getStatContainer().getStatPlayerMap().get(event.getPlayer().getUniqueId());
                player.setPos1(event.getBlock().getLocation());
                player.getPlayer().sendMessage("\2477Set position 1");
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                sendMessageWhenReady(player);
            }
        } catch (NullPointerException ignored) {
            // ignored
        }
    }

    private void sendMessageWhenReady(StatPlayer player) {
        if(player.getPos1() != null && player.getPos2() != null) {
            player.getPlayer().sendMessage("\247aMine position set! Do /mine setup [name] [MATERIAL]");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            try {
                if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("\247aMine Creator")) {
                    StatPlayer player = Box.getInstance().getStatContainer().getStatPlayerMap().get(event.getPlayer().getUniqueId());
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
}
