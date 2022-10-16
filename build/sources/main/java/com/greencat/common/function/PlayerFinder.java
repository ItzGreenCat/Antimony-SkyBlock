package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.mixins.MinecraftAccessor;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
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
    public PlayerFinder(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderESP(RenderLivingEvent.Pre<EntityPlayer> event){
        if(FunctionManager.getStatus("PlayerFinder")){
            if(Minecraft.getMinecraft().theWorld != null) {
                if(event.entity instanceof EntityPlayer){
                        EntityPlayer player = (EntityPlayer) event.entity;
                        if (player != Minecraft.getMinecraft().thePlayer) {
                            Utils.OutlinedBoxWithESP(player.getEntityBoundingBox(), new Color(30, 255, 243), false, 3);
                            Utils.renderText("玩家:" + player.getName(), new BlockPos(player.posX, player.posY + 2.15, player.posZ), ((MinecraftAccessor)Minecraft.getMinecraft()).getTimer().renderPartialTicks);
                        }
                }
            }
        }
    }

}
