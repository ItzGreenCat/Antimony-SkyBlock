package com.greencat.antimony.common.mixins;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(C03PacketPlayer.class)
public class MixinC03PacketPlayer {
    @Shadow
    protected
    float yaw;
    @Shadow
    protected
    float pitch;
}
