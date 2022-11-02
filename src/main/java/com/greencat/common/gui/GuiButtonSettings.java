package com.greencat.common.gui;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonSettings extends GuiButton {
    public int OriginalYPos;
    public int Excursion = 0;
    public GuiButtonSettings(int p_i1041_1_, int p_i1041_2_, int y) {
        super(p_i1041_1_, p_i1041_2_, y, 18, 18, "");
        OriginalYPos = y;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.yPosition = OriginalYPos + this.Excursion;
        if (this.visible) {
            mc.getTextureManager().bindTexture(new ResourceLocation(Antimony.MODID,"MainMenu/settings.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0,this.width,this.height,this.width,this.height);
        }
    }
}
