package com.greencat.common.mixins;

import com.greencat.common.FunctionManager.FunctionManager;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public class MixinRenderer {
    @Inject(method = {"hurtCameraEffect"},
            at = {@At("HEAD")},
            cancellable = true)
    public void noHurtCam(float effect,CallbackInfo cbi) {
        if(FunctionManager.getStatus("NoHurtCam")) {
            cbi.cancel();
        }
    }


}
