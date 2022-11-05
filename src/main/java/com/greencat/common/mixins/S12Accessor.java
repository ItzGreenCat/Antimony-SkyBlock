package com.greencat.common.mixins;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S12PacketEntityVelocity.class)
public interface S12Accessor {
    @Accessor("motionX")
    void setMotionX(int MotionX);
    @Accessor("motionZ")
    void setMotionZ(int MotionZ);
}
