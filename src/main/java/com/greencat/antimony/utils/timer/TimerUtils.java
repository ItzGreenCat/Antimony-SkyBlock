package com.greencat.antimony.utils.timer;

public class TimerUtils extends SystemTimer2{
    public TimerUtils(){
        this.lastMS = System.currentTimeMillis();
    }
    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }
    public boolean hasReached(double delay) {
        return System.currentTimeMillis() - this.lastMS >= delay;
    }

    public boolean hasReached(long delay) {
        return System.currentTimeMillis() - this.lastMS >= delay;
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }
}
