package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.CustomSizeBackground;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static com.greencat.antimony.core.CustomSizeBackground.animation;

@Mixin(GuiSlot.class)
public abstract class MixinGuiSlot {
    @Shadow protected abstract void drawBackground();

    @Shadow protected abstract void func_148142_b(int p_148142_1_, int p_148142_2_);

    @Shadow public int width;

    @Inject(method = "drawContainerBackground",at = @At("HEAD"),remap = false,cancellable = true)
    public void modifyRender(Tessellator p_drawContainerBackground_1_, CallbackInfo ci){
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        drawBackground();
        Gui.drawRect(0,0,sr.getScaledWidth(),sr.getScaledHeight(),new Color(224,224,224,190).getRGB());
        ci.cancel();
    }
    @Inject(method = "overlayBackground",at = @At("HEAD"),cancellable = true)
    public void modifyRender(int p_overlayBackground_1_, int p_overlayBackground_2_, int p_overlayBackground_3_, int p_overlayBackground_4_, CallbackInfo ci){
        CustomSizeBackground.y = p_overlayBackground_1_;
        CustomSizeBackground.h = p_overlayBackground_2_ - p_overlayBackground_1_;
        CustomSizeBackground.x = 0;
        CustomSizeBackground.w = this.width;
        GlStateManager.pushMatrix();
        GlStateManager.color(0.75F,0.75F,0.75F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/CustomUI/wave/frames_" + (int)(animation.xCoord) + ".png"));
        Gui.drawModalRectWithCustomSizedTexture(CustomSizeBackground.x,CustomSizeBackground.y, CustomSizeBackground.x,CustomSizeBackground.y, CustomSizeBackground.w, CustomSizeBackground.h, this.width, this.width * 0.5625F);
        GlStateManager.popMatrix();
        ci.cancel();
    }
    @Inject(method = "drawScreen",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/WorldRenderer;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V",ordinal = 0),cancellable = true)
    public void modifyDrawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci){
        this.func_148142_b(p_drawScreen_1_, p_drawScreen_2_);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        ci.cancel();
    }
}
