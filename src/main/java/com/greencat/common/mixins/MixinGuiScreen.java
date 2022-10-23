package com.greencat.common.mixins;

import com.greencat.utils.Blur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui {
    @Shadow
    public Minecraft mc;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Inject(
            method="drawDefaultBackground",
            at=@At("HEAD"),
            cancellable = true)
    public void defaultBackground(CallbackInfo cib){
        if (this.mc.theWorld != null) {
            Blur.renderBlur(0,0,this.width,this.height,20);
        } else {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/CustomUI/defaultBackground.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(0,0,0,0,scaledResolution.getScaledWidth(),scaledResolution.getScaledHeight(),scaledResolution.getScaledWidth(),scaledResolution.getScaledHeight());
        }
        cib.cancel();
    }
}
