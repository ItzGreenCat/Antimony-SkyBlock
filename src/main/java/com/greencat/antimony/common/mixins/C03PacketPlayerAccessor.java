package com.greencat.antimony.common.mixins;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(C03PacketPlayer.class)
public interface C03PacketPlayerAccessor {
    @Accessor("yaw")
    void setYaw(float yaw);
    @Accessor("pitch")
    void setPitch(float pitch);
    @Accessor("rotating")
    void setRotating(boolean rotating);
    @Accessor("x")
    void setX(double x);
    @Accessor("y")
    void setY(double y);
    @Accessor("z")
    void setZ(double z);
    @Accessor("onGround")
    void setOnGround(boolean onGround);


}
