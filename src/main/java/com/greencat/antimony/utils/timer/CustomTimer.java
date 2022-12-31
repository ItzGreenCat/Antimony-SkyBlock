package com.greencat.antimony.utils.timer;

public class CustomTimer {
    public long time = -1;
    public long record = -1;
    public void save(){
        time = System.currentTimeMillis();
    }
    public void setRecord(long record){
        this.record = record;
    }
    public boolean isFinished(){
        return time + record < System.currentTimeMillis();
    }
}
