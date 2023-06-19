package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.annotation.EventModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MarketingGenerator{
    public MarketingGenerator() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @EventModule
    public void onEnable(CustomEventHandler.FunctionEnableEvent event) {
        if(event.function.getName().equals("MarketingGenerator")){
            event.setCanceled(true);
            Minecraft.getMinecraft().thePlayer.sendChatMessage(Utils.MarketingAccountGenerator((String) ConfigInterface.get("MarketingGenerator","name"),(String) ConfigInterface.get("MarketingGenerator","event"),(String) ConfigInterface.get("MarketingGenerator","explain")));
        }
    }
    @EventModule
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {
        if(event.function.getName().equals("MarketingGenerator")){
            if(event.status) {
                event.setCanceled(true);
                Minecraft.getMinecraft().thePlayer.sendChatMessage(Utils.MarketingAccountGenerator((String) ConfigInterface.get("MarketingGenerator","name"),(String) ConfigInterface.get("MarketingGenerator","event"),(String) ConfigInterface.get("MarketingGenerator","explain")));
            }
        }
    }
}
