package com.GreenCat.common.function.rank;

import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.common.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

public class CustomRank {
    public CustomRank() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void modifyArmorStandName(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (Minecraft.func_71410_x().field_71441_e != null) {
                Entity entity = event.entity;
                    if (entity.func_145818_k_()) {
                        String nameWithRank = replaceName(entity.func_95999_t());
                        entity.func_96094_a(nameWithRank);
                    }
        }
    }
    /*@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void modifyChatMessage(ClientChatReceivedEvent event){
        for(String key : RankList.rankMap.keySet()) {
            if (event.message.getFormattedText().contains(key)) {
                event.message = new ChatComponentText(replaceName(event.message.getFormattedText()));
            }
        }
    }*/
    private String replaceName(String OriginalName){
        String newName = OriginalName;
        for(Map.Entry<String,String> entry : RankList.rankMap.entrySet()){
            newName = newName.replace(entry.getKey(),entry.getValue());
        }
        return newName;
    }
}
