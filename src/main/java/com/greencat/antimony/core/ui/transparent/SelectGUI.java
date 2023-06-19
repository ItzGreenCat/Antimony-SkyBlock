package com.greencat.antimony.core.ui.transparent;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.utils.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class SelectGUI {
    Minecraft mc = Minecraft.getMinecraft();
    public void draw(){
        int height = (Integer) ConfigInterface.get("HUD","HUDHeight") + 20;
        int width = 5;
        FontManager.GothamRoundedFont.drawString("",0,0,0xFFFFFF);
        ResourceLocation logo = new ResourceLocation(Antimony.MODID, "logo.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(logo);
        Gui.drawModalRectWithCustomSizedTexture(width,(Integer) ConfigInterface.get("HUD","HUDHeight"),0,0,20,20,20,20);
        FontManager.ExpressaSerialBigFont.drawString("antimony",width + 22, (float) ((Integer) ConfigInterface.get("HUD","HUDHeight") + ((FontManager.GothamRoundedFont.getHeight() * 1.0) / 2) - 5),0xFFFFFF);
        for(SelectTable table : SelectGUIStorage.TableStorage){
            if(table.getID().equals(Antimony.PresentGUI)){
                for(SelectObject object : table.getList()){
                    ResourceLocation resourceLocation = new ResourceLocation(Antimony.MODID, "hud2.png");
                    Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
                    if(object.getName().equals(Antimony.PresentFunction)) {
                        Gui.drawModalRectWithCustomSizedTexture(width,height,0,14,FunctionManager.getLongestTextWidthAdd20(),14,FunctionManager.getLongestTextWidthAdd20() - 15,28);
                        FontManager.GothamRoundedFont.drawString(object.getName(),width + 10,height + 4,0xFFFFFF);
                    } else {
                        Gui.drawModalRectWithCustomSizedTexture(width,height,0,0,FunctionManager.getLongestTextWidthAdd20(),14,FunctionManager.getLongestTextWidthAdd20() - 15,28);
                        FontManager.GothamRoundedFont.drawString(object.getName(),width + 3,height + 4,0xFFFFFF);
                    }
                    height = height + 14;
                }
            }
        }
    }
}
