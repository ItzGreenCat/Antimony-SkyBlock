package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.common.mixins.EntityPlayerSPAccessor;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WTap implements ReflectionlessEventHandler {
    public static boolean bowMode = false;

    public WTap() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    public void onPacket(CustomEventHandler.PacketSentEvent event) {
        bowMode = (Boolean) ConfigInterface.get("WTap","bowMode");
        if (FunctionManager.getStatus("WTap")) {
            if ((event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) event.packet).getAction() == C02PacketUseEntity.Action.ATTACK || bowMode && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging) event.packet).getStatus() == net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemBow)) {
                if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
                    Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SPRINTING));
            }
        }

    }
    public void onPacket(CustomEventHandler.PacketSentEvent.Post event) {
        if(FunctionManager.getStatus("WTap")) {
            if ((event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) event.packet).getAction() == C02PacketUseEntity.Action.ATTACK || bowMode && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging) event.packet).getStatus() == net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemBow) && !Minecraft.getMinecraft().thePlayer.isSprinting()) {
                ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).setServerSprintState(false);
            }
        }
    }

    @Override
    public void invoke(Event event) {
        if (event instanceof CustomEventHandler.PacketSentEvent) {
            onPacket((CustomEventHandler.PacketSentEvent)event);
        }
        if (event instanceof CustomEventHandler.PacketSentEvent.Post) {
            onPacket((CustomEventHandler.PacketSentEvent.Post)event);
        }
    }
}
