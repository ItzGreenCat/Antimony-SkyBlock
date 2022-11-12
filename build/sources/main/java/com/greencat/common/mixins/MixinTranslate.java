package com.greencat.antimony.common.mixins;

import com.greencat.antimony.common.function.ItemTranslate;
import com.greencat.antimony.utils.Utils;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ItemStack.class})
public abstract class MixinTranslate {
    @Inject(method={"getDisplayName"}, cancellable=true,at={@At(value="RETURN")})
    public void Translate(CallbackInfoReturnable<String> cir){
        String OriginalName = cir.getReturnValue();
        ItemTranslate translate = new ItemTranslate();
        cir.setReturnValue(translate.modifyName(OriginalName));
    }
}
