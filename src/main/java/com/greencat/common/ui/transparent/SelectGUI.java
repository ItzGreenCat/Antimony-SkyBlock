package com.greencat.common.ui.transparent;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.common.storage.SelectGUIStorage;
import com.greencat.type.SelectObject;
import com.greencat.type.SelectTable;
import com.greencat.utils.Blur;
import com.greencat.utils.FontManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SelectGUI {
    Minecraft mc = Minecraft.getMinecraft();
    public void draw(){
        int height = (Integer) getConfigByFunctionName.get("HUD","HUDHeight") + 20;
        int width = 5;
        FontManager.GothamRoundedFont.drawSmoothString("",0,0,0xFFFFFF);
        ResourceLocation logo = new ResourceLocation(Antimony.MODID, "logo.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(logo);
        Gui.drawModalRectWithCustomSizedTexture(width,(Integer)getConfigByFunctionName.get("HUD","HUDHeight"),0,0,20,20,20,20);
        FontManager.ExpressaSerialBigFont.drawSmoothString("antimony",width + 23, (float) ((Integer)getConfigByFunctionName.get("HUD","HUDHeight") + ((FontManager.GothamRoundedFont.getHeight() * 1.0) / 2)),0xFFFFFF);
        for(SelectTable table : SelectGUIStorage.TableStorage){
            if(table.getID().equals(Antimony.PresentGUI)){
                for(SelectObject object : table.getList()){
                    ResourceLocation resourceLocation = new ResourceLocation(Antimony.MODID, "hud2.png");
                    Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
                    if(object.getName().equals(Antimony.PresentFunction)) {
                        Gui.drawModalRectWithCustomSizedTexture(width,height,0,14,FunctionManager.getLongestTextWidthAdd20(),14,FunctionManager.getLongestTextWidthAdd20() - 15,28);
                        FontManager.GothamRoundedFont.drawSmoothString(object.getName(),width + 10,height + 4,0xFFFFFF);
                    } else {
                        Gui.drawModalRectWithCustomSizedTexture(width,height,0,0,FunctionManager.getLongestTextWidthAdd20(),14,FunctionManager.getLongestTextWidthAdd20() - 15,28);
                        FontManager.GothamRoundedFont.drawSmoothString(object.getName(),width + 3,height + 4,0xFFFFFF);
                    }
                    height = height + 14;
                }
            }
        }
    }
}
