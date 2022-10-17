package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HollowAutoPurchase {
    public HollowAutoPurchase() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void AutoPurchase(ClientChatReceivedEvent event){
        if(EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("purchase")) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/purchasecrystallhollowspass");
        }
    }
}
