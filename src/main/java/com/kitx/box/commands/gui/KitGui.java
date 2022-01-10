package com.kitx.box.commands.gui;

import com.kitx.box.Box;
import com.kitx.box.stats.PlayerData;
import com.kitx.box.utils.ColorUtil;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.stream.IntStream;

public class KitGui {
    private final int[] TILES_TO_UPDATE = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26
    };

    public KitGui(PlayerData data) {
        SGMenu kitMenu  = Box.getInstance().getSpiGUI().create("&0Kit selection", 3);

        IntStream.range(0, TILES_TO_UPDATE.length).map(i -> TILES_TO_UPDATE.length - i + -1).forEach(
                index -> kitMenu.setButton(TILES_TO_UPDATE[index], new SGButton(
                        new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                                .name("")
                                .build()
                ))
        );

        kitMenu.setAutomaticPaginationEnabled(false);

        SGButton basic = new SGButton(
                new ItemBuilder(Material.LEATHER_HELMET)
                        .name("&cBasic Kit")
                        .lore(
                                "&7Free for everyone",
                                "&8&m       ",
                                "&aKit contents:",
                                " &8> &fLeather armor set",
                                " &8> &f1x Stone pickaxe",
                                " &8> &f1x Stone sword",
                                " &8> &f1x Stone axe",
                                " &8> &f16x Steak",
                                data.getBasicKitCD().getSeconds() > 0 ? "&cPlease wait: " + data.getBasicKitCD().convertTime() : "&cCooldown 1 hour"


                        )
                        .build()
        ).withListener(event -> {
            if(!data.getBasicKitCD().isFinished(3600)) {
                data.getPlayer().sendMessage(ColorUtil.getHex("&cThat kit is on cooldown for: " + data.getBasicKitCD().convertTime()));
            } else {
                final Player player = data.getPlayer();
                player.getInventory().addItem(new ItemBuilder(Material.LEATHER_HELMET).build());
                player.getInventory().addItem(new ItemBuilder(Material.LEATHER_CHESTPLATE).build());
                player.getInventory().addItem(new ItemBuilder(Material.LEATHER_LEGGINGS).build());
                player.getInventory().addItem(new ItemBuilder(Material.LEATHER_BOOTS).build());

                player.getInventory().addItem(new ItemBuilder(Material.STONE_SWORD).build());
                player.getInventory().addItem(new ItemBuilder(Material.STONE_PICKAXE).build());
                player.getInventory().addItem(new ItemBuilder(Material.STONE_AXE).build());
                player.getInventory().addItem(new ItemBuilder(Material.COOKED_BEEF).amount(16).build());
                player.sendMessage(ColorUtil.getHex("&aReceived basic kit!"));
            }

        });


        SGButton newbieProtection = new SGButton(
                new ItemBuilder(Material.DIAMOND)
                        .name("&5Newbie protection!")
                        .lore(
                                "&8&m       ",
                                "&fGetting killed too much?",
                                "&fWant to mine in peace?",
                                "",
                                "&aGet 10 minutes of newbie protection.",
                                "",
                                "&8> Click to purchase"
                        )
                        .build()
        ).withListener(event -> {
            if(data.getKills() < 3) {
                data.getPlayer().sendMessage(ChatColor.GREEN + "You now have newbie protection for 10 minutes.");
                data.getNewbieProtection().resetTime();
            }  else {
                data.getPlayer().sendMessage("\247cYou have killed too many people to receive newbie protection!");
            }
        });

        // Coming soon
        kitMenu.setButton(12, basic);
        kitMenu.setButton(14, newbieProtection);

        data.getPlayer().openInventory(kitMenu.getInventory());
    }
}
