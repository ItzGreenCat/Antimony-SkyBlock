package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sneak extends FunctionStatusTrigger {
    public Sneak() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(!FunctionManager.getStatus(getName())){
            return;
        }
        if(Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null){
            if(!Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()){
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(),true);
            }
        }
    }

    @Override
    public String getName() {
        return "Sneak";
    }

    @Override
    public void post() {
        if(Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null){
            if(Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()){
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(),false);
            }
        }
    }

    @Override
    public void init() {

    }
}
