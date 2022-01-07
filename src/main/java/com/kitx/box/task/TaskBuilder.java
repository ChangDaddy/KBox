package com.kitx.box.task;

import org.atteo.classindex.ClassIndex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class TaskBuilder {

    public TaskBuilder() {
        List<Class<?>> tasks = new ArrayList<>();

        ClassIndex.getSubclasses(Task.class, Task.class.getClassLoader())
                        .forEach(clazz -> {
                            if(Modifier.isAbstract(clazz.getModifiers()))
                                return;
                            tasks.add(clazz);
                        });

        tasks.forEach(aClass -> {
            try {
                aClass.getMethod("init").invoke(aClass.newInstance());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                System.out.println("Missing init function");
                e.printStackTrace();
            }
        });
    }
}
