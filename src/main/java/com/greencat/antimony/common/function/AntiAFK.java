package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.greencat.antimony.core.config.getConfigByFunctionName.get;

public class AntiAFK {
    int tick = 0;
    final int maxTick = 500;
    int type = 0;
    public AntiAFK(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("AntiAFK")) {
                type = (Integer) get("AntiAFK","type");
                if (tick != maxTick) {
                    tick = tick + 1;
                }
                if(type == 0) {
                    if (tick == maxTick - 100) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), true);
                    }
                    if (tick == maxTick) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
                        tick = 0;
                    }
                } else if(type == 1){
                    if (tick == maxTick - 10) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), true);
                    }
                    if (tick == maxTick - 5) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), true);
                    }
                    if (tick == maxTick) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), false);
                        tick = 0;
                    }
                }
            }
        }
    }
}
