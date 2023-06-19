package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoTool implements ReflectionlessEventHandler {
    public static long latest;
    public AutoTool() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    public static boolean isEnable = false;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("AutoTool")){
            isEnable = true;
        } else {
            isEnable = false;
        }
    }
    public void onClickBlock(CustomEventHandler.ClickBlockEvent event){
        if(isEnable){
            Switch(event.pos);
        }
    }
    public static void Switch(BlockPos pos){
        try {
            int bestSlot = -1;
            float bestSpeed = 1.0F;
            if (System.currentTimeMillis() - latest >= 0) {
                latest = System.currentTimeMillis();
                for (int i = 0; i < 8; ++i) {
                    ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                    if (stack != null && stack.getItem().getStrVsBlock(stack,Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()) > bestSpeed)
                    {
                        bestSlot = i;
                        bestSpeed = stack.getItem().getStrVsBlock(stack,Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock());
                    }
                }
                if(bestSlot != -1) {
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = bestSlot;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void invoke(Event event) {
        if(event instanceof CustomEventHandler.ClickBlockEvent){
            onClickBlock((CustomEventHandler.ClickBlockEvent) event);
        }
    }
}
