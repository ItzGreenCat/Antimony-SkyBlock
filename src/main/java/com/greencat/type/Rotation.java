package com.greencat.type;

public class Rotation {
    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void addYaw(float yaw) {
        this.yaw += yaw;
    }

    public void addPitch(float pitch) {
        this.pitch += pitch;
    }

    public float getValue() {
        return Math.abs(this.yaw) + Math.abs(this.pitch);
    }

    public String toString() {
        return "Rotation{yaw=" + this.yaw + ", pitch=" + this.pitch + '}';
    }
}
