package com.GreenCat.type;

import com.GreenCat.common.ui.NoticeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class Notice {
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
    int x = scaledResolution.func_78326_a() / 2 - (160 / 2);
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
        Minecraft.func_71410_x().field_71446_o.func_110577_a(NoticeManager.resourceLocation);
        GlStateManager.func_179124_c(1.0F,1.0F,1.0F);
        Gui.func_146110_a(x,y,0,0,160,32,160,32);
        Minecraft.func_71410_x().field_71466_p.func_78276_b(message,x + 16,y + 7,16776960);
        if(isEnable) {
            Minecraft.func_71410_x().field_71466_p.func_78276_b("已经启用", x + 16, y + 17, 16776960);
        } else {
            Minecraft.func_71410_x().field_71466_p.func_78276_b("已经禁用", x + 16, y + 17, 16776960);
        }
    }
}
