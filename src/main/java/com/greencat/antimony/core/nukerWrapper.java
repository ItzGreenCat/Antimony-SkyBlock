package com.greencat.antimony.core;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class nukerWrapper {
    public static nukerCore2 nuker = new nukerCore2("NukerWrapper");
    public static boolean enable = false;
    public nukerWrapper(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("NukerWrapper")){
            nuker.post();
            nuker.enable = false;
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("NukerWrapper")) {
            if(enable) {
                 if(event.status) {
                    nuker.init();
                    nuker.enable = true;
                }
            } else {
                nuker.post();
                nuker.enable = false;
            }
            if (!event.status) {
                nuker.post();
                nuker.enable = false;
            }
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals("NukerWrapper")){
            if(enable) {
                nuker.init();
                nuker.enable = true;
            } else {
                nuker.post();
                nuker.enable = false;
            }
        }
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("NukerWrapper")){
            nuker.enable = enable;
            nuker.active = enable;
        } else {
            nuker.enable = false;
            nuker.active = false;
        }
    }
    public static void enable(){
        FunctionManager.setStatusNoNotice("NukerWrapper",true);
    }
    public static void disable(){
        FunctionManager.setStatusNoNotice("NukerWrapper",false);
    }
}
