package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public abstract class MixinGuiConnecting {

    @Inject(method = "connect", at = @At("HEAD"))
    public void injectConnect(String ip, int port, CallbackInfo ci) {
        Antimony.setLastServer(ip + ":" + port);
    }
}
