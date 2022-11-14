package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.common.Via;
import com.greencat.antimony.core.via.handler.CommonTransformer;
import com.greencat.antimony.core.via.handler.DecodeHandler;
import com.greencat.antimony.core.via.handler.EncodeHandler;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.NetworkManager$5")
public abstract class MixinNetworkManagerChInit {

    @Inject(method = "initChannel", at = @At(value = "TAIL"), remap = false)
    private void onInitChannel(Channel channel, CallbackInfo ci) {
        if (channel instanceof SocketChannel && Antimony.getVersion() != Via.SHARED_VERSION) {

            UserConnection user = new UserConnectionImpl(channel, true);
            new ProtocolPipelineImpl(user);

            channel.pipeline()
                    .addBefore("encoder", CommonTransformer.HANDLER_ENCODER_NAME, new EncodeHandler(user))
                    .addBefore("decoder", CommonTransformer.HANDLER_DECODER_NAME, new DecodeHandler(user));
        }
    }
}
