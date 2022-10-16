package com.greencat.common.ui;

import com.greencat.Antimony;
import com.greencat.common.config.ConfigLoader;
import com.greencat.common.register.AntimonyRegister;
import com.greencat.type.AntimonyFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ClassicFunctionList {
    Minecraft mc = Minecraft.getMinecraft();

    public void draw() {
        int height = ConfigLoader.FunctionListHeight();
        for (AntimonyFunction function : AntimonyRegister.FunctionList) {
            if(function.getStatus()) {
                mc.fontRendererObj.drawString(function.getName(), new ScaledResolution(mc).getScaledWidth() - mc.fontRendererObj.getStringWidth(function.getName()), height, Antimony.Color);
                height = height + 10;
            }
        }
    }
}
