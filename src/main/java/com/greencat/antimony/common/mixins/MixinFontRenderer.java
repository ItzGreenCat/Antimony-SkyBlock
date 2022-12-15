package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {
    @ModifyVariable(
            method = "renderStringAtPos",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0)
    public String renderStringAtPos(String str){
        String originalString = str;
        String temp = originalString;
        temp = temp.replace("Chum","Cum").replace("chum","cum");
        if(FunctionManager.getStatus("NickHider") && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getName() != null){
            temp = temp.replace(Minecraft.getMinecraft().thePlayer.getName(),(String) getConfigByFunctionName.get("NickHider","name"));
        }
        return temp;
    }
}
