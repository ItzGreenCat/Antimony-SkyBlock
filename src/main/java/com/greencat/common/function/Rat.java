package com.greencat.common.function;

import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Rat {
    Utils utils = new Utils();
    String Rat = "之前有人给我发了shady的github链接，偶然看到个这个,就拿过来了lol,顺便拿这个做了个彩蛋功能";
    public Rat() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static String a = "                  ..----.._    _              ";
    public static String b = "                .' .--.    '-.(O)_            ";
    public static String c = "    '-.__.-'''=:|  ,  _)_ |__ . c\'-..        ";
    public static String d = "                 ''------'---''---'-'         ";
    @SubscribeEvent
    public void showRat(ClientChatReceivedEvent event) {
        if(EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("You are playing on profile")){
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "You have been Rat"));
        }
    }

}
