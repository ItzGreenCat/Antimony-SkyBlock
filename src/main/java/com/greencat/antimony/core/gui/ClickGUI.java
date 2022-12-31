package com.greencat.antimony.core.gui;

import com.greencat.Antimony;
import com.greencat.antimony.common.function.title.TitleManager;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.utils.Blur;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.FontManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClickGUI extends GuiScreen {
    //Dynamic index of button ID
    private int index = 1;
    //The current height of the button
    private int ButtonListHeight;
    //Whether to display animations when opening the current GUI
    private boolean animation = true;
    //Gui's name(Specified here is the name of the SelectTable, which specifies which SelectTable content is displayed by the current GUI)
    String guiName;
    //The width of the GUI sidebar
    private final int widthBound = FunctionManager.getLongestTextWidthAdd20() + 40;
    //Parent GUI
    private GuiScreen parentScreen;
    private GuiButton BackButton;
    private GuiButton refreshButton;
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    //The offset value of the GUI sidebar
    private int pos;
    //The vertical axis offset value of the button
    public int ButtonExcursion = 0;
    //FPS Limit
    private int limit;
    Blur blur = new Blur();
    //The table that corresponds the button to the SelectObject
    HashMap<GuiButton, SelectObject> FunctionObjectMap = new HashMap<>();
    public ClickGUI(GuiScreen parent,String guiName)
    {
        this.guiName = guiName;
        parentScreen = parent;
    }
    public ClickGUI(GuiScreen parent,String guiName,boolean animation)
    {
        this.guiName = guiName;
        parentScreen = parent;
        this.animation = animation;
    }
    public void initGui(){
        limit = Minecraft.getMinecraft().gameSettings.limitFramerate;
        Minecraft.getMinecraft().gameSettings.limitFramerate = 60;
        pos = widthBound;
        index = 1;
        ButtonListHeight = 53;
        FunctionObjectMap.clear();
        for(SelectTable table : SelectGUIStorage.TableStorage){
            if(table.getID().equals(guiName)) {
                for(SelectObject object : table.getList()) {
                    GuiTableButton button;
                    if(object.getType().equals("function") && Objects.requireNonNull(FunctionManager.getFunctionByName(object.getName())).isConfigurable()) {
                        button = new GuiTableButton(index, 10 , ButtonListHeight, widthBound - 28, 18,object.getName(),true);
                        this.buttonList.add(new GuiButtonSettings(-index,10 + widthBound - 28, ButtonListHeight));
                    } else {
                        if(object.getType().equals("function")) {
                            button = new GuiTableButton(index, 10, ButtonListHeight, widthBound - 10, 18, object.getName(), true);
                        } else {
                            button = new GuiTableButton(index, 10, ButtonListHeight, widthBound - 10, 18, object.getName(), false);
                        }
                    }
                    this.buttonList.add(button);
                    FunctionObjectMap.put(button, object);
                    ButtonListHeight = ButtonListHeight + 20;
                    index = index + 1;
                }
            }
        }
        BackButton = new GuiClickGUIButton(0,10,this.height - 25,widthBound - 10,18,"Back",new ResourceLocation(Antimony.MODID,"clickgui/back.png"));
        refreshButton = new GuiClickGUIButton(114514,this.width - 70,this.height - 25,70,18,"Refresh");
        this.buttonList.add(BackButton);
        this.buttonList.add(refreshButton);

    }
    public void drawScreen(int x, int y, float delta)
    {
        if(!(parentScreen instanceof ClickGUI) && animation) {
            if (pos > 0) {
                if(Minecraft.getDebugFPS() != 0) {
                    pos = pos - (widthBound / Minecraft.getDebugFPS() * 2);
                } else {
                    pos = 0;
                }
            } else {
                pos = 0;
            }
        } else {
            pos = 0;
        }
        drawRect(0,0,scaledResolution.getScaledWidth(),20,new Color(23,135,183).getRGB());
        FontManager.QuicksandFont35.drawString("Antimony",2,2,new Color(255,255,255).getRGB());
        drawRect(0,20,widthBound - pos,scaledResolution.getScaledHeight(),new Color(255,255,255,128).getRGB());
        drawRect(5,44,widthBound - pos  - 5,46,new Color(0,0,0).getRGB());
        FontManager.QuicksandFont35.drawString("ClickGui",22 - pos,25,new Color(0,0,0).getRGB());
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Antimony.MODID,"clickgui/GuiIcon.png"));
        drawModalRectWithCustomSizedTexture(-pos,22,0,0,20,20,20,20);
        for(GuiButton button : this.buttonList){
            if(button instanceof GuiClickGUIButton){
                button.xPosition = ((GuiClickGUIButton) button).OriginalXPos - pos;
            }
            if(button instanceof GuiButtonSettings){
                button.xPosition = ((GuiButtonSettings) button).OriginalXPos - pos;
            }
        }
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.ButtonExcursion = this.ButtonExcursion - 10;
        } else if (dWheel > 0) {
            this.ButtonExcursion = this.ButtonExcursion + 10;
        }
        for(GuiButton button : this.buttonList){
            button.visible = button.yPosition >= 53 && (button.yPosition <= this.height - 25 - 18 || (button.id == 0 || button.id == 114514));
        }
        for(GuiButton button : this.buttonList){
                if (button instanceof GuiTableButton) {
                    ((GuiTableButton) button).Excursion = this.ButtonExcursion;
                }
                if (button instanceof GuiButtonSettings) {
                    ((GuiButtonSettings) button).Excursion = this.ButtonExcursion;
                }
        }
        super.drawScreen(x,y,delta);
    }
    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.visible) {
            if (button == BackButton) {
                if (parentScreen instanceof ClickGUI) {
                    ((ClickGUI) parentScreen).animation = false;
                }
                mc.displayGuiScreen(parentScreen);
            }
                if (button.id > 0) {
                    if(button.id != 114514) {
                        for (Map.Entry<GuiButton, SelectObject> entry : FunctionObjectMap.entrySet()) {
                            if (button == entry.getKey()) {
                                if (entry.getValue().getType().equals("function")) {
                                    FunctionManager.switchStatus(entry.getValue().getName());
                                    break;
                                } else if (entry.getValue().getType().equals("table")) {
                                    mc.displayGuiScreen(new ClickGUI(mc.currentScreen, entry.getValue().getName(), false));
                                }
                            }
                        }
                    } else {
                        pos = 0;
                    }
                } else if (button.id < 0) {
                    for (Map.Entry<GuiButton, SelectObject> entry : FunctionObjectMap.entrySet()) {
                        if (-button.id == entry.getKey().id) {
                            mc.displayGuiScreen(new SettingsGUI(mc.currentScreen, Objects.requireNonNull(FunctionManager.getFunctionByName(entry.getValue().getName())).getName(), Objects.requireNonNull(FunctionManager.getFunctionByName(entry.getValue().getName())).getConfigurationList()));
                        }
                    }
            }
        }
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
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
        FontManager.QuicksandFont.drawString(str,x,y + height,0xFFFFFF);
    }
    @Override
    public void onGuiClosed(){
        Minecraft.getMinecraft().gameSettings.limitFramerate = limit;
    }

}
