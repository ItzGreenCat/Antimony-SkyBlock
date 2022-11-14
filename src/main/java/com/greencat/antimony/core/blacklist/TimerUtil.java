package com.greencat.antimony.core.blacklist;

import java.util.Date;

public class TimerUtil {
	
    private long lastMS;
    private long ms = this.getCurrentMS();

    //StopWatch
    public final long getElapsedTime() {
        return this.getCurrentMS() - this.ms;
    }

    public final boolean elapsed(long milliseconds) {
        return this.getCurrentMS() - this.ms > milliseconds;
    }

    public final void resetStopWatch() {
        this.ms = this.getCurrentMS();
    }
    
    //Timer
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        if ((double)(this.getCurrentMS() - this.lastMS) >= milliseconds) {
            return true;
        }
        return false;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(float milliSec) {
        if ((float)(this.getTime() - this.lastMS) >= milliSec) {
            return true;
        }
        return false;
    }
    
    public long getLastMS() {
		return lastMS;
	}

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public static long curTime() {
        return new Date().getTime();
    }
}

