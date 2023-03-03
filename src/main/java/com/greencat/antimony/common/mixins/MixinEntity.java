package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
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

    @Shadow public World worldObj;

    @Shadow private int entityId;

    @Inject(method = "moveEntity",at = @At("HEAD"),cancellable = true)
    public void onMove(double x, double y, double z, CallbackInfo ci){
        if(Minecraft.getMinecraft().theWorld != null){
            if (this.worldObj != null) {
                if(Minecraft.getMinecraft().thePlayer.getEntityId() == this.entityId && Minecraft.getMinecraft().theWorld == worldObj){
                    CustomEventHandler.CurrentPlayerMoveEvent event = new CustomEventHandler.CurrentPlayerMoveEvent(x,y,z);
                    CustomEventHandler.EVENT_BUS.post(event);
                    if(event.isCanceled()){
                        ci.cancel();
                    }
                }
            }
        }
    }

}
