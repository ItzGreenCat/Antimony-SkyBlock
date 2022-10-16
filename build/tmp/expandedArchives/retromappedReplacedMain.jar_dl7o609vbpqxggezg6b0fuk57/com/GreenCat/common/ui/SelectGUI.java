package com.GreenCat.common.ui;

import com.GreenCat.Antimony;
import com.GreenCat.common.config.ConfigLoader;
import com.GreenCat.common.storage.SelectGUIStorage;
import com.GreenCat.type.SelectObject;
import com.GreenCat.type.SelectTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class SelectGUI {
    Minecraft mc = Minecraft.func_71410_x();
    public static String PresentGUI = "root";
    public static String PresentFunction = "";
    public void draw(){



        int height = ConfigLoader.GuiHeight() + 10;
        int width = 0;
        mc.field_71466_p.func_78276_b("Antimony",0, ConfigLoader.GuiHeight(), Antimony.Color);
        for(SelectTable table : SelectGUIStorage.TableStorage){
            if(table.getID().equals(PresentGUI)){
                for(SelectObject object : table.getList()){
                    if(object.getName().equals(PresentFunction)) {
                        if(object.getType().equals("function")) {
                            String selectObjectName = "[功能]" + object.getName() + " <-";
                            /*final ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "hw.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                            GlStateManager.color(1.0F,1.0F,1.0F);
                            //Gui.drawModalRectWithCustomSizedTexture(width,height - 1,0,0,mc.fontRendererObj.getStringWidth(selectObjectName),10,1,1);
                            final ResourceLocation resourceLocation2 = new ResourceLocation(Antimony.MODID, "w.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation2);
                            Gui.drawModalRectWithCustomSizedTexture(0,height - 1,0,0,width,10,1,1);*/
                            mc.field_71466_p.func_78276_b(selectObjectName, width, height, Antimony.Color);
                        } else {
                            String selectObjectName = "[列表]" + object.getName() + " <-";
                            /*final ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "hw.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                            GlStateManager.color(1.0F,1.0F,1.0F);
                            //Gui.drawModalRectWithCustomSizedTexture(width,height - 1,0,0,mc.fontRendererObj.getStringWidth(selectObjectName),10,1,1);
                            final ResourceLocation resourceLocation2 = new ResourceLocation(Antimony.MODID, "w.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation2);
                            Gui.drawModalRectWithCustomSizedTexture(0,height - 1,0,0,width,10,1,1);*/
                            mc.field_71466_p.func_78276_b(selectObjectName, width, height, Antimony.Color);
                        }
                    } else {
                        if(object.getType().equals("function")) {
                            String selectObjectName = "[功能]" + object.getName();
                            /*final ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "hb.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                            GlStateManager.color(1.0F,1.0F,1.0F);
                            //Gui.drawModalRectWithCustomSizedTexture(width,height - 1,0,0,mc.fontRendererObj.getStringWidth(selectObjectName),10,1,1);
                            final ResourceLocation resourceLocation2 = new ResourceLocation(Antimony.MODID, "b.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation2);
                            Gui.drawModalRectWithCustomSizedTexture(0,height - 1,0,0,width,10,1,1);*/
                            mc.field_71466_p.func_78276_b(selectObjectName, width, height, Antimony.Color);
                        } else {
                            String selectObjectName = "[列表]" + object.getName();
                            /*final ResourceLocation resourceLocation1 = new ResourceLocation(Antimony.MODID, "hb.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
                            GlStateManager.color(1.0F,1.0F,1.0F);
                            //Gui.drawModalRectWithCustomSizedTexture(width,height - 1,0,0,mc.fontRendererObj.getStringWidth(selectObjectName),10,1,1);
                            final ResourceLocation resourceLocation2 = new ResourceLocation(Antimony.MODID, "b.png");
                            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation2);
                            Gui.drawModalRectWithCustomSizedTexture(0,height - 1,0,0,width,10,1,1);*/
                            mc.field_71466_p.func_78276_b(selectObjectName, width, height, Antimony.Color);
                        }
                    }
                    height = height + 10;
                }

            }
        }
    }
}
