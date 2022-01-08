package com.kitx.box.task;

import com.google.common.collect.ImmutableSet;
import com.kitx.box.Box;
import org.atteo.classindex.ClassIndex;
import org.reflections.util.ClasspathHelper;
import org.reflections.vfs.Vfs;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

public class TaskBuilder {

    public TaskBuilder() {
        Set<Class<?>> tasks = new HashSet<>();
        Collection<URL> urls = ClasspathHelper.forClassLoader(ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader(), Box.getInstance().getClass().getClassLoader());

        if(urls.size() > 0) {
            urls.forEach(url -> Vfs.fromURL(url).getFiles().forEach(file -> {
                String name = file.getRelativePath().replace("/", ".").replace(".class", "");
                try { if (name.startsWith("com.kitx.box.task.impl")) tasks.add(Class.forName(name)); } catch(ClassNotFoundException ex) { ex.printStackTrace(); }
            }));
        }

        ImmutableSet.copyOf(tasks).forEach(clazz -> {
            try {
                clazz.getMethod("init").invoke(clazz.newInstance());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                Box.getInstance().getLogger().warning("Missing init function for: " + clazz.getSimpleName());
            }
        });
    }
}
