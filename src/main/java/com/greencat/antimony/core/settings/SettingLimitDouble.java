package com.greencat.antimony.core.settings;

import com.greencat.antimony.core.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class SettingLimitDouble extends AbstractSettingOptionTextField {
    double DefaultValue;
    double max;
    double min;
    public SettingLimitDouble(String name, String ID, double DefaultValue,double MaxValue,double MinValue) {
        this.name = name;
        this.ID = ID;
        this.DefaultValue = DefaultValue;
        this.max = MaxValue;
        this.min = MinValue;
    }
    @Override
    public void init(){
        this.setText(String.valueOf(ConfigLoader.getDouble(parentFunction + "_" + ID,DefaultValue)));
    }
    @Override
    public void update() {
        if(this.visible) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            Minecraft.getMinecraft().fontRendererObj.drawString(name, this.xPosition + (this.width / 2) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2), yPosition - 10, 0xFFFFFF);
        }
        this.drawTextBox();
    }

    @Override
    public Object getValue() {
        return ConfigLoader.getDouble(parentFunction + "_" + ID,DefaultValue);
    }

    @Override
    public void setValue() {
        try {
            if(Double.parseDouble(this.getText()) > max) {
                ConfigLoader.setDouble(parentFunction + "_" + ID, max, DefaultValue);
            } else if(Double.parseDouble(this.getText()) < min) {
                ConfigLoader.setDouble(parentFunction + "_" + ID, min, DefaultValue);
            } else {
                ConfigLoader.setDouble(parentFunction + "_" + ID, Double.parseDouble(this.getText()), DefaultValue);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
