package com.greencat.common.gui;

import com.greencat.Antimony;
import com.greencat.utils.FontManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiClickGUIButton extends GuiButton {
    public int OriginalYPos;
    public int Excursion = 0;
    public GuiClickGUIButton(int p_i46323_1_, int x, int y, int p_i46323_4_, int p_i46323_5_, String p_i46323_6_) {
        super(p_i46323_1_,x,y, p_i46323_4_, p_i46323_5_, p_i46323_6_);
        OriginalYPos = y;
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean isCoveredByMouse = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            if(isCoveredByMouse) {
                Utils.drawRoundRect2(this.xPosition,this.yPosition,this.width,this.height,this.height / 4,new Color(1,168,254).getRGB());
            } else {
                Utils.drawRoundRect2(this.xPosition,this.yPosition,this.width,this.height,this.height / 4,new Color(250,250,250).getRGB());
            }
            FontManager.STXINWEIFont.drawSmoothString(this.displayString,this.xPosition + (this.width / 2) - (FontManager.STXINWEIFont.getStringWidth(this.displayString) / 2),this.yPosition + (this.height / 2), new Color(0,0,0, 255).getRGB());
        }
    }
}
