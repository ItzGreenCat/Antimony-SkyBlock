package com.greencat.utils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class Chroma {
    public static float CurrentColor = 0;
    public static Color color;
    public Chroma() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event){
        if(CurrentColor + 0.002 < 1.0F){
            CurrentColor = CurrentColor + 0.002F;
        } else {
            CurrentColor = 0;
        }
        color = Color.getHSBColor(CurrentColor,1.0F,1.0F);
    }
    public static int getR(){
        return color.getRed();
    }
    public static int getG(){
        return color.getGreen();
    }
    public static int getB(){
        return color.getBlue();
    }
}