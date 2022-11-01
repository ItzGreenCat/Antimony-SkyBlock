package com.greencat.settings;

import com.greencat.common.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class SettingBoolean extends AbstractSettingOptionButton {
    Boolean DefaultValue;
    public SettingBoolean(String name,String ID,Boolean DefaultValue) {
        this.name = name;
        this.ID = ID;
        this.DefaultValue = DefaultValue;
    }
    @Override
    public void update(){
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        Minecraft.getMinecraft().fontRendererObj.drawString(name,(scaledResolution.getScaledWidth() / 2) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2),yPosition - 10,0xFFFFFF);
        this.displayString = ConfigLoader.getBoolean(parentFunction + "_" + ID,DefaultValue) ? "On" : "Off";
    }

    @Override
    public Object getValue() {
        return ConfigLoader.getBoolean(parentFunction + "_" + ID,DefaultValue);
    }
    public void switchStatus(){
        ConfigLoader.setBoolean(parentFunction + "_" + ID,!ConfigLoader.getBoolean(parentFunction + "_" + ID,DefaultValue),DefaultValue);
    }

}
