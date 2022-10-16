package com.greencat.common.mixins;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.function.Killaura;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Shadow
    private Entity renderViewEntity;
    @Inject(
            method = "createDisplay",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V",
                    remap = false
            )
    )
    private void inject_createDisplay(CallbackInfo cbi) {
        Display.setTitle("Antimony Mixin Test Title");
    }
    @Inject(
            method = {"getRenderViewEntity"},
            at = {@At("HEAD")}
    )
    public void getRenderViewEntity(CallbackInfoReturnable<Entity> cir) {
        if (FunctionManager.getStatus("Killaura") && this.renderViewEntity != null && this.renderViewEntity == Minecraft.getMinecraft().thePlayer) {
            if (Killaura.entityTarget != null) {
                ((EntityLivingBase)this.renderViewEntity).rotationYawHead = ((EntityPlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
                ((EntityLivingBase)this.renderViewEntity).renderYawOffset = ((EntityPlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
            }

        }
    }
}