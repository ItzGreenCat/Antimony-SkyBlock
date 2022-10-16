package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
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
            if(FunctionManager.getStatus("Sprint") && Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed()){
                Minecraft.getMinecraft().thePlayer.setSprinting(true);
            }
        }
    }
}
