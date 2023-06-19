package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.C17PacketCustomPayloadAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import io.netty.buffer.Unpooled;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientSpoof implements ReflectionlessEventHandler {
    public ClientSpoof() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    public void onPacket(CustomEventHandler.PacketEvent event){
        if (FunctionManager.getStatus("ClientSpoof")) {
            Packet<?> packet = event.packet;
            int mode = (Integer) ConfigInterface.get("ClientSpoof","clientSpoofMode");
            if ((Boolean) ConfigInterface.get("ClientSpoof","clientSpoof") && !Minecraft.getMinecraft().isIntegratedServerRunning() && mode == 0){
                try {
                    if (packet instanceof C17PacketCustomPayload) {
                        final C17PacketCustomPayload customPayload = (C17PacketCustomPayload) packet;
                        ((C17PacketCustomPayloadAccessor) customPayload).setData(new PacketBuffer(Unpooled.buffer()).writeString("vanilla"));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            if ((Boolean) ConfigInterface.get("ClientSpoof","clientSpoof") && !Minecraft.getMinecraft().isIntegratedServerRunning() && mode == 1) {
                try {
                    if (packet instanceof C17PacketCustomPayload) {
                        final C17PacketCustomPayload customPayload = (C17PacketCustomPayload) packet;
                        ((C17PacketCustomPayloadAccessor) customPayload).setData(new PacketBuffer(Unpooled.buffer()).writeString("LMC"));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            if ((Boolean) ConfigInterface.get("ClientSpoof","clientSpoof") && !Minecraft.getMinecraft().isIntegratedServerRunning() && mode == 2) {
                try {
                    if (packet instanceof C17PacketCustomPayload) {
                        final C17PacketCustomPayload customPayload = (C17PacketCustomPayload) packet;
                        ((C17PacketCustomPayloadAccessor) customPayload).setData(new PacketBuffer(Unpooled.buffer()).writeString("CB"));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            if ((Boolean) ConfigInterface.get("ClientSpoof","clientSpoof") && !Minecraft.getMinecraft().isIntegratedServerRunning() && mode == 3) {
                try {
                    if (packet instanceof C17PacketCustomPayload) {
                        final C17PacketCustomPayload customPayload = (C17PacketCustomPayload) packet;
                        ((C17PacketCustomPayloadAccessor) customPayload).setData(new PacketBuffer(Unpooled.buffer()).writeString("Lunar-Client"));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            if ((Boolean) ConfigInterface.get("ClientSpoof","clientSpoof") && !Minecraft.getMinecraft().isIntegratedServerRunning() && mode == 4) {
                try {
                    if (packet instanceof C17PacketCustomPayload) {
                        final C17PacketCustomPayload customPayload = (C17PacketCustomPayload) packet;
                        ((C17PacketCustomPayloadAccessor) customPayload).setData(new PacketBuffer(Unpooled.buffer()).writeString("PLC18"));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            if (!Minecraft.getMinecraft().isIntegratedServerRunning() || (Boolean) ConfigInterface.get("ClientSpoof","fmlFix")) {
                if(((Boolean) ConfigInterface.get("ClientSpoof","proxySpoof")) && packet.getClass().getName().equals("net.minecraftforge.fml.common.network.internal.FMLProxyPacket")){
                    event.setCanceled(true);
                } else if(packet instanceof C17PacketCustomPayload){
                    if (((Boolean) ConfigInterface.get("ClientSpoof","brandSpoof")) && !((C17PacketCustomPayload) packet).getChannelName().startsWith("MC|")) {
                        event.setCanceled(true);
                    } else if (((C17PacketCustomPayload) packet).getChannelName().equalsIgnoreCase("MC|Brand")) {
                        int brandMode = (Integer) ConfigInterface.get("ClientSpoof","brands");
                        if(brandMode != 3) {
                            ((C17PacketCustomPayloadAccessor) packet).setData(new PacketBuffer(Unpooled.buffer()).writeString(brandMode == 0 ? "vanilla" : brandMode == 1 ? "lunarclient" : "CB"));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void invoke(Event event) {
        if (event instanceof CustomEventHandler.PacketEvent) {
                onPacket((CustomEventHandler.PacketEvent) event);
        }
    }
}
