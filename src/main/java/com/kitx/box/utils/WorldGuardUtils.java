package com.kitx.box.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

@UtilityClass
public class WorldGuardUtils {
    /*
    Please don't change your api again world guard :)
     */
    public boolean isAFK(Player player) {
        RegionManager rm  = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));

        //Get a list of all regions at a given location
        assert rm != null;
        ApplicableRegionSet set = rm.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));

        for(ProtectedRegion region : set) {
            if(region.getId().equalsIgnoreCase("insideafk")) {
                return true;
            }
        }

        return false;
    }

}