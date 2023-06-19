package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BHop implements ReflectionlessEventHandler {
    public static boolean isEnable = false;
    public static boolean useStrafe = false;
    public BHop() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("BHop")){
            isEnable = true;
            useStrafe = (Boolean) ConfigInterface.get("BHop","strafe");
        } else {
            isEnable = false;
        }
    }
    public void onMotionPre(CustomEventHandler.MotionChangeEvent.Pre event){
        if(isEnable && !Minecraft.getMinecraft().thePlayer.isInWater() && !Minecraft.getMinecraft().thePlayer.isInLava()){
            if(Utils.isMoving()){
                if(Minecraft.getMinecraft().thePlayer.onGround){
                    Minecraft.getMinecraft().thePlayer.jump();
                    if (useStrafe) {
                        Utils.strafe();
                    }
                }
            } else if(useStrafe){
                Minecraft.getMinecraft().thePlayer.motionX = 0.0F;
                Minecraft.getMinecraft().thePlayer.motionZ = 0.0F;
            }
        }
    }

    @Override
    public void invoke(Event event) {
        if(event instanceof CustomEventHandler.MotionChangeEvent.Pre){
            onMotionPre((CustomEventHandler.MotionChangeEvent.Pre) event);
        }
    }
}
