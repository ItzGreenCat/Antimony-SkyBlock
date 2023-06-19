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
    public double sec = 0;
    public int prevFPS = 1;
    public long lastMoveTo = 0;
    public AnimationEngine(int x,int y) {
        xCoord = x;
        yCoord = y;
        targetX = x;
        targetY = y;
    }
    public AnimationEngine() {
        xCoord = 0;
        yCoord = 0;
        targetX = 0;
        targetY = 0;
    }
    public void register(){
        AnimationManager.add(this);
    };
    public void moveTo(int x,int y,double second){
        register();
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
        prevFPS = Minecraft.getDebugFPS();
        sec = second;
        lastMoveTo = System.currentTimeMillis();
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
    public void RenderTick(){
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
            if(targetX == xCoord && targetY == yCoord){
                destroy();
            }
            if(targetX != xCoord || targetY != yCoord){
                if(Math.abs(Minecraft.getDebugFPS() - prevFPS) > 5){
                    destroy();
                    moveTo((int)this.targetX,(int)this.targetY,(sec * 1000 - (System.currentTimeMillis() - lastMoveTo)) / 1000);
                }
            }
    }
    public void destroy(){
        AnimationManager.destroy(this);
    }
}
