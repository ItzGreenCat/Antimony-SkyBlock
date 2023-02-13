package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint {
    public Sprint(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if (Minecraft.getMinecraft().theWorld != null) {
            if(FunctionManager.getStatus("Sprint")){
                if(!Utils.isMoving() || Minecraft.getMinecraft().thePlayer.isSneaking() || (!(Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel() > 0.6f) || Minecraft.getMinecraft().thePlayer.capabilities.allowFlying)){
                    Minecraft.getMinecraft().thePlayer.setSprinting(false);
                    return;
                }
                if(Minecraft.getMinecraft().thePlayer.movementInput.moveForward >= 0.6F) {
                    Minecraft.getMinecraft().thePlayer.setSprinting(true);
                }
            }
        }
    }
}
