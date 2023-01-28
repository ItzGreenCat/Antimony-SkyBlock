package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.RenderLivingEntityAccessor;
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

import static com.greencat.antimony.core.PlayerNameFilter.isValid;

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
                            if(isValid((EntityPlayer) event.entity,false)) {
                                Utils.OutlinedBoxWithESP(player.getEntityBoundingBox(), new Color(30, 255, 243), false, 3);
                                Utils.renderText("玩家:" + player.getName(), new BlockPos(player.posX, player.posY + 3.15, player.posZ), ((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().renderPartialTicks);
                            }
                        }
                    }
                }
            }
            if(mode == 1) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    if (event.entity instanceof EntityPlayer) {
                        if(isValid((EntityPlayer) event.entity,false)) {
                            GlStateManager.disableDepth();
                        }
                    }
                }
            }
            if(mode == 2) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    if (event.entity instanceof EntityPlayer) {
                        if(isValid((EntityPlayer) event.entity,false)) {
                            ((RenderLivingEntityAccessor)event.renderer).setOutline(true);
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
                        if(isValid((EntityPlayer) event.entity,false)) {
                            GlStateManager.enableDepth();
                        }
                    }
                }
            }
        }
    }


}
