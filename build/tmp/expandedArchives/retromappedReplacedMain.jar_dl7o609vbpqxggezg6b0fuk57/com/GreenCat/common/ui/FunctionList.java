package com.GreenCat.common.ui;

import com.GreenCat.Antimony;
import com.GreenCat.common.config.ConfigLoader;
import com.GreenCat.common.register.GCARegister;
import com.GreenCat.type.AntimonyFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class FunctionList {
    Minecraft mc = Minecraft.func_71410_x();

    public void draw() {
        int height = ConfigLoader.FunctionListHeight();
        for (AntimonyFunction function : GCARegister.FunctionList) {
            if(function.getStatus()) {
                /*int randomNumber = function.color;
                if(randomNumber == 0){
                    final ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "aqua.png");
                    Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                    GlStateManager.color(1.0F,1.0F,1.0F);
                    Gui.drawModalRectWithCustomSizedTexture(new ScaledResolution(mc).getScaledWidth() - 6,height - 1,0,0,6,10,1,1);
                }
                if(randomNumber == 1){
                    final ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "gold.png");
                    Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                    GlStateManager.color(1.0F,1.0F,1.0F);
                    Gui.drawModalRectWithCustomSizedTexture(new ScaledResolution(mc).getScaledWidth() - 6,height - 1,0,0,6,10,1,1);
                }
                if(randomNumber == 2){
                    final ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "green.png");
                    Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                    GlStateManager.color(1.0F,1.0F,1.0F);
                    Gui.drawModalRectWithCustomSizedTexture(new ScaledResolution(mc).getScaledWidth() - 6,height - 1,0,0,6,10,1,1);
                }
                if(randomNumber == 3){
                    final ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "pink.png");
                    Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                    GlStateManager.color(1.0F,1.0F,1.0F);
                    Gui.drawModalRectWithCustomSizedTexture(new ScaledResolution(mc).getScaledWidth() - 6,height - 1,0,0,6,10,1,1);
                }*/
                mc.field_71466_p.func_78276_b(function.getName(), new ScaledResolution(mc).func_78326_a() - mc.field_71466_p.func_78256_a(function.getName()), height, Antimony.Color);
                height = height + 10;
            }
        }
    }
}
