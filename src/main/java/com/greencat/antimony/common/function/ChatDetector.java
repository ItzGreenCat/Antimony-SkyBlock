package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.greencat.antimony.core.config.ConfigInterface.get;

public class ChatDetector {
    public ChatDetector() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event){
        if(FunctionManager.getStatus("ChatDetector")) {
            if (EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).toLowerCase().contains(((String)get("ChatDetector","trigger")).toLowerCase())) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage((String)get("ChatDetector","message"));
            }
        }
    }
}
