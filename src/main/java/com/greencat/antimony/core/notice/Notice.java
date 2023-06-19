package com.greencat.antimony.core.notice;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Blur;
import com.greencat.antimony.utils.FontManager;
import com.greencat.antimony.utils.render.AnimationEngine;
import com.greencat.antimony.utils.render.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;


public class Notice {
    String[] message;
    String title;
    long startTime = 0;
    AnimationEngine animation = new AnimationEngine();
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    public Notice(String title, boolean hasTitle, String... message){
        this.message = message;
        this.title = hasTitle ? title : "Notification";
        animation.xCoord = scaledResolution.getScaledWidth();
        animation.yCoord = scaledResolution.getScaledHeight();
    }
    public Notice(String... message){
        this("",false,message);
    }
    public void init(){
        animation.moveTo(scaledResolution.getScaledWidth() - (getLongestWidth() + 5 + 3),scaledResolution.getScaledHeight() - (2 + getHeight()),0.25);
        startTime = System.currentTimeMillis();
    }
    public void next(Notice notice){
        animation.moveTo((int) animation.targetX, (int) (animation.targetY - (notice.getHeight() + 5)),0.25);
    }
    public boolean isAlive(){
        return animation.xCoord <= scaledResolution.getScaledWidth();
    }
    public void render(){
        int x = (int)animation.xCoord;
        int y = (int)animation.yCoord;
        int width = getLongestWidth() + 5;
        int height = 5;
        GlStateManager.pushMatrix();
        Blur.renderBlur(x,y,width,getHeight(),10);
        GlStateManager.popMatrix();
        FontManager.QuicksandBoldFont.drawString(title, x + (getLongestWidth() / 2.0f) - (FontManager.QuicksandBoldFont.getStringWidth(title) / 2.0f), y + height, 0xFFFFFF);
        height = height + 13;
        for(String str : message){
            FontManager.QuicksandFont.drawString(str,x + 3,y + height - 5,0xFFFFFF);
            height = height + 13;
        }
        Shadow.drawShadow(x,y,width,getHeight());
    }
    public void remove(){
        animation.moveTo(scaledResolution.getScaledWidth() + 10, (int) animation.yCoord,0.25);
    }
    private int getHeight() {
        return 2 + 13 + (message.length * 11) - 5 + 3;
    }
    private int getLongestWidth(){
        int longest = 0;
        for(String str : message){
            longest = Math.max(longest,FontManager.QuicksandFont.getStringWidth(str));
        }
        longest = Math.max(longest,15 + FontManager.QuicksandBoldFont.getStringWidth(title));
        return Math.max(longest, FunctionManager.getLongestTextWidthAdd5() - 5) + 15;
    }
}
