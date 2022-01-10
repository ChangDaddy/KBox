package com.kitx.box.utils;

import lombok.Getter;

@Getter
public class Cooldown {
    private long cooldown;
    private long time;

    public boolean isFinished(long seconds) {
        this.cooldown = seconds;
        if (time == 0 || getSeconds() < 1) {
            time = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public String convertTime() {
        return String.format("%02d:%02d", getSeconds() / 60, getSeconds() % 60);
    }

    public long getSeconds() {
        return ((time / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
    }
}