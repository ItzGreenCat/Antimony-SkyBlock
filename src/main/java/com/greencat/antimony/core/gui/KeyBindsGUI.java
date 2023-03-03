package com.greencat.antimony.core.gui;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.settings.AbstractSettingOptionTextField;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.utils.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.print.DocFlavor;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

import static org.yaml.snakeyaml.tokens.Token.ID.Key;

public class KeyBindsGUI extends GuiScreen{
    //go to see ClickGUI.java
    private GuiScreen parentScreen;
    private int index = 1;
    private int ButtonListHeight;
    private KeyBindButton targetButton;
    //FPS Limit
    private int limit;
    private boolean animation = true;
    private final int widthBound = FunctionManager.getLongestTextWidthAdd20() + 40;
    private GuiButton BackButton;
    private GuiButton refreshButton;
    private GuiTextField field;
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    private int pos;
    private String rule = null;
    public int ButtonExcursion = 0;
    public KeyBindsGUI(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
    public KeyBindsGUI(GuiScreen parentScreen,String rule) {
        this.parentScreen = parentScreen;
        this.rule = rule;
    }
    public void initGui() {
        limit = Minecraft.getMinecraft().gameSettings.limitFramerate;
        Keyboard.enableRepeatEvents(true);
        Minecraft.getMinecraft().gameSettings.limitFramerate = 60;
        pos = widthBound;
        index = 1;
        ButtonListHeight = 53;
        for (Map.Entry<String,AntimonyFunction> entry : AntimonyRegister.FunctionList.entrySet()) {
            AntimonyFunction function = entry.getValue();
            if(rule == null) {
                this.buttonList.add(new KeyBindButton(index, 10, ButtonListHeight, widthBound - 10, 18, function));
                ButtonListHeight = ButtonListHeight + 20;
                index = index + 1;
            } else {
                if(function.getName().toLowerCase().contains(rule.toLowerCase())){
                    this.buttonList.add(new KeyBindButton(index, 10, ButtonListHeight, widthBound - 10, 18, function));
                    ButtonListHeight = ButtonListHeight + 20;
                    index = index + 1;
                }
            }
        }
        BackButton = new GuiClickGUIButton(0,10,this.height - 25,widthBound - 10,18,"Back",new ResourceLocation(Antimony.MODID,"clickgui/back.png"));
        refreshButton = new GuiClickGUIButton(114514,this.width - 70,this.height - 45,70,18,"Refresh");
        field = new GuiTextField(783468230,Minecraft.getMinecraft().fontRendererObj,this.width - 90,this.height - 25,87,18);
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
        for(GuiButton button : this.buttonList){
            if(button instanceof KeyBindButton){
                if(button != targetButton) {
                    ((KeyBindButton) button).refresh();
                } else {
                    ((KeyBindButton) button).setWait();
                }
            }
        }
        drawRect(0,0,scaledResolution.getScaledWidth(),20,new Color(23,135,183).getRGB());
        FontManager.QuicksandFont35.drawString("Antimony",2,2,new Color(255,255,255).getRGB());
        drawRect(0,20,widthBound - pos,scaledResolution.getScaledHeight(),new Color(255,255,255,128).getRGB());
        drawRect(5,44,widthBound - pos  - 5,46,new Color(0,0,0).getRGB());
        FontManager.QuicksandFont35.drawString("KeyBinds",2 - pos,25,new Color(0,0,0).getRGB());
        drawModalRectWithCustomSizedTexture(-pos,22,0,0,20,20,20,20);
        for(GuiButton button : this.buttonList){
            if(button instanceof KeyBindButton){
                button.xPosition = ((KeyBindButton) button).OriginalXPos - pos;
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
            if (button instanceof KeyBindButton) {
                ((KeyBindButton) button).Excursion = this.ButtonExcursion;
            }
        }
        field.drawTextBox();
        super.drawScreen(x,y,delta);
    }
    @Override
    public void actionPerformed(GuiButton button){
        if(button.id == 0){
            mc.displayGuiScreen(parentScreen);
        } else {
            if(button == refreshButton) {
                pos = 0;
            } else if(button instanceof KeyBindButton) {
                targetButton = (KeyBindButton) button;
            }
        }
    }
    @Override
    protected void keyTyped(char typeChar, int keyCode) throws IOException {
        if(targetButton != null) {
            if (keyCode != Keyboard.KEY_ESCAPE) {
                targetButton.setKey(keyCode);
                targetButton = null;
                super.keyTyped(typeChar, keyCode);
            } else {
                targetButton.setKey(-114514);
                targetButton = null;
            }
            Antimony.reloadKeyMapping();
        } else {
            if(keyCode == Keyboard.KEY_RETURN && field.isFocused()) {
                Minecraft.getMinecraft().displayGuiScreen(new KeyBindsGUI(parentScreen,field.getText()));
            } else {
                field.textboxKeyTyped(typeChar, keyCode);
                super.keyTyped(typeChar, keyCode);
            }
        }
    }
    @Override
    protected void mouseClicked(int par1, int par2, int par3) throws IOException {
        if(field.getVisible()) {
            field.mouseClicked(par1, par2, par3);
        }
        super.mouseClicked(par1, par2, par3);
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void onGuiClosed(){
        Keyboard.enableRepeatEvents(false);
        Minecraft.getMinecraft().gameSettings.limitFramerate = limit;
    }
}
