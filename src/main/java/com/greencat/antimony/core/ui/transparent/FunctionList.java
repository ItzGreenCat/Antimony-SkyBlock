package com.greencat.antimony.core.ui.transparent;

import com.greencat.Antimony;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.utils.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class FunctionList {
    Minecraft mc = Minecraft.getMinecraft();

    public void draw() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        FontManager.GothamRoundedFont.drawString("",0,0,0xFFFFFF);
        int height = (Integer) getConfigByFunctionName.get("HUD","FunctionListHeight");

        ResourceLocation resourceLocation = new ResourceLocation(Antimony.MODID, "hud2.png");
        for (AntimonyFunction function : AntimonyRegister.enabledList) {
            if(function.getStatus()) {
                Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
                Gui.drawModalRectWithCustomSizedTexture((int) (scaledResolution.getScaledWidth() - FontManager.GothamRoundedFont.getStringWidth(function.getName()) - 6),height,0,0, (int) FontManager.GothamRoundedFont.getStringWidth(function.getName()) + 6,14, (float) FontManager.GothamRoundedFont.getStringWidth(function.getName()) + 6,28);
                FontManager.GothamRoundedFont.drawString(function.getName(),scaledResolution.getScaledWidth() - FontManager.GothamRoundedFont.getStringWidth(function.getName()) - 3,height + 5,0xFFFFFF);
                height = height + 14;
            }
        }
    }
}
