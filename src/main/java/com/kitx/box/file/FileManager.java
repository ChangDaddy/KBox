package com.kitx.box.file;

import com.kitx.box.Box;
import com.kitx.box.utils.FileUtil;

import java.io.File;

public class FileManager {
    private final Box plugin = Box.getInstance();
    private final File dataFolder = plugin.getDataFolder();

    public FileManager() {
        if(!dataFolder.exists())
            dataFolder.mkdir();

        loadFile("classindex", "https://repo1.maven.org/maven2/org/atteo/classindex/classindex/3.9/classindex-3.9.jar");
        loadFile("fastutil", "https://repo1.maven.org/maven2/it/unimi/dsi/fastutil/8.1.0/fastutil-8.1.0.jar");
    }

    public void loadFile(String fileName, String fileUrl) {
        try {
            File lib = new File(dataFolder, String.format("lib/%s.jar", fileName));

            if(!lib.exists()) {
                if(!lib.getParentFile().exists())
                    lib.getParentFile().mkdir();

                plugin.getLogger().info(String.format("Downloading %s...", fileName));
                FileUtil.download(lib, fileUrl);
                plugin.getLogger().info(String.format("Finished downloading %s", fileName));
            }

            FileUtil.injectUrl(lib.toURI().toURL());
        } catch (Exception e) {
            plugin.getLogger().warning(String.format("Failed to download or load %s: %s", fileName, e.getMessage()));
        }
    }
}
