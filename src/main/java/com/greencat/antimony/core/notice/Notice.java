package com.greencat.antimony.core.notice;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Blur;
import com.greencat.antimony.utils.FontManager;
import com.greencat.antimony.utils.render.AnimationEngine;
import com.greencat.antimony.utils.render.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class Notice {
    String[] message;
    long startTime = 0;
    AnimationEngine animation = new AnimationEngine();
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    public Notice(String... message){
        this.message = message;
        animation.xCoord = scaledResolution.getScaledWidth();
        animation.yCoord = scaledResolution.getScaledHeight();
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
        for(String str : message){
            FontManager.QuicksandBoldFont.drawString(str,x + 3,y + height,0xFFFFFF);
            height = height+ 13;
        }
        Shadow.drawShadow(x,y,width,getHeight());
    }
    public void remove(){
        animation.moveTo(scaledResolution.getScaledWidth() + 10, (int) animation.yCoord,0.25);
    }
    private int getHeight() {
        return 2 + (message.length * 13) + 3;
    }
    private int getLongestWidth(){
        int longest = 0;
        for(String str : message){
            longest = Math.max(longest,FontManager.QuicksandBoldFont.getStringWidth(str));
        }
        return Math.max(longest, FunctionManager.getLongestTextWidthAdd5() - 5) + 15;
    }
}
