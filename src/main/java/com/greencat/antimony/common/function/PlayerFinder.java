package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.common.mixins.MinecraftAccessor;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;

public class PlayerFinder {
    public static int mode = 1;
    public PlayerFinder(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (event.phase == TickEvent.Phase.START) {
            mode = (Integer) getConfigByFunctionName.get("PlayerFinder","mode");
        }
    }
    @SubscribeEvent
    public void RenderESP(RenderLivingEvent.Pre<EntityPlayer> event){
        if(FunctionManager.getStatus("PlayerFinder")){
            if(mode == 0) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    if (event.entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) event.entity;
                        if (player != Minecraft.getMinecraft().thePlayer && ((!((Boolean) getConfigByFunctionName.get("PlayerFinder", "showNpc"))) || (!(player.isInvisible() || Utils.isNPC(player))))) {
                            if(isValid((EntityPlayer) event.entity)) {
                                Utils.OutlinedBoxWithESP(player.getEntityBoundingBox(), new Color(30, 255, 243), false, 3);
                                Utils.renderText("玩家:" + player.getName(), new BlockPos(player.posX, player.posY + 2.15, player.posZ), ((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks);
                            }
                        }
                    }
                }
            } else {
                if (Minecraft.getMinecraft().theWorld != null) {
                    if (event.entity instanceof EntityPlayer) {
                        if(isValid((EntityPlayer) event.entity)) {
                            GlStateManager.disableDepth();
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onRenderPost(RenderLivingEvent.Post<EntityPlayer> event){
        if(FunctionManager.getStatus("PlayerFinder")){
            if(mode == 1){
                if (Minecraft.getMinecraft().theWorld != null) {
                    if (event.entity instanceof EntityPlayer) {
                        if(isValid((EntityPlayer) event.entity)) {
                            GlStateManager.enableDepth();
                        }
                    }
                }
            }
        }
    }
    public boolean isValid(EntityPlayer player){
        if(!player.getName().contains("Goblin") && !player.getName().contains("Ice Walker") && !player.getName().contains("Weakling") && !player.getName().contains("Frozen Steve")){
            return true;
        }
        return false;
    }

}
