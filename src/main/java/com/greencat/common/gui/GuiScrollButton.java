package com.greencat.common.gui;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiScrollButton extends GuiButton {
    String IconLocation1;
    String IconLocation2;
    public GuiScrollButton(int id, int x, int y, int w, int h, String Icon1,String Icon2) {
        super(id,x,y,w,h,"");
        IconLocation1 = Icon1;
        IconLocation2 = Icon2;
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean isCoveredByMouse = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            if(isCoveredByMouse) {
                mc.getTextureManager().bindTexture(new ResourceLocation(Antimony.MODID,"clickgui/" + IconLocation2 + ".png"));
            } else {
                mc.getTextureManager().bindTexture(new ResourceLocation(Antimony.MODID,"clickgui/" + IconLocation1 + ".png"));
            }
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0,this.width,this.height,this.width,this.height);
        }
    }
}
