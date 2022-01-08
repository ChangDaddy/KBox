package com.kitx.box.commands;

import com.kitx.box.Box;
import com.kitx.box.mine.Mine;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.utils.ColorUtil;
import com.kitx.box.utils.CountDown;
import com.samjakob.spigui.item.ItemBuilder;
import me.gleeming.command.Command;
import me.gleeming.command.help.Help;
import me.gleeming.command.paramter.Param;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MineCommand {

    @Help(names = {"mines"})
    public void help(CommandSender player) {
        player.sendMessage(ColorUtil.translate("&cUsage: /mines create, reset, list, setup [name] [material] [reset]"));
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

    @Command(names = {"mines reset"}, playerOnly = true, permission = "core.admin")
    public void reset(Player player) {
        Box.getInstance().getMineContainer().getMines().forEach(Mine::clean);
    }

    @Command(names = {"eat"}, permission = "command.eat", playerOnly = true)
    public void eatCommand(Player player, @Param(name = "target", required = false) Player target) {
        if(target != null) {
            target.setFoodLevel(20);
        } else {
            player.setFoodLevel(20);
        }
    }

    @Command(names = {"mines list"}, permission = "core.admin")
    public void list(CommandSender sender) {
        for (Mine mine : Box.getInstance().getMineContainer().getMines()) {
            sender.sendMessage("\247b" + mine.getName());
        }
    }

    @Command(names = {"mines setup"}, playerOnly = true, permission = "core.admin")
    public void setup(Player p, @Param(name = "name") String name, @Param(name = "material") Material material, @Param(name = "reset") Integer reset) {
        PlayerData player = Box.getInstance().getStatContainer().getStatPlayerMap().get(p.getUniqueId());
        if (player.getPos1() != null && player.getPos2() != null) {
            if (material == null) {
                p.sendMessage("\247cMaterial invalid");
                return;
            }
            Box.getInstance().getMineContainer().getMines().add(new Mine(name, player.getPos1(), player.getPos2(), material, new CountDown(reset)));
            p.sendMessage("\247aCreated mine!");
            player.setPos1(null);
            player.setPos2(null);
        } else {
            p.sendMessage("\247cPosition is not set!");
        }
    }
}
