package com.kitx.box.mine;

import com.kitx.box.utils.CountDown;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

@RequiredArgsConstructor
@Getter
public class Mine {
    private final String name;
    private final Location pos1;
    private final Location pos2;
    private final Material block;
    private final CountDown reset;

    public void clean() {

        final int topBlockX = (Math.max(this.pos1.getBlockX(), this.pos2.getBlockX()));
        final int bottomBlockX = (Math.min(this.pos1.getBlockX(), this.pos2.getBlockX()));

        final int topBlockY = (Math.max(this.pos1.getBlockY(), this.pos2.getBlockY()));
        final int bottomBlockY = (Math.min(this.pos1.getBlockY(), this.pos2.getBlockY()));

        final int topBlockZ = (Math.max(this.pos1.getBlockZ(), this.pos2.getBlockZ()));
        final int bottomBlockZ = (Math.min(this.pos1.getBlockZ(), this.pos2.getBlockZ()));

        final World world = this.pos1.getWorld();

        for (int x = bottomBlockX; x <= topBlockX; x++) {

            for (int z = bottomBlockZ; z <= topBlockZ; z++) {

                for (int y = bottomBlockY; y <= topBlockY; y++) {

                    assert world != null;
                    Block block = world.getBlockAt(x, y, z);
                    if(block.getType() == this.block) continue;
                    block.setType(this.block);
                }
            }
        }
    }
}
