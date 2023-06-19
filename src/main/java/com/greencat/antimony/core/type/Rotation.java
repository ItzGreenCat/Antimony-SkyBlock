package com.greencat.antimony.core.type;

import com.greencat.antimony.utils.RotationUtils;

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
    public void fixedSensitivity(float sensitivity) {
        float f = sensitivity * 0.6F + 0.2F;
        float gcd = f * f * f * 1.2F;

        Rotation rotation = RotationUtils.serverRotation;

        float deltaYaw = yaw - rotation.yaw;
        deltaYaw -= deltaYaw % gcd;
        yaw = rotation.yaw + deltaYaw;

        float deltaPitch = pitch - rotation.pitch;
        deltaPitch -= deltaPitch % gcd;
        pitch = rotation.pitch + deltaPitch;
    }
}
