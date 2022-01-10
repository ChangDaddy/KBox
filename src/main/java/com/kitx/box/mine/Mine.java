package com.kitx.box.mine;

import com.kitx.box.utils.CountDown;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.naming.Context;
import java.io.*;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Mine implements Serializable {
    private final String name;
    private final List<MappedBlock> blocks;
    private final CountDown reset;
    private final boolean broadcast;

    public void clean() {
        for(MappedBlock mappedBlock : blocks) {
            World world = Bukkit.getWorld(mappedBlock.getLocation().getWorld());
            assert world != null;
            Block block = world.getBlockAt(mappedBlock.getLocation().getX(), mappedBlock.getLocation().getY(), mappedBlock.getLocation().getZ());
            if(block.getType().name().equals(mappedBlock.getMaterial())) continue;
            block.setType(Material.valueOf(mappedBlock.getMaterial()));
        }
    }
    public String serialize() {
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(io);
            os.writeObject(this);
            os.flush();
            byte[] serializedObject = io.toByteArray();
            return Base64.getEncoder().encodeToString(serializedObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Mine deserialize(String encodedObject) {
        try {
            byte[] serializedObject = Base64.getDecoder().decode(encodedObject);

            ByteArrayInputStream in = new ByteArrayInputStream(serializedObject);
            ObjectInputStream is = new ObjectInputStream(in);

            return (Mine) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
