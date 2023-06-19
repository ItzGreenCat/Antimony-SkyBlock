package com.greencat.antimony.common.mixins;

import com.greencat.antimony.common.function.UngrabMouse;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.util.MouseHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHelper.class)
public class MixinMouseHelper {

    @Inject(method = "mouseXYChange",at = @At("HEAD"),cancellable = true)
    public void modifyMouseXYChange(CallbackInfo ci){
        if(FunctionManager.getStatus("UngrabMouse") && UngrabMouse.isUngrabbed){
            ci.cancel();
        }
    }
    @Inject(method = "grabMouseCursor",at = @At("HEAD"),cancellable = true)
    public void modifyGrabMouseCursor(CallbackInfo ci){
        if(FunctionManager.getStatus("UngrabMouse") && UngrabMouse.isUngrabbed){
            UngrabMouse.doesGameWantUngrab = false;
            ci.cancel();
        }
    }
    @Inject(method = "ungrabMouseCursor",at = @At("HEAD"),cancellable = true)
    public void modifyUngrabMouseCursor(CallbackInfo ci){
        if(FunctionManager.getStatus("UngrabMouse") && UngrabMouse.isUngrabbed){
            UngrabMouse.doesGameWantUngrab = true;
            ci.cancel();
        }
    }
}
