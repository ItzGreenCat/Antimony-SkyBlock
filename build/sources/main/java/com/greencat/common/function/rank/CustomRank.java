package com.greencat.common.function.rank;

import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class CustomRank {
    public CustomRank() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    /*@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void modifyArmorStandName(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (Minecraft.getMinecraft().theWorld != null) {
                Entity entity = event.entity;
                    if (entity.hasCustomName()) {
                        String nameWithRank = replaceName(entity.getCustomNameTag());
                        entity.setCustomNameTag(nameWithRank);
                    }
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
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
