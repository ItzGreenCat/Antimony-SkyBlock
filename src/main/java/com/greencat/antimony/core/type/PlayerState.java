package com.greencat.antimony.core.type;

public class PlayerState {
    public double x;
    public double y;
    public double z;

    public float yaw;
    public float pitch;

    boolean sneak;
    boolean sprint;
    public PlayerState(double x,double y,double z,float yaw,float pitch,boolean sneak,boolean sprint){
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.sneak = sneak;
        this.sprint = sprint;
    }
}
