package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Eagle {
    public Eagle(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("Eagle")) {
            if (Minecraft.getMinecraft().theWorld != null) {
                if (Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - 1, Minecraft.getMinecraft().thePlayer.posZ)).getBlock() == Blocks.air) {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(),true);
                } else {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(),false);
                }
            }
        }
    }
    @SubscribeEvent
    public void onDisabled(CustomEventHandler.FunctionDisabledEvent event) {
        if (event.function.getName().equals("Eagle")) {
            if (Minecraft.getMinecraft().theWorld != null) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(),false);
            }
        }
    }
}
