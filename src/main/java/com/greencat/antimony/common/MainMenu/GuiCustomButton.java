package com.greencat.antimony.common.MainMenu;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiCustomButton extends GuiButton {
    String IconLocation;
    public GuiCustomButton(int id, int x, int y, int w, int h,String str,String Icon) {
        super(id,x,y,w,h,str);
        IconLocation = Icon;
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(new ResourceLocation(Antimony.MODID,"MainMenu/" + IconLocation + ".png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean isCoveredByMouse = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            if(!isCoveredByMouse) {
                new Color(121, 121, 121);
                GlStateManager.color(0.7F, 0.7F, 0.7F, 1.0F);
                Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0,this.width,this.height,this.width,this.height);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                Gui.drawRect(this.xPosition,this.yPosition,this.xPosition + this.width,this.yPosition + this.height,new Color(140, 140, 140,120).getRGB());
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0,this.width,this.height,this.width,this.height);
            }
        }
    }
}
