package com.greencat.antimony.core.type;

import com.greencat.antimony.develop.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class DanmakuMessage {
    public int x = 0;
    public int y = 0;
    public String message;
    public int track = 0;
    public int endX = 0;
    public DanmakuMessage(String msg){
        message = msg;
        endX = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() + Minecraft.getMinecraft().fontRendererObj.getStringWidth(message);
    }
    public void draw(){
        endX = x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(message);
        Console.addMessage("drawing danmaku message at X: " + x + " Y: " + y + " track: " + track + " context: " + message);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(message,x,y,16777215);
    }
    public void refreshEndX(){
        endX = x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(message);
    }
}
