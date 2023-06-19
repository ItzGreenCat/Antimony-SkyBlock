package com.greencat.antimony.common.function.base;

import com.greencat.antimony.core.event.CustomEventHandler;
import me.greencat.lwebus.core.annotation.EventModule;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class FunctionStatusTrigger{
    @EventModule
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event){
        if(event.function.getName().equals(getName())){
            post();
        }
    }
    @EventModule
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals(getName())){
            init();
        }
    }
    @EventModule
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals(getName())){
            if(event.status){
                init();
            } else {
                post();
            }
        }
    }
    public abstract String getName();
    public abstract void post();
    public abstract void init();
}
