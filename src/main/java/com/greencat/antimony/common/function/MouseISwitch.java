package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MouseISwitch {
    public MouseISwitch() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    boolean leftTrigger = true;
    String itemName = "";
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        leftTrigger = (Boolean) getConfigByFunctionName.get("MouseISwitch","leftTrigger");
        itemName = (String) getConfigByFunctionName.get("MouseISwitch","itemName");
        if(FunctionManager.getStatus("MouseISwitch")){
            if(Minecraft.getMinecraft().theWorld != null) {
                try {
                    if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getDisplayName().toLowerCase()).contains(itemName.toLowerCase())) {
                        if (leftTrigger && Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown()) {
                            InstantSwitch.InstantSwitchCore();
                        } else if (!leftTrigger && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) {
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
