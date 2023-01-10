package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Vec3;
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
        if(FunctionManager.getStatus("Camera") && (Boolean) getConfigByFunctionName.get("Camera","noHurtCamera")) {
            cbi.cancel();
        }
    }
}
