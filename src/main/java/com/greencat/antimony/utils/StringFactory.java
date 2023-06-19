package com.greencat.antimony.utils;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import net.minecraft.client.Minecraft;

public class StringFactory {
    public static String process(String str){
        if(str != null) {
            String originalString = str;
            String temp = originalString;
            temp = temp.replace("Chum", "Cum").replace("chum", "cum").replace("CHUM", "CUM");
            temp = temp.replace("Beast", "Breast");
            temp = temp.replace("e z", "ez").replace("e/z", "ez");
            if (FunctionManager.getStatus("NickHider") && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getName() != null) {
                temp = temp.replace(Minecraft.getMinecraft().thePlayer.getName(), (String) ConfigInterface.get("NickHider", "name"));
            }
            return temp;
        } else {
            return str;
        }
    }
}
