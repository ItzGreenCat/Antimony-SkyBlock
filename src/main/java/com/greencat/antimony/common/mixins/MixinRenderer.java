package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public abstract class MixinRenderer {
    @Shadow private double cameraPitch;

    @Shadow private double cameraYaw;

    @Inject(method = {"hurtCameraEffect"},
            at = {@At("HEAD")},
            cancellable = true)
    public void noHurtCam(float effect,CallbackInfo cbi) {
        if(FunctionManager.getStatus("Camera") && (Boolean) ConfigInterface.get("Camera","noHurtCamera")) {
            cbi.cancel();
        }
    }
}
