package com.greencat.antimony.common.mixins;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S08PacketPlayerPosLook.class)
public interface S08PacketPlayerPosLookAccessor {
    @Accessor("x")
    double getX();
    @Accessor("y")
    double getY();
    @Accessor("z")
    double getZ();
}
