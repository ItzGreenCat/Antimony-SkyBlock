package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoWeapon implements ReflectionlessEventHandler {
    public static boolean isEnable = false;
    public static long latest;
    public AutoWeapon(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("AutoWeapon")){
            isEnable = true;
        } else {
            isEnable = false;
        }
    }
    public void onPacketSent(CustomEventHandler.PacketSentEvent event){
        if (isEnable && event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) event.packet).getAction() == C02PacketUseEntity.Action.ATTACK){
            Switch();
        }
    }
    public static void Switch(){
        try {
            if (System.currentTimeMillis() - latest >= 0) {
                latest = System.currentTimeMillis();
                ItemStack hand = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (hand == null || !(hand.getItem() instanceof ItemSword)) {
                    for (int i = 0; i < 8; ++i) {
                        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                        if (stack != null && stack.getItem() instanceof ItemSword) {
                            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(i));
                            Minecraft.getMinecraft().thePlayer.inventory.currentItem = i;
                            break;
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void invoke(Event event) {
        if(event instanceof CustomEventHandler.PacketSentEvent){
            onPacketSent((CustomEventHandler.PacketSentEvent) event);
        }
    }
}
