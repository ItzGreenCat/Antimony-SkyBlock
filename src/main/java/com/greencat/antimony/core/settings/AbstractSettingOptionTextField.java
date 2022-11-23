package com.greencat.antimony.core.settings;

import com.greencat.antimony.common.mixins.GuiTextFieldAccessor;
import com.greencat.antimony.common.mixins.MixinC03PacketPlayer;
import com.greencat.antimony.common.mixins.MixinGuiTextField;
import com.greencat.antimony.utils.Chroma;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

import java.awt.*;
import java.lang.reflect.Field;
import java.rmi.UnexpectedException;

public abstract class AbstractSettingOptionTextField extends GuiTextField implements ISettingOption {
    public String name;
    public String ID;
    public String parentFunction;
    public int OriginalYPos;
    public int OriginalXPos;
    public int Excursion = 0;
    public boolean visible;
    public AbstractSettingOptionTextField() {
        super(0, Minecraft.getMinecraft().fontRendererObj,0,0,0,0);
        this.setFocused(false);
        this.setCanLoseFocus(true);
        visible = true;
    }
    public void setTextFieldID(int id) {
        ((GuiTextFieldAccessor)this).setID(id);
    }
    public void setX(int x){
        this.xPosition = x;
        OriginalXPos = x;
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
        /*if(this.isFocused()){
            int lvt_4_1_ = Minecraft.getMinecraft().fontRendererObj.getStringWidth(Minecraft.getMinecraft().fontRendererObj.trimStringToWidth(this.getText().substring(((MixinGuiTextField)this).getLineScrollOffset()), this.getWidth()));
            drawRect(this.xPosition,this.yPosition,this.xPosition + this.width,this.yPosition + this.height,new Color(0,0,0,30).getRGB());
            drawRect(this.xPosition,this.height + this.yPosition,this.xPosition + this.width,this.height + this.yPosition + 1,new Color(23,135,183).getRGB());
            Minecraft.getMinecraft().fontRendererObj.drawString(this.getText(),this.xPosition,this.yPosition - (this.height / 2) - 5,new Color(255,255,255).getRGB());
            drawRect(this.xPosition + lvt_4_1_,this.yPosition,this.xPosition + lvt_4_1_ + 1,this.yPosition + this.height, Chroma.color.getRGB());
        } else {
            drawRect(this.xPosition,this.height + this.yPosition,this.xPosition + this.width,this.height + this.yPosition + 1,new Color(0,0,0).getRGB());
            Minecraft.getMinecraft().fontRendererObj.drawString(this.getText(),this.xPosition,this.yPosition - (this.height / 2) - 5,new Color(155,155,155).getRGB());
        }*/
        if(visible) {
            super.drawTextBox();
        }
    }
    public void setVisible(Boolean visible) {
        ((MixinGuiTextField)this).setVisible(visible);
    }
}
