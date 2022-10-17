package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.common.mixins.EntityPlayerSPAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WTap {
    public static boolean bowMode = false;

    public WTap() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPacket(CustomEventHandler.PacketSentEvent event) {
        bowMode = (Boolean)getConfigByFunctionName.get("WTap","bowMode");
        if (FunctionManager.getStatus("WTap")) {
            if ((event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) event.packet).getAction() == C02PacketUseEntity.Action.ATTACK || bowMode && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging) event.packet).getStatus() == net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemBow)) {
                if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
                    Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SPRINTING));
                }

                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SPRINTING));
            }
        }

    }

    @SubscribeEvent
    public void onPacket(CustomEventHandler.PacketSentEvent.Post event) {
        if(FunctionManager.getStatus("WTap")) {
            if ((event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) event.packet).getAction() == C02PacketUseEntity.Action.ATTACK || bowMode && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging) event.packet).getStatus() == net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemBow) && !Minecraft.getMinecraft().thePlayer.isSprinting()) {
                ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).setServerSprintState(false);
            }
        }
    }
}
