package com.greencat.settings;

import com.greencat.common.gui.GuiClickGUIButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public abstract class AbstractSettingOptionButton extends GuiClickGUIButton implements ISettingOption {
    public String name;
    public String ID;
    public String parentFunction;
    public int OriginalYPos;
    public int Excursion = 0;
    public AbstractSettingOptionButton() {
        super(0, 0, 0, 0, 0,"");
    }
    public void setButtonID(int ButtonID){
        this.id = ButtonID;
    }
    public void setX(int x){
        this.xPosition = x;
    }
    public void setY(int y){
        this.yPosition = y;
        this.OriginalYPos = y;
    }
    public void setWidth(int w){
        this.width = w;
    }
    public void setHeight(int h){
        this.height = h;
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.yPosition = this.OriginalYPos + this.Excursion;
        super.drawButton(mc, mouseX, mouseY);
    }
}
