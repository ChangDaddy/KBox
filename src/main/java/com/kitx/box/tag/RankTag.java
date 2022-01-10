package com.kitx.box.tag;

import com.kitx.box.mine.Mine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.Base64;

@RequiredArgsConstructor
@Getter
public class RankTag implements Serializable {
    private final String material;
    private final String name;
    private final String command;

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

    public static RankTag deserialize(String encodedObject) {
        try {
            byte[] serializedObject = Base64.getDecoder().decode(encodedObject);

            ByteArrayInputStream in = new ByteArrayInputStream(serializedObject);
            ObjectInputStream is = new ObjectInputStream(in);

            return (RankTag) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
