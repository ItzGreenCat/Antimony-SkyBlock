package com.greencat.antimony.core.ui.white;

import com.greencat.Antimony;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.type.AntimonyFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class NewFunctionList {
    Minecraft mc = Minecraft.getMinecraft();

    public void draw() {
        int height = (Integer) ConfigInterface.get("HUD","FunctionListHeight");
        try {
            for (AntimonyFunction function : AntimonyRegister.enabledList) {
                if (function.getStatus()) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F);
                    ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "GuiFunctionBackground.png");
                    Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                    Gui.drawModalRectWithCustomSizedTexture(new ScaledResolution(mc).getScaledWidth() - mc.fontRendererObj.getStringWidth(function.getName()) - 10, height, 0, 0, mc.fontRendererObj.getStringWidth(function.getName()) + 10, 10, 16, 16);
                    mc.fontRendererObj.drawString(function.getName(), new ScaledResolution(mc).getScaledWidth() - mc.fontRendererObj.getStringWidth(function.getName()) - 5, height + 1, 0);
                    GlStateManager.color(1.0F, 1.0F, 1.0F);
                    ResourceLocation resourceLocation2 = new ResourceLocation(Antimony.MODID, "FunctionSplit.png");
                    Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation2);
                    Gui.drawModalRectWithCustomSizedTexture(new ScaledResolution(mc).getScaledWidth() - mc.fontRendererObj.getStringWidth(function.getName()) - 10, height + 10, 0, 0, mc.fontRendererObj.getStringWidth(function.getName()) + 10, 2, 16, 16);
                    height = height + 12;
                }
            }
        } catch(Exception ignored){

        }
    }
}
