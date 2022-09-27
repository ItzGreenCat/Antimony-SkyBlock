package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class PlayerFinder {
    public PlayerFinder(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderESP(RenderLivingEvent.Pre<EntityLivingBase> event){
        if(FunctionManager.getStatus("PlayerFinder")){
            if(Minecraft.getMinecraft().theWorld != null) {
                if (event.entity instanceof EntityPlayer && event.entity != Minecraft.getMinecraft().thePlayer) {
                    EntityPlayer player = (EntityPlayer) event.entity;
                    Utils.OutlinedBoxWithESP(player.getEntityBoundingBox(), new Color(30, 255, 243), false, 3);
                    Utils.renderText("玩家:" + player.getName(), player.posX, player.posY + 1, player.posZ);
                }
            }
        }
    }

}
