package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
    @Inject(
            method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        if (!Utils.noEvent.contains(packet) && CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.PacketSentEvent(packet))) {
            callbackInfo.cancel();
        }

    }

    @Inject(
            method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private void onSendPacketPost(Packet<?> packet, CallbackInfo callbackInfo) {
        if (!Utils.noEvent.contains(packet) && CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.PacketSentEvent.Post(packet))) {
            callbackInfo.cancel();
        }
        Utils.noEvent.remove(packet);
    }
    @Inject(
            method = {"channelRead0"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onChannelReadHead(ChannelHandlerContext context, Packet packet, CallbackInfo callbackInfo) {
        CustomEventHandler.PacketReceivedEvent event = new CustomEventHandler.PacketReceivedEvent(packet, context);
        CustomEventHandler.EVENT_BUS.post(event);
        if(event.isCanceled()){
            callbackInfo.cancel();
        }

        //test
        /*if (Minecraft.getMinecraft().thePlayer != null && true) {
            if (FunctionManager.getStatus("Velocity") && !Minecraft.getMinecraft().thePlayer.isInLava()) {
                ItemStack held = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (held == null || !held.getDisplayName().contains("Bonzo's Staff") && !held.getDisplayName().contains("Jerry-chine Gun")) {
                    if (packet instanceof S27PacketExplosion) {
                        callbackInfo.cancel();
                    }

                    if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).getEntityID() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
                        callbackInfo.cancel();
                    }
                }
            }
        }*/
    }


}
