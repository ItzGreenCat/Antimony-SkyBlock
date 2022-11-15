package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.ServerRotation;
import com.greencat.antimony.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook")
public class MixinC06PacketPlayerPosLook extends MixinC03PacketPlayer{
    @Inject(
            method = "<init>(DDDFFZ)V",
            at = @At("RETURN")
    )
    public void init(double x, double y, double z, float yaw, float p, boolean p_i45941_7_, CallbackInfo ci) {
        if (ServerRotation.usingServerRotation) {
            this.yaw = ServerRotation.yaw;
            this.pitch = ServerRotation.pitch;
        }
    }
}