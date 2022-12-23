package com.greencat.antimony.utils;

import com.greencat.antimony.core.type.Rotation;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SmoothRotation {
    private static float pitchDifference;
    public static float yawDifference;
    private static int ticks = -1;
    private static int tickCounter = 0;
    private static Runnable callback = null;

    public static boolean running = false;
    public SmoothRotation() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static void smoothLook(Rotation rotation, int ticks, Runnable callback) {
        if(ticks == 0) {
            look(rotation);
            callback.run();
            return;
        }

        SmoothRotation.callback = callback;

        pitchDifference = wrapAngleTo180(rotation.getPitch() - Minecraft.getMinecraft().thePlayer.rotationPitch);
        yawDifference = wrapAngleTo180(rotation.getYaw() - Minecraft.getMinecraft().thePlayer.rotationYaw);

        SmoothRotation.ticks = ticks * 20;
        SmoothRotation.tickCounter = 0;
    }
    public static void look(Rotation rotation) {
        Minecraft.getMinecraft().thePlayer.rotationPitch = rotation.getPitch();
        Minecraft.getMinecraft().thePlayer.rotationYaw = rotation.getYaw();
    }
    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(Minecraft.getMinecraft().thePlayer == null) return;
        if(tickCounter < ticks) {
            running = true;
            Minecraft.getMinecraft().thePlayer.rotationPitch += pitchDifference / ticks;
            Minecraft.getMinecraft().thePlayer.rotationYaw += yawDifference / ticks;
            tickCounter++;
        } else if(callback != null) {
            running = false;
            callback.run();
            callback = null;
        }
    }
    private static double wrapAngleTo180(double angle) {
        return angle - Math.floor(angle / 360 + 0.5) * 360;
    }

    private static float wrapAngleTo180(float angle) {
        return (float) (angle - Math.floor(angle / 360 + 0.5) * 360);
    }
}
