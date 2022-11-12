package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiAFKJump {
    int tick = 0;
    final int maxTick = 500;
    public AntiAFKJump(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("AntiAFKJump")) {
                if (tick != maxTick) {
                    tick = tick + 1;
                }
                if (tick == maxTick - 100){
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(),true);
                }
                if(tick == maxTick){
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(),false);
                    tick = 0;
                }
            }
        }
    }
}
