package com.greencat.antimony.core.settings;

import com.greencat.antimony.core.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SettingTypeSelector extends AbstractSettingOptionButton {
    int DefaultValue;
    HashMap<String,Integer> types;
    public SettingTypeSelector(String name, String ID, int DefaultValue, HashMap<String,Integer> types) {
        this.name = name;
        this.ID = ID;
        this.DefaultValue = DefaultValue;
        this.types = types;
    }
    @Override
    public void update(){
        if(this.visible) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            Minecraft.getMinecraft().fontRendererObj.drawString(name, this.xPosition + (this.width / 2) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2), yPosition - 10, 0xFFFFFF);
            int SavedInt = ConfigLoader.getInt(parentFunction + "_" + ID, DefaultValue);
            for (Map.Entry<String, Integer> entry : types.entrySet()) {
                if (entry.getValue() == SavedInt) {
                    this.displayString = entry.getKey();
                    break;
                }
            }
        }

    }

    @Override
    public Object getValue() {
        return ConfigLoader.getInt(parentFunction + "_" + ID,DefaultValue);
    }
    public String getKey() {
        int SavedInt = ConfigLoader.getInt(parentFunction + "_" + ID,DefaultValue);
        for(Map.Entry<String,Integer> entry : types.entrySet()){
            if(entry.getValue() == SavedInt){
                return entry.getKey();
            }
        }
        return "";
    }
    public void switchStatus(){
        Iterator<Map.Entry<String,Integer>> iterator = types.entrySet().iterator();
        while(iterator.hasNext()){
            if (iterator.next().getValue() == ConfigLoader.getInt(parentFunction + "_" + ID,DefaultValue)) {
                if(iterator.hasNext()) {
                    ConfigLoader.setInt(parentFunction + "_" + ID, iterator.next().getValue(), DefaultValue);
                    break;
                } else {
                    ConfigLoader.setInt(parentFunction + "_" + ID, (Integer) types.values().toArray()[0],DefaultValue);
                }
            }
        }
    }

}
