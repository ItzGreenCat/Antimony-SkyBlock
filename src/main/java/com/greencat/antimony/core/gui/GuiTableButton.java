package com.greencat.antimony.core.gui;

import com.greencat.Antimony;
import com.greencat.antimony.utils.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiTableButton extends GuiClickGUIButton{
    public boolean type;
    public GuiTableButton(int p_i46323_1_, int x, int y, int p_i46323_4_, int p_i46323_5_, String p_i46323_6_,boolean type) {
        super(p_i46323_1_, x, y, p_i46323_4_, p_i46323_5_, p_i46323_6_);
        this.type = type;
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.yPosition = this.OriginalYPos + this.Excursion;
        if (this.visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean isCoveredByMouse = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            if(type) {
                Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID, "clickgui/function.png"));
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID, "clickgui/menu.png"));
            }
            int min = Math.min(this.width,this.height);
            drawModalRectWithCustomSizedTexture(this.xPosition + 3,this.yPosition,0,0,min,min,min,min);
            FontManager.QuicksandFont.drawString(this.displayString,this.xPosition + min + 5,this.yPosition + (this.height / 2.0F) - 2, new Color(0,0,0, 255).getRGB());
            if(isCoveredByMouse){
                drawRect(this.xPosition,this.yPosition,this.xPosition + this.width,this.yPosition + this.height,new Color(0,0,0,30).getRGB());
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
