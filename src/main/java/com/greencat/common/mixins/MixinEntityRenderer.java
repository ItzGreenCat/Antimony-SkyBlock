package com.greencat.common.mixins;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
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
}
