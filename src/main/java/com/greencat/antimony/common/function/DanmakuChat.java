package com.greencat.antimony.common.function;

import com.greencat.antimony.core.DanmakuCore;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.type.DanmakuMessage;
import com.greencat.antimony.develop.Console;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DanmakuChat {
    public DanmakuChat() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void ClientChatReceived(ClientChatReceivedEvent event){
        if(FunctionManager.getStatus("DanmakuChat")) {
            if (event.type != 2) {
                String message = event.message.getUnformattedText();
                if (isValid(message)) {
                    Console.addMessage("Successfully add message: " + message);
                    DanmakuCore.addMessageToQueue(new DanmakuMessage(event.message.getFormattedText()));
                    event.setCanceled(true);
                }
            }
        }
    }
    public Boolean isValid(String str){
        return !(str.contains("[Auction]") || str.contains("FOLLOW") || str.contains("visiting Your Island"));
    }
}
