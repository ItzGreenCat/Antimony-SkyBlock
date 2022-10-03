package com.greencat.common.mixins;

import com.greencat.common.Chat.SendToServer;
import com.greencat.common.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {
    @Inject(method = "sendChatMessage",cancellable = true,at={@At("HEAD")})
    public void sendChatMessage(String p_sendChatMessage_1_, CallbackInfo cbi) {
        if(ConfigLoader.getChatChannel() && !p_sendChatMessage_1_.startsWith("/")){
            SendToServer sendToServer = new SendToServer();
            sendToServer.send(Minecraft.getMinecraft().thePlayer.getName() + "MSG-!-SPLIT" + p_sendChatMessage_1_);
            cbi.cancel();
        }
    }
}
