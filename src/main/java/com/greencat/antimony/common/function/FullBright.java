package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FullBright {
    public static float OriginalGamma;
    public FullBright(){
        MinecraftForge.EVENT_BUS.register(this);
        OriginalGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("FullBright")){
            Minecraft.getMinecraft().gameSettings.gammaSetting = 1000;
        } else {
            Minecraft.getMinecraft().gameSettings.gammaSetting = OriginalGamma;
        }
    }
}
