package com.greencat.antimony.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AnimationEngine {
    public double xCoord;
    public double yCoord;
    public double targetX;
    public double targetY;
    public double xStep;
    public double yStep;
    private boolean modifyX;
    private boolean modifyY;
    public AnimationEngine(int x,int y) {
        xCoord = x;
        yCoord = y;
        MinecraftForge.EVENT_BUS.register(this);
    }
    public AnimationEngine() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    public void moveTo(int x,int y,double second){
        if(targetX != x) {
            this.targetX = x;
            modifyX = true;
        }
        if(targetY != y) {
            this.targetY = y;
            modifyY = true;
        }
        double xStep;
        double xInterpolation;
        double yStep;
        double yInterpolation;
        xInterpolation = Math.abs(targetX - xCoord);
        yInterpolation = Math.abs(targetY - yCoord);
        if(Minecraft.getDebugFPS() != 0) {
            if(xInterpolation > 0) {
                xStep = xInterpolation / (Minecraft.getDebugFPS() * second);
            } else {
                xStep = 0;
            }
            if(yInterpolation > 0) {
                yStep = yInterpolation / (Minecraft.getDebugFPS() * second);
            } else {
                yStep = 0;
            }
        } else {
            xStep = 1;
            yStep = 1;
        }
        if(targetX != x || modifyX) {
            this.xStep = xStep;
            modifyX = false;
        }
        if(targetY != y || modifyY) {
            this.yStep = yStep;
            modifyY = false;
        }
    }
    @SubscribeEvent
    public void RenderTick(TickEvent.RenderTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            if(targetX != xCoord){
                if(Math.abs(targetX - xCoord) > xStep) {
                    if (xCoord > targetX) {
                        xCoord = xCoord - xStep;
                    } else {
                        xCoord = xCoord + xStep;
                    }
                } else {
                    xCoord = targetX;
                }
            }
            if(targetY != yCoord){
                if(Math.abs(targetY - yCoord) > yStep) {
                    if (yCoord > targetY) {
                        yCoord = yCoord - yStep;
                    } else {
                        yCoord = yCoord + yStep;
                    }
                } else {
                    yCoord = targetY;
                }
            }
        }
    }
    public void destroy(){
        MinecraftForge.EVENT_BUS.unregister(this);
    }
    public void enable(){
        MinecraftForge.EVENT_BUS.register(this);
    }
}
