package com.greencat.antimony.common.function;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import scala.util.Random;

public class FakeBan {
    public static void active(String reason){
        ChatComponentText component = new ChatComponentText("\u00a7cYou are permanently banned from this server!");
        component.appendText("\n");
        component.appendText("\n\u00a77Reason: \u00a7r" + reason);
        component.appendText("\n\u00a77Find out more: \u00a7b\u00a7nhttps://www.hypixel.net/appeal");
        component.appendText("\n");
        Random random = new Random();
        component.appendText("\n\u00a77Ban ID: \u00a7r#" + (random.nextInt(89999999) + 10000000));
        component.appendText("\n\u00a77Sharing your Ban ID may affect the processing of your appeal!");
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().closeChannel(component);
    }
}
