package com.greencat.common.mixins;

import com.greencat.common.event.CustomEventHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S40PacketDisconnect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {

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
