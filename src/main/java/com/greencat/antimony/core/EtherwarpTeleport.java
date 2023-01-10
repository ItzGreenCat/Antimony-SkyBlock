package com.greencat.antimony.core;

import com.greencat.antimony.core.config.EtherwarpWaypoints;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.type.Rotation;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EtherwarpTeleport {
    public static long latest;
    public static List<BlockPos> position = new ArrayList<>();
    public static void add(BlockPos pos){
        position.add(pos);
        EtherwarpWaypoints.save();
    }
    public static void clear(){
        position.clear();
    }
    public static void undo(){
        try {
            position.remove(position.size() - 1);
            EtherwarpWaypoints.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static long current = 0;
    public static boolean restore = false;
    public static boolean rotation = false;
    public static boolean start = false;
    public static BlockPos special = null;
    public static void next(){
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null){
            return;
        }
        if(!position.isEmpty() && !rotation) {
            BlockPos pos = position.get(0);
            if (pos != null) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                checkSwitch("Aspect of the");
                rotation = true;
                start = true;
                current = System.currentTimeMillis();
            } else if(special != null){
                checkSwitch("Aspect of the");
                rotation = true;
                start = true;
                current = System.currentTimeMillis();
            }
        }
    }
    public static void checkSwitch(String name){
        try {
            if (System.currentTimeMillis() - latest >= 0) {
                latest = System.currentTimeMillis();
                ItemStack hand = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (hand == null || !StringUtils.stripControlCodes(hand.getDisplayName().toLowerCase()).contains(name.toLowerCase())) {
                    for (int i = 0; i < 8; ++i) {
                        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                        if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains(name.toLowerCase())) {
                            Minecraft.getMinecraft().thePlayer.inventory.currentItem = i;
                            break;
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    @SubscribeEvent
    public void onMotion(CustomEventHandler.MotionChangeEvent event){
        try {
            if (!position.isEmpty()) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                BlockPos pos;
                if (special == null) {
                    pos = position.get(0);
                } else {
                    pos = special;
                }
                if (rotation) {
                    if (((float) pos.getY()) >= (player.getPosition().getY() + player.getEyeHeight())) {
                        Rotation rotation = Utils.getRotation(pos);
                        event.yaw = rotation.getYaw();
                        event.pitch = rotation.getPitch();
                    } else {
                        float[] rotation = Utils.getRotation(pos, EnumFacing.UP);
                        event.yaw = rotation[0];
                        event.pitch = rotation[1];
                    }
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                }
                if (start && System.currentTimeMillis() - current > 300) {
                    if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
                        Minecraft.getMinecraft().playerController.sendUseItem(player, Minecraft.getMinecraft().theWorld, player.getHeldItem());
                        start = false;
                        restore = true;
                        current = System.currentTimeMillis();
                    }
                }
                if (restore && System.currentTimeMillis() - current > 100) {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
                    if (special == null) {
                        position.add(pos);
                        position.remove(0);
                    }
                    if (special != null) {
                        special = null;
                    }
                    restore = false;
                    rotation = false;
                }
            }
        } catch(Exception ignored){

        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if(!position.isEmpty()) {
            List<Vec3> vec3List = new ArrayList<Vec3>();
            Utils.renderText("当前位置",position.get(0),event.partialTicks);
            for(BlockPos pos : position){
                vec3List.add(new Vec3(pos.getX(), pos.getY(), pos.getZ()));
                Utils.OutlinedBoxWithESP(pos, Chroma.color,false,2.5F);
            }
            Utils.drawLines(vec3List,2.5F,event.partialTicks);
        }
    }
}
