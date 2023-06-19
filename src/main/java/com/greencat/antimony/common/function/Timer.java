package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.common.mixins.MinecraftAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.greencat.antimony.core.config.ConfigInterface.get;

public class Timer extends FunctionStatusTrigger implements ReflectionlessEventHandler {
    float speed = 1F;
    boolean onlyMoving = true;
    boolean isEnable = false;
    Minecraft mc = Minecraft.getMinecraft();
    public Timer(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @Override
    public String getName() {
        return "Timer";
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
    public void onPlayerUpdate(CustomEventHandler.PlayerUpdateEvent event){
        if(isEnable) {
            if (Utils.isMoving() || !onlyMoving) {
                ((MinecraftAccessor) mc).getTimer().timerSpeed = speed;
            }
        } else if(((MinecraftAccessor) mc).getTimer().timerSpeed != 1F) {
            ((MinecraftAccessor) mc).getTimer().timerSpeed = 1F;
        }
    }
    @Override
    public void invoke(Event event){
        if(event instanceof CustomEventHandler.PlayerUpdateEvent){
            onPlayerUpdate((CustomEventHandler.PlayerUpdateEvent) event);
        }
    }
    @Override
    public void post(){
        if (mc.thePlayer != null) {
            ((MinecraftAccessor) mc).getTimer().timerSpeed = 1F;
        }
    }

    @Override
    public void init() {

    }
}
