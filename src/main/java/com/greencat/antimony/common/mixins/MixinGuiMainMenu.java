package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.core.via.protocol.ProtocolCollection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    float colorAngle = 0;
    @Inject(
            method = "drawScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V"
            ),
            cancellable = true)
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci){
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/CustomUI/background.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(0,0,0,0,scaledResolution.getScaledWidth(),scaledResolution.getScaledHeight(),scaledResolution.getScaledWidth(),scaledResolution.getScaledHeight());
        int Scaling;
        if(Minecraft.getMinecraft().gameSettings.guiScale != 0) {
            Scaling = (5 - Minecraft.getMinecraft().gameSettings.guiScale) * 2;
        } else {
            Scaling = 4 * 2;
        }
        if(colorAngle + 0.001 > 1F){
            colorAngle = 0;
        } else {
            colorAngle = colorAngle + 0.001F;
        }
        Color color = Color.getHSBColor(colorAngle,1F,1F);
        GlStateManager.scale(Scaling,Scaling,1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().fontRendererObj.drawString("Antimony",((scaledResolution.getScaledWidth() / 2) - ((Minecraft.getMinecraft().fontRendererObj.getStringWidth("Antimony") * Scaling) / 2)) / Scaling,(scaledResolution.getScaledHeight() / 4) / Scaling,color.getRGB());
        GlStateManager.scale(1.0 / Scaling,1.0 / Scaling,1.0F);
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        ci.cancel();
    }
}
