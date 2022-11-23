package com.greencat.antimony.core.gui;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiButtonSettings extends GuiButton {
    public int OriginalYPos;
    public int OriginalXPos;
    public int Excursion = 0;
    public GuiButtonSettings(int p_i1041_1_, int x, int y) {
        super(p_i1041_1_, x, y, 18, 18, "");
        OriginalYPos = y;
        OriginalXPos = x;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.yPosition = OriginalYPos + this.Excursion;
        if (this.visible) {
            boolean isCoveredByMouse = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            mc.getTextureManager().bindTexture(new ResourceLocation(Antimony.MODID,"clickgui/settings.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0,this.width,this.height,this.width,this.height);
            if(isCoveredByMouse){
                drawRect(this.xPosition,this.yPosition,this.xPosition + this.width,this.yPosition + this.height,new Color(0,0,0,30).getRGB());
            }
        }
    }
}
