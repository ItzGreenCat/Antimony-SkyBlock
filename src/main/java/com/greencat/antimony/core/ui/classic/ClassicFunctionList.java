package com.greencat.antimony.core.ui.classic;

import com.greencat.Antimony;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.type.AntimonyFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ClassicFunctionList {
    Minecraft mc = Minecraft.getMinecraft();

    public void draw() {
        int height = (Integer) ConfigInterface.get("HUD", "FunctionListHeight");
        try {
            for (AntimonyFunction function : AntimonyRegister.enabledList) {
                if (function.getStatus()) {
                    mc.fontRendererObj.drawString(function.getName(), new ScaledResolution(mc).getScaledWidth() - mc.fontRendererObj.getStringWidth(function.getName()), height, Antimony.Color.AntimonyCyan.getRGB());
                    height = height + 10;
                }
            }
        } catch (Exception ignored) {

        }
    }
}
