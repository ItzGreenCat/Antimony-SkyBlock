package com.greencat.antimony.core;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class nukerWrapper {
    public static nukerCore2 nuker = new nukerCore2("NukerWrapper");
    public static boolean enable = false;
    public static int tick = 0;
    Utils utils = new Utils();
    public nukerWrapper(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("NukerWrapper")){
            nuker.post();
            tick = 0;
            nuker.enable = false;
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("NukerWrapper")) {
            if(enable) {
                 if(event.status) {
                    nuker.init();
                     tick = 0;
                    nuker.enable = true;
                }
            } else {
                nuker.post();
                tick = 0;
                nuker.enable = false;
            }
            if (!event.status) {
                nuker.post();
                tick = 0;
                nuker.enable = false;
            }
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals("NukerWrapper")){
            if(enable) {
                nuker.init();
                tick = 0;
                nuker.enable = true;
            } else {
                nuker.post();
                tick = 0;
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
        if(nuker.pos != null) {
            if (tick > 400) {
                Utils.print("Nuker超时,重置状态");
                disable();
                enable();
                tick = 0;
            } else {
                tick = tick + 1;
            }
        } else {
            tick = 0;
        }
    }
    public static void enable(){
        FunctionManager.setStatusNoNotice("NukerWrapper",true);
    }
    public static void disable(){
        FunctionManager.setStatusNoNotice("NukerWrapper",false);
    }
}
