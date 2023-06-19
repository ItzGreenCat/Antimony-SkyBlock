package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.greencat.antimony.core.config.ConfigInterface.get;

@Mixin(ModelBiped.class)
public class MixinModelBiped extends ModelBase {
    @Inject(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V",ordinal = 7))
    public void renderHeadPre(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_, float p_render_6_, float p_render_7_, CallbackInfo ci){
        if(FunctionManager.getStatus("Giant") && p_render_1_ instanceof AbstractClientPlayer) {
            double scaleValue = (Double) get("Giant", "headScale");
            GlStateManager.scale(scaleValue, scaleValue, scaleValue);
        }
    }
    @Inject(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V",ordinal = 13))
    public void renderHeadPre2(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_, float p_render_6_, float p_render_7_, CallbackInfo ci){
        if(FunctionManager.getStatus("Giant") && p_render_1_ instanceof AbstractClientPlayer) {
            double scaleValue = (Double) get("Giant", "headScale");
            GlStateManager.scale(scaleValue, scaleValue, scaleValue);
        }
    }
    @Inject(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V",ordinal = 7,shift = At.Shift.AFTER))
    public void renderHeadPost(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_, float p_render_6_, float p_render_7_, CallbackInfo ci){
        if(FunctionManager.getStatus("Giant") && p_render_1_ instanceof AbstractClientPlayer) {
            double scaleValue = 1 / (Double) get("Giant", "headScale");
            GlStateManager.scale(scaleValue, scaleValue, scaleValue);
        }
    }
    @Inject(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V",ordinal = 13,shift = At.Shift.AFTER))
    public void renderHeadPost2(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_, float p_render_6_, float p_render_7_, CallbackInfo ci){
        if(FunctionManager.getStatus("Giant") && p_render_1_ instanceof AbstractClientPlayer) {
            double scaleValue = 1 / (Double) get("Giant", "headScale");
            GlStateManager.scale(scaleValue, scaleValue, scaleValue);
        }
    }
}
