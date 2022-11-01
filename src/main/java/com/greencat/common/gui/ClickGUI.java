package com.greencat.common.gui;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.function.title.TitleManager;
import com.greencat.common.storage.SelectGUIStorage;
import com.greencat.type.SelectObject;
import com.greencat.type.SelectTable;
import com.greencat.utils.Blur;
import com.greencat.utils.Chroma;
import com.greencat.utils.FontManager;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClickGUI extends GuiScreen {
    private int index = 1;
    private int ButtonListHeight;
    String guiName;
    private GuiScreen parentScreen;
    private GuiButton BackButton;
    private GuiScrollButton upButton;
    private GuiScrollButton downButton;
    public int ButtonExcursion = 0;
    Blur blur = new Blur();
    HashMap<GuiButton, SelectObject> FunctionObjectMap = new HashMap<>();
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    public ClickGUI(GuiScreen parent,String guiName)
    {
        this.guiName = guiName;
        parentScreen = parent;
    }
    public void initGui(){
        index = 1;
        ButtonListHeight = 53;
        FunctionObjectMap.clear();
        for(SelectTable table : SelectGUIStorage.TableStorage){
            if(table.getID().equals(guiName)) {
                for(SelectObject object : table.getList()) {
                    GuiTableButton button;
                    if(object.getType().equals("function") && Objects.requireNonNull(FunctionManager.getFunctionByName(object.getName())).isConfigurable()) {
                        button = new GuiTableButton(index, 10 , ButtonListHeight, FunctionManager.getLongestTextWidthAdd20()  + 50 - 20, 18, object.getType().equals("function") ? "[Function] " + object.getName() : "[List] " + object.getName());
                        this.buttonList.add(new GuiButtonSettings(-index,10 + FunctionManager.getLongestTextWidthAdd20()  + 50 - 18, ButtonListHeight));
                    } else {
                        button = new GuiTableButton(index, 10, ButtonListHeight, FunctionManager.getLongestTextWidthAdd20()  + 50, 18, object.getType().equals("function") ? "[Function] " + object.getName() : "[List] " + object.getName());
                    }
                    this.buttonList.add(button);
                    FunctionObjectMap.put(button, object);
                    ButtonListHeight = ButtonListHeight + 20;
                    index = index + 1;
                }
            }
        }
        upButton = new GuiScrollButton(114514,FunctionManager.getLongestTextWidthAdd20()  + 100,this.height / 2 - 30,30,30,"ScrollUp1","ScrollUp2");
        downButton = new GuiScrollButton(1919810,FunctionManager.getLongestTextWidthAdd20()  + 100,this.height / 2 + 30,30,30,"ScrollDown1","ScrollDown2");

        this.buttonList.add(upButton);
        this.buttonList.add(downButton);

        BackButton = new GuiClickGUIButton(0,10,this.height - 25,200,20,"Back");
        this.buttonList.add(BackButton);

    }
    public void drawScreen(int x, int y, float delta)
    {
        drawDefaultBackground();
        for(GuiButton button : this.buttonList){
            if(button instanceof GuiTableButton){
                ((GuiTableButton) button).Excursion = this.ButtonExcursion;
            }
            if(button instanceof GuiButtonSettings){
                ((GuiButtonSettings) button).Excursion = this.ButtonExcursion;
            }
        }
        super.drawScreen(x,y,delta);
        Utils.drawStringScaled("Antimony",Minecraft.getMinecraft().fontRendererObj,(this.width / 2) - ((Minecraft.getMinecraft().fontRendererObj.getStringWidth("Antimony") * 5) / 2),5,Chroma.color.getRGB(),5);
        int Scaling;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if(Minecraft.getMinecraft().gameSettings.guiScale != 0) {
            Scaling = (5 - Minecraft.getMinecraft().gameSettings.guiScale) * 2;
        } else {
            Scaling = 2;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0,0,100);
        drawEntityOnScreen((this.width / 4) * 3,this.height / 4 * 3,Scaling * 20,(float)(this.width / 2) - x, (float)(this.height / 2) - y,Minecraft.getMinecraft().thePlayer);
        GlStateManager.popMatrix();
    }
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == BackButton) {
            mc.displayGuiScreen(parentScreen);
        }
        if(button.id != 114514 && button.id != 1919810) {
            if (button.id > 0) {
                for (Map.Entry<GuiButton, SelectObject> entry : FunctionObjectMap.entrySet()) {
                    if (button == entry.getKey()) {
                        if (entry.getValue().getType().equals("function")) {
                            FunctionManager.switchStatus(entry.getValue().getName());
                            break;
                        } else if (entry.getValue().getType().equals("table")) {
                            mc.displayGuiScreen(new ClickGUI(mc.currentScreen, entry.getValue().getName()));
                        }
                    }
                }
            } else if (button.id < 0) {
                for (Map.Entry<GuiButton, SelectObject> entry : FunctionObjectMap.entrySet()) {
                    if (-button.id == entry.getKey().id) {
                        mc.displayGuiScreen(new SettingsGUI(mc.currentScreen, Objects.requireNonNull(FunctionManager.getFunctionByName(entry.getValue().getName())).getName(), Objects.requireNonNull(FunctionManager.getFunctionByName(entry.getValue().getName())).getConfigurationList()));
                    }
                }
            }
        } else if(button.id == 114514){
            this.ButtonExcursion = ButtonExcursion - 10;
        } else {
            this.ButtonExcursion = ButtonExcursion + 10;
        }
    }
    public static void drawEntityOnScreen(int p_drawEntityOnScreen_0_, int p_drawEntityOnScreen_1_, int p_drawEntityOnScreen_2_, float p_drawEntityOnScreen_3_, float p_drawEntityOnScreen_4_, EntityLivingBase p_drawEntityOnScreen_5_) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_drawEntityOnScreen_0_, (float)p_drawEntityOnScreen_1_, 50.0F);
        GlStateManager.scale((float)(-p_drawEntityOnScreen_2_), (float)p_drawEntityOnScreen_2_, (float)p_drawEntityOnScreen_2_);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float lvt_6_1_ = p_drawEntityOnScreen_5_.renderYawOffset;
        float lvt_7_1_ = p_drawEntityOnScreen_5_.rotationYaw;
        float lvt_8_1_ = p_drawEntityOnScreen_5_.rotationPitch;
        float lvt_9_1_ = p_drawEntityOnScreen_5_.prevRotationYawHead;
        float lvt_10_1_ = p_drawEntityOnScreen_5_.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(p_drawEntityOnScreen_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_drawEntityOnScreen_5_.renderYawOffset = (float)Math.atan((double)(p_drawEntityOnScreen_3_ / 40.0F)) * 20.0F;
        p_drawEntityOnScreen_5_.rotationYaw = (float)Math.atan((double)(p_drawEntityOnScreen_3_ / 40.0F)) * 40.0F;
        p_drawEntityOnScreen_5_.rotationPitch = -((float)Math.atan((double)(p_drawEntityOnScreen_4_ / 40.0F))) * 20.0F;
        p_drawEntityOnScreen_5_.rotationYawHead = p_drawEntityOnScreen_5_.rotationYaw;
        p_drawEntityOnScreen_5_.prevRotationYawHead = p_drawEntityOnScreen_5_.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager lvt_11_1_ = Minecraft.getMinecraft().getRenderManager();
        lvt_11_1_.setPlayerViewY(180.0F);
        lvt_11_1_.setRenderShadow(false);
        lvt_11_1_.renderEntityWithPosYaw(p_drawEntityOnScreen_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        lvt_11_1_.setRenderShadow(true);
        p_drawEntityOnScreen_5_.renderYawOffset = lvt_6_1_;
        p_drawEntityOnScreen_5_.rotationYaw = lvt_7_1_;
        p_drawEntityOnScreen_5_.rotationPitch = lvt_8_1_;
        p_drawEntityOnScreen_5_.prevRotationYawHead = lvt_9_1_;
        p_drawEntityOnScreen_5_.rotationYawHead = lvt_10_1_;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    public void drawInformationString(String str,int x,int y){
        FontManager.QuicksandFont.drawSmoothString(str,x,y + height,0xFFFFFF);
    }


}
