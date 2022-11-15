package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.ServerRotation;
import com.greencat.antimony.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook")
public class MixinC05PacketPlayerLook extends MixinC03PacketPlayer{
    @Inject(
            method = "<init>(FFZ)V",
            at = @At("RETURN")
    )
    public void init(float p_i45255_1_, float p_i45255_2_, boolean p_i45255_3_, CallbackInfo ci) {
        if (ServerRotation.usingServerRotation) {
            this.yaw = ServerRotation.yaw;
            this.pitch = ServerRotation.pitch;
        }
    }
}