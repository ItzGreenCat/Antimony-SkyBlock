package com.greencat.common.mixins;

import com.greencat.common.event.CustomEventHandler;
import com.greencat.utils.Utils;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

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
        if(CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.PacketReceivedEvent(packet, context))) {
            callbackInfo.cancel();
        }
    }


}
