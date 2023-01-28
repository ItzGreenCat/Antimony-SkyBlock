package com.greencat.antimony.common.decorate;

import com.greencat.Antimony;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events {
    public Events() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void Render(RenderLivingEvent.Pre<EntityLivingBase> event){
        if(event.entity instanceof EntityPlayer) {
            if(Antimony.GroundDecorateList.containsKey(EnumChatFormatting.getTextWithoutFormattingCodes(event.entity.getName()))){
                GroundDecorate.draw(event.entity.posX, event.entity.posY, event.entity.posZ,new ResourceLocation(Antimony.MODID,"ground/" + Antimony.GroundDecorateList.get(EnumChatFormatting.getTextWithoutFormattingCodes(event.entity.getName())) + ".png"));
            }
        }
    }
}