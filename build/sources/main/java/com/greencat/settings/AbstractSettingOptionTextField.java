package com.greencat.antimony.core.settings;

import com.greencat.antimony.common.mixins.GuiTextFieldAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.lang.reflect.Field;

public abstract class AbstractSettingOptionTextField extends GuiTextField implements ISettingOption {
    public String name;
    public String ID;
    public String parentFunction;
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
    }
    public void setWidth(int w){
        this.width = w;
    }
    public void setHeight(int h){
        this.height = h;
    }
    public abstract void setValue();
    public abstract void init();
}
