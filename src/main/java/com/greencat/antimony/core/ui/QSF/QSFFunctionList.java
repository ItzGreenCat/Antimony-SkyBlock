package com.greencat.antimony.core.ui.QSF;


import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.utils.Blur;
import com.greencat.antimony.utils.FontManager;
import com.greencat.antimony.utils.GaussianBlur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import static com.greencat.antimony.utils.render.Scissor.*;

import java.util.HashMap;
import java.util.Map;

public class QSFFunctionList {
    Minecraft mc = Minecraft.getMinecraft();
    static HashMap<String,Integer> enabledFunctions = new HashMap<>();
    public void draw() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int height = (Integer) getConfigByFunctionName.get("HUD","FunctionListHeight");
        for (AntimonyFunction function : AntimonyRegister.FunctionList) {
            if(function.getStatus()) {
                GlStateManager.pushMatrix();
                Blur.renderBlur(scaledResolution.getScaledWidth() - FontManager.QuicksandBoldFont.getStringWidth(function.getName()) - 6,height,FontManager.QuicksandBoldFont.getStringWidth(function.getName()) + 6,13,10);
                GlStateManager.popMatrix();
                FontManager.QuicksandBoldFont.drawString(function.getName(), scaledResolution.getScaledWidth() - FontManager.QuicksandBoldFont.getStringWidth(function.getName()) - 3, height + 5, 0xFFFFFF);
                height = height + 13;
            }
        }
    }
}
