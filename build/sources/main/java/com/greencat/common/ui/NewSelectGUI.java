package com.greencat.antimony.core.ui;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class NewSelectGUI {
    Minecraft mc = Minecraft.getMinecraft();
    public void draw(){



        int height = ConfigLoader.GuiHeight() + 25;
        int width = 0;
        GlStateManager.color(1.0F,1.0F,1.0F);
        ResourceLocation resourceLocation = new ResourceLocation(Antimony.MODID, "GuiHead.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(0,ConfigLoader.GuiHeight(),0,0,FunctionManager.getLongestTextWidthAdd20(),25,16,16);
        ResourceLocation resourceLocation2 = new ResourceLocation(Antimony.MODID, "GuiHeadText.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation2);
        Gui.drawModalRectWithCustomSizedTexture(0,ConfigLoader.GuiHeight(),0,0,100,25,100,25);
        //mc.fontRendererObj.drawString("Antimony",0, ConfigLoader.GuiHeight(), Antimony.Color);
        for(SelectTable table : SelectGUIStorage.TableStorage){
            if(table.getID().equals(Antimony.PresentGUI)){
                for(SelectObject object : table.getList()){
                    if(object.getName().equals(Antimony.PresentFunction)) {
                        GlStateManager.color(1.0F,1.0F,1.0F);
                        ResourceLocation resourceLocation3 = new ResourceLocation(Antimony.MODID, "GuiFunctionBackground.png");
                        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation3);
                        Gui.drawModalRectWithCustomSizedTexture(0,height,0,0,FunctionManager.getLongestTextWidthAdd20(),14,16,16);

                        ResourceLocation resourceLocation4 = new ResourceLocation(Antimony.MODID, "FunctionSplit.png");
                        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation4);
                        Gui.drawModalRectWithCustomSizedTexture(0,height + 14,0,0,FunctionManager.getLongestTextWidthAdd20(),2,16,16);
                        if(object.getType().equals("function")) {
                            if (FunctionManager.getStatus(object.getName())) {
                                ResourceLocation resourceLocation5 = new ResourceLocation(Antimony.MODID, "GreenPoint.png");
                                Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation5);
                                Gui.drawModalRectWithCustomSizedTexture(10 / 2 - ((10 / 2) / 2),height + (14 / 2) - (5 / 2),0,0,5,5,5,5);
                            } else {
                                ResourceLocation resourceLocation6 = new ResourceLocation(Antimony.MODID, "RedPoint.png");
                                Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation6);
                                Gui.drawModalRectWithCustomSizedTexture(10 / 2 - ((10 / 2) / 2),height + (14 / 2) - (5 / 2),0,0,5,5,5,5);
                            }
                        } else {
                            ResourceLocation resourceLocation7 = new ResourceLocation(Antimony.MODID, "BluePoint.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation7);
                            Gui.drawModalRectWithCustomSizedTexture(10 / 2 - ((10 / 2) / 2),height + (14 / 2) - (5 / 2),0,0,5,5,5,5);
                        }

                        ResourceLocation resourceLocation8 = new ResourceLocation(Antimony.MODID, "GuiHead.png");
                        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation8);
                        Gui.drawModalRectWithCustomSizedTexture(FunctionManager.getLongestTextWidthAdd20() - 10,height + (14 / 2) - (5 / 2),0,0,5,5,5,5);

                        String selectObjectName = object.getName();
                        mc.fontRendererObj.drawString(selectObjectName, width + 10, height + (14 / 2) - (5 / 2), 0);
                    } else {
                        GlStateManager.color(1.0F,1.0F,1.0F);
                        ResourceLocation resourceLocation3 = new ResourceLocation(Antimony.MODID, "GuiFunctionBackground.png");
                        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation3);
                        Gui.drawModalRectWithCustomSizedTexture(0,height,0,0,FunctionManager.getLongestTextWidthAdd20(),14,16,16);

                        ResourceLocation resourceLocation4 = new ResourceLocation(Antimony.MODID, "FunctionSplit.png");
                        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation4);
                        Gui.drawModalRectWithCustomSizedTexture(0,height + 14,0,0,FunctionManager.getLongestTextWidthAdd20(),2,16,16);
                        if(object.getType().equals("function")) {
                            if (FunctionManager.getStatus(object.getName())) {
                                ResourceLocation resourceLocation5 = new ResourceLocation(Antimony.MODID, "GreenPoint.png");
                                Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation5);
                                Gui.drawModalRectWithCustomSizedTexture(10 / 2 - ((10 / 2) / 2),height + (14 / 2) - (5 / 2),0,0,5,5,5,5);
                            } else {
                                ResourceLocation resourceLocation6 = new ResourceLocation(Antimony.MODID, "RedPoint.png");
                                Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation6);
                                Gui.drawModalRectWithCustomSizedTexture(10 / 2 - ((10 / 2) / 2),height + (14 / 2) - (5 / 2),0,0,5,5,5,5);
                            }
                        } else {
                            ResourceLocation resourceLocation7 = new ResourceLocation(Antimony.MODID, "BluePoint.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation7);
                            Gui.drawModalRectWithCustomSizedTexture(10 / 2 - ((10 / 2) / 2),height + (14 / 2) - (5 / 2),0,0,5,5,5,5);
                        }

                        String selectObjectName = object.getName();
                        mc.fontRendererObj.drawString(selectObjectName, width + 10, height + (14 / 2) - (5 / 2), 0);
                    }
                    height = height + 16;
                }

            }
        }
    }
}
