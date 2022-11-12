package com.greencat.antimony.core.settings;

import com.greencat.antimony.core.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class SettingInt extends AbstractSettingOptionTextField {
    int DefaultValue;
    public SettingInt(String name, String ID, int DefaultValue) {
        this.name = name;
        this.ID = ID;
        this.DefaultValue = DefaultValue;
    }
    @Override
    public void init(){
        this.setText(String.valueOf(ConfigLoader.getInt(parentFunction + "_" + ID,DefaultValue)));
    }
    @Override
    public void update() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        Minecraft.getMinecraft().fontRendererObj.drawString(name,(scaledResolution.getScaledWidth() / 2) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2),yPosition - 10,0xFFFFFF);
        this.drawTextBox();
    }

    @Override
    public Object getValue() {
        return ConfigLoader.getInt(parentFunction + "_" + ID,DefaultValue);
    }

    @Override
    public void setValue() {
        try {
            ConfigLoader.setInt(parentFunction + "_" + ID,Integer.parseInt(this.getText()), DefaultValue);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
