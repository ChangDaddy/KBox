package com.kitx.box.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CountDown implements Serializable {
    private int seconds;
    private int maxSeconds;
    public CountDown(int seconds, boolean reset) {
        this.seconds = seconds;
        this.maxSeconds = seconds;
        if(reset) this.seconds = 0;
    }

    public CountDown(int seconds) {
        this.seconds = seconds;
        this.maxSeconds = seconds;
    }

    public void countDown() {
        seconds = Math.max(0, seconds - 1);
    }

    public boolean isFinished() {
        return seconds == 0;
    }

    /**
     * @return the converted seconds to minutes and seconds
     */
    public String convertTime() {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    public void resetTime() {
        seconds = this.maxSeconds;
    }
}