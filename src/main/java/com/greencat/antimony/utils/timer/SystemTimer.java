package com.greencat.antimony.utils.timer;

public class SystemTimer {
    //idk how its work
    //just copy these code from github LOL
    private long time = -1L;

    public Boolean hasTimePassed(Long s){
        return System.currentTimeMillis() >= time + s;
    }

    public Long hasTimeLeft(long s){
        return s+ time - System.currentTimeMillis();
    }

    public Long timePassed() {
        return System.currentTimeMillis() - time;
    }

    public void reset() {
        time = System.currentTimeMillis();
    }
    private long lastMS = 0L;

    public SystemTimer() {
        this.reset1();
    }

    public int convertToMS(int d) {
        return 1000 / d;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(long milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }

    public boolean hasTimeReached(long delay) {
        return System.currentTimeMillis() - this.lastMS >= delay;
    }

    public long getDelay() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public void reset1() {
        this.lastMS = this.getCurrentMS();
    }

    public void setLastMS() {
        this.lastMS = System.currentTimeMillis();
    }

    public void setLastMS(long lastMS) {
        this.lastMS = lastMS;
    }
}
