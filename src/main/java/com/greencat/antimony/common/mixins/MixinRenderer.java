package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public abstract class MixinRenderer {
    @Inject(method = {"hurtCameraEffect"},
            at = {@At("HEAD")},
            cancellable = true)
    public void noHurtCam(float effect,CallbackInfo cbi) {
        if(FunctionManager.getStatus("Camera") && (Boolean) getConfigByFunctionName.get("Camera","noHurtCamera")) {
            cbi.cancel();
        }
    }


}
