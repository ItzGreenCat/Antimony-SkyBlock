package com.greencat.antimony.common.mixins;

import com.greencat.antimony.utils.StringFactory;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {
    @ModifyVariable(
            method = "renderStringAtPos",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0)
    public String renderStringAtPos(String str){
        return StringFactory.process(str);
    }
    @ModifyVariable(
            method = "getStringWidth",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0)
    public String getStringWidth(String str){
        return StringFactory.process(str);
    }
}
