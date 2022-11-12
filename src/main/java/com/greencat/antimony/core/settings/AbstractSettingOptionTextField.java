package com.greencat.antimony.core.settings;

import com.greencat.antimony.common.mixins.GuiTextFieldAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public abstract class AbstractSettingOptionTextField extends GuiTextField implements ISettingOption {
    public String name;
    public String ID;
    public String parentFunction;
    public int OriginalYPos;
    public int Excursion = 0;
    public AbstractSettingOptionTextField() {
        super(0, Minecraft.getMinecraft().fontRendererObj,0,0,0,0);
        this.setFocused(false);
        this.setCanLoseFocus(true);
    }
    public void setTextFieldID(int id) {
        ((GuiTextFieldAccessor)this).setID(id);
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
    public abstract void setValue();
    public abstract void init();
    public void drawTextBox(){
        this.yPosition = this.OriginalYPos + this.Excursion;
        super.drawTextBox();
    }
}
