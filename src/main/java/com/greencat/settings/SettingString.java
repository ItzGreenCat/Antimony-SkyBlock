package com.greencat.settings;

import com.greencat.common.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class SettingString extends AbstractSettingOptionTextField {
    String DefaultValue;
    public SettingString(String name, String ID, String DefaultValue) {
        this.name = name;
        this.ID = ID;
        this.DefaultValue = DefaultValue;
    }
    @Override
    public void init(){
        this.setText(ConfigLoader.getString(parentFunction + "_" + ID,DefaultValue));
    }
    @Override
    public void update() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        Minecraft.getMinecraft().fontRendererObj.drawString(name,(scaledResolution.getScaledWidth() / 2) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2),yPosition - 10,0xFFFFFF);
        this.drawTextBox();
    }

    @Override
    public Object getValue() {
        return ConfigLoader.getString(parentFunction + "_" + ID,DefaultValue);
    }

    @Override
    public void setValue() {
        ConfigLoader.setString(parentFunction + "_" + ID,this.getText(),DefaultValue);
    }
}
