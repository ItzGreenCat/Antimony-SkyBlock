package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MouseISwitch {
    public MouseISwitch() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("MouseISwitch")){
            if(Minecraft.getMinecraft().theWorld != null) {
                try {
                    if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getDisplayName().toLowerCase()).contains(ConfigLoader.getMSwitch().split("~~SPLIT~~")[0].toLowerCase())) {
                        if (ConfigLoader.getMSwitch().split("~~SPLIT~~")[1].equals("RIGHT") && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) {
                            InstantSwitch.InstantSwitchCore();
                        } else if (ConfigLoader.getMSwitch().split("~~SPLIT~~")[1].equals("LEFT") && Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown()) {
                            InstantSwitch.InstantSwitchCore();
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
