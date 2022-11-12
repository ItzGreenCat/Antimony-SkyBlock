package com.greencat.antimony.core.gui;

import net.minecraft.client.Minecraft;

public class GuiTableButton extends GuiClickGUIButton{
    public GuiTableButton(int p_i46323_1_, int x, int y, int p_i46323_4_, int p_i46323_5_, String p_i46323_6_) {
        super(p_i46323_1_, x, y, p_i46323_4_, p_i46323_5_, p_i46323_6_);
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.yPosition = this.OriginalYPos + this.Excursion;
        super.drawButton(mc, mouseX, mouseY);
    }
}
