package com.greencat.antimony.common.decorate;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class Events {
    public Events() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void Render(RenderLivingEvent.Pre<EntityLivingBase> event){
        if(event.entity instanceof EntityPlayer) {
            try {
                for(Map.Entry<String,String> entry : Antimony.GroundDecorateList.entrySet()) {
                    if(entry.getKey().equalsIgnoreCase(EnumChatFormatting.getTextWithoutFormattingCodes(event.entity.getName()))){
                        GroundDecorate.draw(event.entity.posX, event.entity.posY, event.entity.posZ,new ResourceLocation(Antimony.MODID,"ground/" + entry.getValue() + ".png"));
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    /*@SubscribeEvent
    public void RenderSelfDecorate(RenderWorldLastEvent event){
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0){
            try {
                for (Map.Entry<String, String> entry : Antimony.GroundDecorateList.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().thePlayer.getName()))) {
                        GroundDecorate.draw(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ,new ResourceLocation(Antimony.MODID,"ground/" + entry.getValue() + ".png"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}
