package com.greencat.common.mixins;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({EntityLivingBase.class})
public abstract class MixinLivingEntityBase extends MixinEntity{
    @Shadow
    public float rotationYawHead;
    @Shadow
    private int jumpTicks;
    @Shadow
    protected boolean isJumping;
    @Shadow
    public float jumpMovementFactor;
    @Shadow
    protected int newPosRotationIncrements;
    @Shadow
    public float moveForward;
    @Shadow
    public float moveStrafing;
    @Shadow
    protected float movedDistance;
    @Shadow
    protected int entityAge;
    @Shadow
    protected double newPosX;
}
