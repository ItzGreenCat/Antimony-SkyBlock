package com.greencat.core;

import com.greencat.common.event.CustomEventHandler;
import com.greencat.common.mixins.EntityPlayerSPAccessor;
import com.greencat.type.Rotation;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class nukerCore {
    public nukerCore(){
        CustomEventHandler.EVENT_BUS.register(this);
    }
    public Vec3 pos;
    public void nuke(Vec3 posVector) {
        if (posVector != null) {
            pos = posVector;
            BlockPos block = new BlockPos(posVector);
            if(posVector.distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) <= 4.5 || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() != Blocks.air) {
                if (!Minecraft.getMinecraft().thePlayer.isUsingItem()) {
                    if (Minecraft.getMinecraft().currentScreen == null) {
                        EnumFacing facing = Utils.calculateEnumfacingLook(posVector);
                        if (Minecraft.getMinecraft().playerController.onPlayerDamageBlock(block, facing)) {
                            Minecraft.getMinecraft().effectRenderer.addBlockHitEffects(block, facing);
                            Minecraft.getMinecraft().thePlayer.swingItem();
                        }
                    } else {
                        Minecraft.getMinecraft().playerController.resetBlockRemoving();
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onMovePre(CustomEventHandler.MotionChangeEvent.Pre event){
        if(pos != null){
            /*Rotation rotation = Utils.getRotation(pos);
            Minecraft.getMinecraft().thePlayer.rotationYaw = rotation.getYaw();
            Minecraft.getMinecraft().thePlayer.rotationPitch = rotation.getYaw();*/
            float[] angles = Utils.getServerAngles(pos);
            event.yaw = ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - MathHelper.wrapAngleTo180_float((float) Math.max(-180, Math.min((double) (((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - angles[0]), 180)));
            event.pitch = ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - MathHelper.wrapAngleTo180_float((float) Math.max(-90, Math.min((double) (((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - angles[1]), 90)));
        }

    }
}
