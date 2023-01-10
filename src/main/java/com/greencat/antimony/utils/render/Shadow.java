package com.greencat.antimony.utils.render;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Shadow {
    public static void drawShadow(int x,int y,int w,int h){
        GlStateManager.color(1.0F,1.0F,1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelleft.png"));
        Gui.drawModalRectWithCustomSizedTexture(x - 19,y,0,0,19,h,19,h);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelright.png"));
        Gui.drawModalRectWithCustomSizedTexture(x + w,y,0,0,19,h,19,h);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/paneltop.png"));
        Gui.drawModalRectWithCustomSizedTexture(x,y - 19,0,0,w,19,w,19);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelbottom.png"));
        Gui.drawModalRectWithCustomSizedTexture(x,y + h,0,0,w,19,w,19);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/paneltopleft.png"));
        Gui.drawModalRectWithCustomSizedTexture(x - 18,y - 18,0,0,18,18,18,18);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/paneltopright.png"));
        Gui.drawModalRectWithCustomSizedTexture(x + w,y - 18,0,0,18,18,18,18);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelbottomleft.png"));
        Gui.drawModalRectWithCustomSizedTexture(x - 18,y + h,0,0,18,18,18,18);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelbottomright.png"));
        Gui.drawModalRectWithCustomSizedTexture(x + w,y + h,0,0,18,18,18,18);
    }
    public static void drawShadow(int x,int y,int w,int h,ShadowLocation location){
        GlStateManager.color(1.0F,1.0F,1.0F);
        if(location == ShadowLocation.TOP){
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/paneltop.png"));
            Gui.drawModalRectWithCustomSizedTexture(x,y - 19,0,0,w,19,w,19);
        } else if(location == ShadowLocation.BOTTOM){
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelbottom.png"));
            Gui.drawModalRectWithCustomSizedTexture(x,y + h,0,0,w,19,w,19);
        } else if (location == ShadowLocation.LEFT) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelleft.png"));
            Gui.drawModalRectWithCustomSizedTexture(x - 19,y,0,0,19,h,19,h);
        } else if(location == ShadowLocation.RIGHT){
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelright.png"));
            Gui.drawModalRectWithCustomSizedTexture(x + w,y,0,0,19,h,19,h);
        } else if(location == ShadowLocation.TOP_LEFT){
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/paneltopleft.png"));
            Gui.drawModalRectWithCustomSizedTexture(x - 18,y - 18,0,0,18,18,18,18);
        } else if(location == ShadowLocation.TOP_RIGHT){
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/paneltopright.png"));
            Gui.drawModalRectWithCustomSizedTexture(x + w,y - 18,0,0,18,18,18,18);
        } else if(location == ShadowLocation.BOTTOM_LEFT){
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelbottomleft.png"));
            Gui.drawModalRectWithCustomSizedTexture(x - 18,y + h,0,0,18,18,18,18);
        } else if(location == ShadowLocation.BOTTOM_RIGHT){
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"shadow/panelbottomright.png"));
            Gui.drawModalRectWithCustomSizedTexture(x + w,y + h,0,0,18,18,18,18);
        }
    }
    public enum ShadowLocation{
        TOP,BOTTOM,LEFT,RIGHT,TOP_LEFT,TOP_RIGHT,BOTTOM_LEFT,BOTTOM_RIGHT;
    }
}
