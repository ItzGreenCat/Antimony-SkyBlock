package com.greencat.antimony.core.ui.classic;

import com.greencat.Antimony;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.type.AntimonyFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ClassicFunctionList {
    Minecraft mc = Minecraft.getMinecraft();

    public void draw() {
        int height = (Integer) getConfigByFunctionName.get("HUD","FunctionListHeight");
        for (AntimonyFunction function : AntimonyRegister.FunctionList) {
            if(function.getStatus()) {
                mc.fontRendererObj.drawString(function.getName(), new ScaledResolution(mc).getScaledWidth() - mc.fontRendererObj.getStringWidth(function.getName()), height, Antimony.Color);
                height = height + 10;
            }
        }
    }
}
