package com.greencat.common.gui;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.settings.*;
import com.greencat.utils.Blur;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsGUI extends GuiScreen {
    String guiName;
    private int index = 1;
    private GuiScreen parentScreen;
    private GuiButton BackButton;
    private int ButtonListHeight;
    List<ISettingOption> options;
    List<AbstractSettingOptionTextField> optionTextField = new ArrayList<AbstractSettingOptionTextField>();
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    public SettingsGUI(GuiScreen parent, String guiName, List<ISettingOption> list)
    {
        this.guiName = guiName;
        parentScreen = parent;
        options = list;
    }
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        index = 1;
        ButtonListHeight = 70;
        optionTextField.clear();
        for(ISettingOption option : options){
            if(option instanceof AbstractSettingOptionButton) {
                AbstractSettingOptionButton button = (AbstractSettingOptionButton) option;
                button.setButtonID(index);
                button.setX((width / 2) - ((FunctionManager.getLongestTextWidthAdd20() + 50) / 2));
                button.setY(ButtonListHeight);
                button.setWidth(FunctionManager.getLongestTextWidthAdd20() + 50);
                button.setHeight(18);
                ButtonListHeight = ButtonListHeight + 30;
                index = index + 1;
                this.buttonList.add(button);
            }
            if(option instanceof AbstractSettingOptionTextField){
                AbstractSettingOptionTextField textField = (AbstractSettingOptionTextField) option;
                textField.init();
                textField.setX((width / 2) - ((FunctionManager.getLongestTextWidthAdd20() + 50) / 2));
                textField.setY(ButtonListHeight);
                textField.setWidth(FunctionManager.getLongestTextWidthAdd20() + 50);
                textField.setHeight(18);
                ButtonListHeight = ButtonListHeight + 30;
                index = index + 1;
                optionTextField.add(textField);
            }
        }
        BackButton = new GuiButton(0,this.width / 2 - 100,this.height - 25,200,20,"保存并返回");
        this.buttonList.add(BackButton);
    }
    public void drawScreen(int x, int y, float delta)
    {
        if(!Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
            Blur b = new Blur();
            b.blurAreaBoarder(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 1, 0.3F, 1, 0);
        }
        super.drawScreen(x,y,delta);
        for(GuiButton button : this.buttonList){
            if(button instanceof AbstractSettingOptionButton){
                ((AbstractSettingOptionButton)button).update();
            }
        }
        for(AbstractSettingOptionTextField textField : optionTextField){
            textField.update();
        }
        String title = guiName + " 配置";
        Utils.drawStringScaled(title,mc.fontRendererObj,(width / 2) - ((mc.fontRendererObj.getStringWidth(title) * 2) / 2),25,0xFFFFFF,2);
    }
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == BackButton) {
            for (AbstractSettingOptionTextField textField : optionTextField) {
                textField.setValue();
            }
            mc.displayGuiScreen(parentScreen);
        }
        if(button instanceof AbstractSettingOptionButton){
            if(button instanceof SettingBoolean){
                handleBoolean((SettingBoolean) button);
            }
            if(button instanceof SettingTypeSelector){
                handleTypeSelector((SettingTypeSelector) button);
            }
        }
    }
    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        if(!optionTextField.isEmpty()) {
            for (AbstractSettingOptionTextField textField : optionTextField) {
                if (textField.textboxKeyTyped(par1, par2))
                    return;
            }
        }
        super.keyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) throws IOException {
        if(!optionTextField.isEmpty()) {
            for (AbstractSettingOptionTextField textField : optionTextField) {
                textField.mouseClicked(par1, par2, par3);
            }
        }
        super.mouseClicked(par1, par2, par3);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false); //关闭键盘连续输入
        Blur.stopBlur();
    }
    private void handleBoolean(SettingBoolean option){
        option.switchStatus();
        Utils utils = new Utils();
        utils.print(guiName + " 的 " + option.name + "已经设置为 " + ((Boolean)option.getValue() ? "开启" : "关闭"));
    }
    private void handleTypeSelector(SettingTypeSelector option){
        option.switchStatus();
        Utils utils = new Utils();
        utils.print(guiName + " 的 " + option.name + "已经设置为 " + (option.getKey()));
    }
}
