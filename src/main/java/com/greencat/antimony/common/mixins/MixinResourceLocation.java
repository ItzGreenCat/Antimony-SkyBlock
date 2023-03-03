package com.greencat.antimony.common.mixins;

import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourceLocation.class)
public class MixinResourceLocation {
    @Shadow @Final protected String resourcePath;

    @Shadow @Final protected String resourceDomain;

    @Inject(method = "getResourcePath",at = @At("HEAD"),cancellable = true)
    public void modify(CallbackInfoReturnable<String> cir){
        if(this.resourcePath != null && this.resourcePath.equals("textures/gui/forge.gif")){
            cir.setReturnValue("textures/loading.gif");
        }
    }
    @Inject(method = "getResourceDomain",at = @At("HEAD"),cancellable = true)
    public void modifyDomain(CallbackInfoReturnable<String> cir){
        if(this.resourcePath != null && this.resourcePath.equals("textures/gui/forge.gif") && this.resourceDomain != null && this.resourceDomain.equals("fml")){
            cir.setReturnValue("minecraft");
        }
    }
}
