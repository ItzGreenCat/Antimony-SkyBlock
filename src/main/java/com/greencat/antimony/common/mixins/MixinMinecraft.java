package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.common.Via;
import com.greencat.antimony.common.function.SynthesizerAura;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.common.function.Killaura;
import com.greencat.antimony.common.function.ShortBowAura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
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
        if ((FunctionManager.getStatus("Killaura") || FunctionManager.getStatus("ShortBowAura") || FunctionManager.getStatus("SynthesizerAura")) && this.renderViewEntity != null && this.renderViewEntity == Minecraft.getMinecraft().thePlayer) {
            if (Killaura.entityTarget != null || ShortBowAura.target != null || SynthesizerAura.entityTarget != null) {
                ((EntityLivingBase)this.renderViewEntity).rotationYawHead = ((EntityPlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
                ((EntityLivingBase)this.renderViewEntity).renderYawOffset = ((EntityPlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
            }
        }
    }
    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectConstructor(GameConfiguration p_i45547_1_, CallbackInfo ci) {
        Via.init();
        try {
            Antimony.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}