package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.type.Rotation;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.annotation.EventModule;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class BlockTarget implements ReflectionlessEventHandler {
    BlockPos target = null;
    boolean isEnable = false;
    public BlockTarget(){
        CustomEventHandler.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }
    @EventModule
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals("BlockTarget")){
            init(event);
        }
    }
    @EventModule
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event){
        if(event.function.getName().equals("BlockTarget")){
            post();
        }
    }
    @EventModule
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("BlockTarget")){
            if(event.status){
                init(event);
            } else {
                post();
            }
        }
    }
    private void init(Event event){
        if(Minecraft.getMinecraft().theWorld != null) {
            MovingObjectPosition position = Minecraft.getMinecraft().thePlayer.rayTrace(100.0D, 0);
            if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                target = position.getBlockPos();
            } else {
                event.setCanceled(true);
                Utils.print("BlockTarget无法取到方块");
            }
        }
    }
    private void post(){
        target = null;
    }

    public void onMotion(CustomEventHandler.MotionChangeEvent event){
        if(Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("BlockTarget") && target != null) {
                isEnable = true;
                Rotation rotation = Utils.getRotation(target);
                Minecraft.getMinecraft().thePlayer.rotationYaw = rotation.getYaw();
                Minecraft.getMinecraft().thePlayer.rotationPitch = rotation.getPitch();
            } else {
                isEnable = false;
            }
        }
    }
    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event){
        if(Minecraft.getMinecraft().theWorld != null) {
            if (isEnable && target != null) {
                Utils.OutlinedBoxWithESP(target, Color.orange, false, 2.5F);
            }
        }
    }
    @SubscribeEvent
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if (isEnable) {
            Utils.print("检测到世界服务器改变,自动关闭BlockTarget");
            FunctionManager.setStatus("BlockTarget", false);
        }
    }

    @Override
    public void invoke(Event event) {
        if(event instanceof CustomEventHandler.MotionChangeEvent){
            onMotion((CustomEventHandler.MotionChangeEvent) event);
        }
    }
}
