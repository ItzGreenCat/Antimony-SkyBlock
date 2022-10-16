package com.greencat.common.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;
import java.util.UUID;

@Mixin({Entity.class})
public abstract class MixinEntity {
    @Shadow
    public Entity ridingEntity;
    @Shadow
    public boolean onGround;
    @Shadow
    public float rotationYaw;
    @Shadow
    public boolean isAirBorne;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    public double posZ;
    @Shadow
    public double posX;
    @Shadow
    public float rotationPitch;

    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();
    @Shadow
    public abstract boolean isSprinting();
}
