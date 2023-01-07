package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @Shadow private float thirdPersonDistance;

    @Redirect(
            method = {"getMouseOver"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D",
                    ordinal = 2
            )
    )
    private double distanceTo(Vec3 instance, Vec3 vec) {
        if(FunctionManager.getStatus("Reach") && (instance.distanceTo(vec) <= (Double)getConfigByFunctionName.get("Reach","distance"))){
            return 2.9000000953674316D;
        } else {
            return instance.distanceTo(vec);
        }
    }
    @Redirect(
            method = {"orientCamera"},
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistanceTemp:F"
            )
    )
    public float thirdPersonDistanceTemp(EntityRenderer instance) {
        return FunctionManager.getStatus("Camera") ? ((Double)getConfigByFunctionName.get("Camera","distance")).floatValue() : this.thirdPersonDistance;
    }

    @Redirect(
            method = {"orientCamera"},
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistance:F"
            )
    )
    public float thirdPersonDistance(EntityRenderer instance) {
        return FunctionManager.getStatus("Camera") ? ((Double)getConfigByFunctionName.get("Camera","distance")).floatValue() : this.thirdPersonDistance;
    }
    @Redirect(
            method = {"orientCamera"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D"
            )
    )
    public double onCamera(Vec3 instance, Vec3 vec) {
        return FunctionManager.getStatus("Camera") && (Boolean)getConfigByFunctionName.get("Camera","clip") ? (Double)getConfigByFunctionName.get("Camera","distance") : instance.distanceTo(vec);
    }
}
