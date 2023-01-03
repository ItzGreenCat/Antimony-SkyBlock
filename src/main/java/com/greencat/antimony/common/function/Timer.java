package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.MinecraftAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.greencat.antimony.core.config.getConfigByFunctionName.get;

public class Timer {
    float speed = 1F;
    boolean onlyMoving = true;
    boolean isEnable = false;
    Minecraft mc = Minecraft.getMinecraft();
    public Timer(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event){
        if(event.function.getName().equals("Timer")){
            post();
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("Timer") && (!event.status)){
            post();
        }
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            if(FunctionManager.getStatus("Timer")){
                speed = (float)(double)get("Timer","speed");
                onlyMoving = (Boolean)get("Timer","onlyMoving");
                isEnable = true;
            } else {
                isEnable = false;
            }
        }
    }
    @SubscribeEvent
    public void onPlayerUpdate(CustomEventHandler.PlayerUpdateEvent event){
        if(isEnable) {
            if (Utils.isMoving() || !onlyMoving) {
                ((MinecraftAccessor) mc).getTimer().timerSpeed = speed;
            }
        } else if(((MinecraftAccessor) mc).getTimer().timerSpeed != 1F) {
            ((MinecraftAccessor) mc).getTimer().timerSpeed = 1F;
        }
    }
    private void post(){
        if (mc.thePlayer != null) {
            ((MinecraftAccessor) mc).getTimer().timerSpeed = 1F;
        }
    }
}
