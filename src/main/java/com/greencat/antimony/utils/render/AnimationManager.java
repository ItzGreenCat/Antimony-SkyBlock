package com.greencat.antimony.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class AnimationManager {
    public static final ConcurrentHashMap<AnimationEngine,Integer> animations = new ConcurrentHashMap<AnimationEngine,Integer>();
    public AnimationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static void add(AnimationEngine animation){
        animations.put(animation,0);
    }
    public static void destroy(AnimationEngine animation){
        animations.remove(animation);
    }
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            animations.keySet().forEach(AnimationEngine::RenderTick);
        }
    }

}
