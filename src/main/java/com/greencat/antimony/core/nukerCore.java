package com.greencat.antimony.core;

import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.common.mixins.EntityPlayerSPAccessor;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.annotation.EventModule;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;

public class nukerCore {
    public nukerCore(){
        CustomEventHandler.EVENT_BUS.register(this);
    }
    public Vec3 pos;
    public static int extraBPS = 0;
    public static int skipBPS = 0;
    public static int bpsLimit = 0;
    public static boolean noRotate = false;
    public static boolean noDelay = false;
    public void nuke(Vec3 posVector, Object clazz, Method finder, Object... args) {
        if (posVector != null) {
            pos = posVector;
            BlockPos block = new BlockPos(posVector);
            if(posVector.distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) <= ((Double) ConfigInterface.get("Nuker","core1radius")) || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() != Blocks.air) {
                if (!Minecraft.getMinecraft().thePlayer.isUsingItem()) {
                    if (Minecraft.getMinecraft().currentScreen == null) {
                        if(bpsLimit != 0){
                            if(skipBPS != bpsLimit){
                                skipBPS = skipBPS + 1;
                                return;
                            } else {
                                skipBPS = 0;
                            }
                        }
                        if(extraBPS > 0){
                            for(int i = 0;i <= extraBPS;i ++){
                                try {
                                    finder.setAccessible(true);
                                    Object obj = finder.invoke(clazz, args);
                                    if (obj != null){
                                        if (obj instanceof Vec3) {
                                            Vec3 vec = (Vec3) obj;
                                            BlockPos extraBlock = new BlockPos(vec);
                                            if (vec.distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) <= ((Double) ConfigInterface.get("Nuker","core1radius")) || Minecraft.getMinecraft().theWorld.getBlockState(extraBlock).getBlock() != Blocks.air) {
                                                nuke(vec, extraBlock);
                                            }
                                        }
                                        if (obj instanceof BlockPos) {
                                            Vec3 vec = new Vec3((BlockPos) obj);
                                            BlockPos extraBlock = new BlockPos(vec);
                                            if (vec.distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) <= ((Double) ConfigInterface.get("Nuker","core1radius")) || Minecraft.getMinecraft().theWorld.getBlockState(extraBlock).getBlock() != Blocks.air) {
                                                nuke(vec, extraBlock);
                                            }
                                        }
                                    }
                                } catch(Exception ignored){

                                }
                            }
                        } else {
                            nuke(pos,block);
                        }
                        Minecraft.getMinecraft().thePlayer.swingItem();
                    } else {
                        Minecraft.getMinecraft().playerController.resetBlockRemoving();
                    }
                }
            }
        }
    }
    private void nuke(Vec3 posVector,BlockPos block){
        EnumFacing facing = Utils.calculateEnumfacingLook(posVector);
        if (Minecraft.getMinecraft().playerController.onPlayerDamageBlock(block, facing)) {
            Minecraft.getMinecraft().effectRenderer.addBlockHitEffects(block, facing);
        }
        if(extraBPS > 0 || noDelay){
            Minecraft.getMinecraft().theWorld.setBlockToAir(block);
        }
    }
    @EventModule
    public void onMovePre(CustomEventHandler.MotionChangeEvent.Pre event){
        if(pos != null && !noRotate){
            /*Rotation rotation = Utils.getRotation(pos);
            Minecraft.getMinecraft().thePlayer.rotationYaw = rotation.getYaw();
            Minecraft.getMinecraft().thePlayer.rotationPitch = rotation.getYaw();*/
            float[] angles = Utils.getServerAngles(pos);
            event.yaw = ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - MathHelper.wrapAngleTo180_float((float) Math.max(-180, Math.min((double) (((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - angles[0]), 180)));
            event.pitch = ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - MathHelper.wrapAngleTo180_float((float) Math.max(-90, Math.min((double) (((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - angles[1]), 90)));
        }

    }
}
