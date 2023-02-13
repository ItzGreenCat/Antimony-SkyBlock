package com.greencat.antimony.common.mixins;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer extends ModelBiped {
    private ModelRenderer bipedGreenCatEar;
    private ModelRenderer bipedGreenCatEar2;
    @Inject(method = "<init>*",at = @At(value = "RETURN"))
    public void init(float p_i46304_1_, boolean p_i46304_2_, CallbackInfo ci){
        this.bipedGreenCatEar = new ModelRenderer(this, 24, 0);
        this.bipedGreenCatEar.addBox(-3.7F, -3.8F, -3.1F, 2, 2, 1);
        this.bipedGreenCatEar2 = new ModelRenderer(this, 30, 0);
        this.bipedGreenCatEar2.addBox(-7.3F, -3.8F, -3.1F, 2, 2, 1);
    }
    public void renderGreenCatEar(float f) {
        renderGreenCatEar1(f);
        renderGreenCatEar2(f);
    }
    public void renderGreenCatEar1(float f) {
        copyModelAngles(this.bipedHead, this.bipedGreenCatEar);
        this.bipedGreenCatEar.rotationPointX = 0.0F;
        this.bipedGreenCatEar.rotationPointY = 0.0F;
        this.bipedGreenCatEar.render(f);
    }
    public void renderGreenCatEar2(float f) {
        copyModelAngles(this.bipedHead, this.bipedGreenCatEar2);
        this.bipedGreenCatEar2.rotationPointX = 0.0F;
        this.bipedGreenCatEar2.rotationPointY = 0.0F;
        this.bipedGreenCatEar2.render(f);
    }
}
