package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.HUDManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoUse {
    public static long latest;
    int Tick = 0;
    int maxTick = 0;
    Minecraft mc = Minecraft.getMinecraft();
    public AutoUse(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event){
        maxTick = ((Integer) ConfigInterface.get("AutoUse","cooldown")) * 40;
        if(FunctionManager.getStatus("AutoUse")) {
            if(Minecraft.getMinecraft().theWorld != null) {
                if (Tick < maxTick) {
                    Tick = Tick + 1;
                } else {
                    if (mc.theWorld != null) {
                        if (!triggerInstantSwitch()) {
                            Utils.print("无法找到对应物品");
                        }
                        Tick = 0;
                    }
                }
            }
        }
    }
    public boolean triggerInstantSwitch() {
        boolean foundItem = false;
        if (System.currentTimeMillis() - latest >= 0) {
            latest = System.currentTimeMillis();
            for (int i = 0; i < 8; ++i) {
                ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains(((String) ConfigInterface.get("AutoUse","itemName")).toLowerCase())) {
                    int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = i;
                    Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, stack);
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = currentSlot;
                    foundItem = true;
                    break;
                }
            }
        }
        return foundItem;
    }
    @SubscribeEvent
    public void RenderText(RenderGameOverlayEvent event){
        if(FunctionManager.getStatus("AutoUse")) {
            if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
                double second = ((double) (((Integer) ConfigInterface.get("AutoUse","cooldown") * 40) - Tick)) / 40;
                HUDManager.Render("AutoUse Cooldown",(int)second,(Integer) ConfigInterface.get("AutoUse","timerX"),(Integer) ConfigInterface.get("AutoUse","timerY"));
            }
        }
    }

}
