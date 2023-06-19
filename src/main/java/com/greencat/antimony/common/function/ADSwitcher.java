package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ADSwitcher extends FunctionStatusTrigger {
    double lastY = 0;
    double KeepY = 0;
    long lastFall = 0L;
    boolean state = false;
    public ADSwitcher(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @Override
    public String getName() {
        return "ADSwitcher";
    }

    @Override
    public void post() {
        KeepY = Minecraft.getMinecraft().thePlayer.posY;
        lastY = Minecraft.getMinecraft().thePlayer.posY;
        lastFall = System.currentTimeMillis();
        if(((Integer) ConfigInterface.get("ADSwitcher","keyPress")) == 0){
            state = true;
        } else if(((Integer) ConfigInterface.get("ADSwitcher","keyPress")) == 1){
            state = false;
        }
    }

    @Override
    public void init() {
        KeepY = Minecraft.getMinecraft().thePlayer.posY;
        lastY = Minecraft.getMinecraft().thePlayer.posY;
        lastFall = System.currentTimeMillis();
        if(((Integer) ConfigInterface.get("ADSwitcher","keyPress")) == 0){
            state = true;
        } else if(((Integer) ConfigInterface.get("ADSwitcher","keyPress")) == 1){
            state = false;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && FunctionManager.getStatus("ADSwitcher")){
            if(Minecraft.getMinecraft().thePlayer.posY != lastY){
                lastY = Minecraft.getMinecraft().thePlayer.posY;
                lastFall = System.currentTimeMillis();
            }
            if (Math.abs(lastY - KeepY) > 0.7 && System.currentTimeMillis() - lastFall > 100L) {
                KeepY = Minecraft.getMinecraft().thePlayer.posY;
                lastY = Minecraft.getMinecraft().thePlayer.posY;
                state = !state;
            }
            if(state){
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(),true);
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(),false);
            } else {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(),false);
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(),true);
            }
        }
    }
}
