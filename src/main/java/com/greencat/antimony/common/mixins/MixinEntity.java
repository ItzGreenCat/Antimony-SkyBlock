package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    public double posY;
    @Shadow
    public double posX;
    @Shadow
    public float rotationPitch;

    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();
    @Shadow
    public abstract boolean isSprinting();
    @Shadow
    protected abstract void setFlag(int flag, boolean value);
    @Inject(method = "moveFlying", at = @At("HEAD"), cancellable = true)
    private void handleRotations(float strafe, float forward, float friction, final CallbackInfo callbackInfo) {
        if ((Object) this != Minecraft.getMinecraft().thePlayer)
            return;
        Antimony.strafe = strafe;
        Antimony.forward = forward;
        Antimony.friction = friction;
        CustomEventHandler.MoveStrafeEvent event = new CustomEventHandler.MoveStrafeEvent(strafe,forward,friction);
        CustomEventHandler.EVENT_BUS.post(event);
        if(event.isCanceled()){
            callbackInfo.cancel();
        }
    }
}
