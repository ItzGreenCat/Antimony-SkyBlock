package com.greencat.common.MainMenu;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

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
            if(isCoveredByMouse) {
                GlStateManager.color(0.7F, 0.7F, 0.7F, 1.0F);
                Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0,this.width,this.height,this.width,this.height);
                Minecraft.getMinecraft().fontRendererObj.drawString(this.displayString,this.xPosition + (this.width / 2) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.displayString) / 2),this.yPosition + this.height + 3,0xFFFFFF);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0,this.width,this.height,this.width,this.height);
            }
        }
    }
}
