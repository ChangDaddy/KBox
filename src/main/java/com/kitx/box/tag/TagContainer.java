package com.kitx.box.tag;


import com.kitx.box.Box;
import com.kitx.box.mine.Mine;
import com.kitx.box.utils.ColorUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TagContainer implements Listener {
    private final List<RankTag> rankTagList = new ArrayList<>();


    public TagContainer() {
        loadTags();
    }

    public void createTag(String material, String name, String command) {
        rankTagList.add(new RankTag(material, name, command));
    }


    public void loadTags() {
        try {
            File file = new File(Box.getInstance().getDataFolder(), "tags.yml");
            if (!file.exists()) file.createNewFile();

            YamlConfiguration load = YamlConfiguration.loadConfiguration(file);
            for (String keys : load.getKeys(false)) {
                rankTagList.add(RankTag.deserialize(load.getString(keys)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTags() {
        try {
            File file = new File(Box.getInstance().getDataFolder(), "tags.yml");
            if (!file.exists()) file.createNewFile();

            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

            for (RankTag tag : rankTagList) {
                yml.set(tag.getName(), tag.serialize());
            }

            yml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            try {
                final String name = event.getItem().getItemMeta().getDisplayName().replaceAll("\247", "&");
                for (RankTag rankTag : rankTagList) {
                    if (rankTag.getName().equals(name) && event.getItem().getType() == Material.NAME_TAG && event.getItem().getEnchantmentLevel(Enchantment.DURABILITY) > 9) {
                        LuckPerms api = LuckPermsProvider.get();
                        User user = api.getPlayerAdapter(Player.class).getUser(event.getPlayer());
                        if(!user.getPrimaryGroup().contains(rankTag.getCommand())) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rankTag.getCommand().replaceAll("%player%", event.getPlayer().getName()));
                            ItemStack itemStack = new ItemStack(event.getItem());
                            itemStack.setAmount(1);
                            event.getPlayer().getInventory().removeItem(new ItemStack(itemStack));
                            event.getPlayer().sendMessage(ColorUtil.getHex(ChatColor.GREEN + "You received " + rankTag.getName()));
                        } else {
                            event.getPlayer().sendMessage(ChatColor.RED + "You already have that rank!");
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
    }
}
