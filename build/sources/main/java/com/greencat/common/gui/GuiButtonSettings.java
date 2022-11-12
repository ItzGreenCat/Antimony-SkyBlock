package com.greencat.antimony.core.gui;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonSettings extends GuiButton {
    public GuiButtonSettings(int p_i1041_1_, int p_i1041_2_, int p_i1041_3_) {
        super(p_i1041_1_, p_i1041_2_, p_i1041_3_, 18, 18, "");
    }

    public void drawButton(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_) {
        if (this.visible) {
            p_drawButton_1_.getTextureManager().bindTexture(new ResourceLocation(Antimony.MODID,"setting.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0,18,18,18,18);
        }
    }
}
