package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class AutoPickaxe {
    Utils utils = new Utils();
    public AutoPickaxe() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void MiningTrigger(PlayerInteractEvent event) throws NullPointerException{
        if(FunctionManager.getStatus("AutoPickaxe")) {
            if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
                Minecraft.getMinecraft().thePlayer.inventory.currentItem = Utils.FindPickaxeInHotBar();
            }
        }
    }
}
