package com.greencat.antimony.core.settings;

import net.minecraft.client.gui.GuiButton;

public abstract class AbstractSettingOptionButton extends GuiButton implements ISettingOption {
    public String name;
    public String ID;
    public String parentFunction;
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
    }
    public void setWidth(int w){
        this.width = w;
    }
    public void setHeight(int h){
        this.height = h;
    }
}
