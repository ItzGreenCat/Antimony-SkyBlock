package com.greencat.antimony.common.mixins;

import com.greencat.antimony.common.function.CustomItemName;
import com.greencat.antimony.utils.Utils;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ItemStack.class})
public abstract class MixinItemStack {
    @Inject(method={"getDisplayName"}, cancellable=true,at={@At(value="RETURN")})
    public void Translate(CallbackInfoReturnable<String> cir){
        if(CustomItemName.hasCustomName(Utils.getUUIDForItem((ItemStack)(Object)this))) {
            cir.setReturnValue(CustomItemName.getCustomName(Utils.getUUIDForItem((ItemStack) (Object) this)));
        }
        /*if(!CustomItemName.hasCustomName(Utils.getUUIDForItem((ItemStack)(Object)this))) {
            String OriginalName = cir.getReturnValue();
            ItemTranslate translate = new ItemTranslate();
            cir.setReturnValue(translate.modifyName(OriginalName));
        } else {
            cir.setReturnValue(CustomItemName.getCustomName(Utils.getUUIDForItem((ItemStack)(Object)this)));
        }*/
    }
}
