package com.greencat.type;

import com.greencat.common.ui.NoticeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class Notice {
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    int x = scaledResolution.getScaledWidth() / 2 - (160 / 2);
    int y = -32;
    int time = 0;
    boolean isEnable;
    String message = "";

    public void SetNotice(String notice,boolean checkEnable){
        message = notice;
        isEnable = checkEnable;
    }
    public void setX(int XLocation){
        x = XLocation;
    }
    public void setY(int YLocation){
        y = YLocation;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void remove(){
        x = 10000;
        y = 10000;
    }
    public void timeAdd(){
        time = time + 1;
    }
    public int getTime(){
        return time;
    }
    public void runDraw(){
        Minecraft.getMinecraft().renderEngine.bindTexture(NoticeManager.resourceLocation);
        GlStateManager.color(1.0F,1.0F,1.0F);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,160,32,160,32);
        Minecraft.getMinecraft().fontRendererObj.drawString(message,x + 16,y + 7,16776960);
        if(isEnable) {
            Minecraft.getMinecraft().fontRendererObj.drawString("已经启用", x + 16, y + 17, 16776960);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawString("已经禁用", x + 16, y + 17, 16776960);
        }
    }
}
