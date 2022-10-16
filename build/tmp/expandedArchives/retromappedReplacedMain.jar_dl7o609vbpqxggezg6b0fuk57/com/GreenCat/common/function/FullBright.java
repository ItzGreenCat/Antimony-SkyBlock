package com.GreenCat.common.function;

import com.GreenCat.common.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FullBright {
    public static float OriginalGamma;
    public FullBright(){
        MinecraftForge.EVENT_BUS.register(this);
        OriginalGamma = Minecraft.func_71410_x().field_71474_y.field_74333_Y;
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("FullBright")){
            Minecraft.func_71410_x().field_71474_y.field_74333_Y = 1000;
        } else {
            Minecraft.func_71410_x().field_71474_y.field_74333_Y = OriginalGamma;
        }
    }
}
