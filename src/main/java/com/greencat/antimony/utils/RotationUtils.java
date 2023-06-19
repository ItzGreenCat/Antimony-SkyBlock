package com.greencat.antimony.utils;

import com.greencat.antimony.common.mixins.C03PacketPlayerAccessor;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.type.Rotation;
import me.greencat.lwebus.core.annotation.EventModule;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class RotationUtils {
    private static final Random random = new Random();

    private static int keepLength;
    private static double x = random.nextDouble();
    private static double y = random.nextDouble();
    private static double z = random.nextDouble();

    public static Rotation serverRotation = new Rotation(0F, 0F);
    public RotationUtils() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    public static Rotation targetRotation;
    public static boolean keepCurrentRotation = false;

    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)
                ));
    }
    public static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if(targetRotation != null) {
            keepLength--;

            if (keepLength <= 0)
                reset();
        }

        if(random.nextGaussian() > 0.8D) x = Math.random();
        if(random.nextGaussian() > 0.8D) y = Math.random();
        if(random.nextGaussian() > 0.8D) z = Math.random();
    }
    @EventModule
    public void onPacket(final CustomEventHandler.PacketEvent event) {
        final Packet packet = event.packet;

        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;

            if (targetRotation != null && !keepCurrentRotation && (targetRotation.getYaw() != serverRotation.getYaw() || targetRotation.getPitch() != serverRotation.getPitch())) {
                ((C03PacketPlayerAccessor)packetPlayer).setYaw(targetRotation.getYaw());
                ((C03PacketPlayerAccessor)packetPlayer).setPitch(targetRotation.getPitch());
                ((C03PacketPlayerAccessor)packetPlayer).setRotating(true);
            }

            if (packetPlayer.getRotating())
                serverRotation = new Rotation(packetPlayer.getYaw(), packetPlayer.getPitch());
        }
    }
    public static void setTargetRotation(final Rotation rotation, final int keepLength) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch())
                || rotation.getPitch() > 90 || rotation.getPitch() < -90)
            return;

        rotation.fixedSensitivity(Minecraft.getMinecraft().gameSettings.mouseSensitivity);
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
    }

    public static void setTargetRotation(final Rotation rotation) {
        setTargetRotation(rotation, 0);
    }

    public static void reset() {
        keepLength = 0;
        targetRotation = null;
    }

}
