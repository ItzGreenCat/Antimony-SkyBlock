package com.greencat.antimony.core.gui;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.settings.*;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
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
    private GuiScrollButton upButton;
    private GuiScrollButton downButton;
    public int ButtonExcursion = 0;
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
        upButton = new GuiScrollButton(114514,(width / 2) - ((FunctionManager.getLongestTextWidthAdd20() + 50) / 2) + FunctionManager.getLongestTextWidthAdd20() + 50 + 50,this.height / 2 - 30,30,30,"ScrollUp1","ScrollUp2");
        downButton = new GuiScrollButton(1919810,(width / 2) - ((FunctionManager.getLongestTextWidthAdd20() + 50) / 2) + FunctionManager.getLongestTextWidthAdd20() + 50 + 50,this.height / 2 + 30,30,30,"ScrollDown1","ScrollDown2");

        this.buttonList.add(upButton);
        this.buttonList.add(downButton);

        BackButton = new GuiClickGUIButton(0,this.width / 2 - 100,this.height - 25,200,20,"Save and Back");
        this.buttonList.add(BackButton);
    }
    public void drawScreen(int x, int y, float delta)
    {
        drawDefaultBackground();
        String title = guiName + " 配置";
        for(GuiButton button : this.buttonList){
            if(button instanceof AbstractSettingOptionButton){
                ((AbstractSettingOptionButton) button).Excursion = this.ButtonExcursion;
            }
        }
        for(AbstractSettingOptionTextField textField : optionTextField){
            textField.Excursion = this.ButtonExcursion;
        }
        Utils.drawStringScaled(title,mc.fontRendererObj,(width / 2) - ((mc.fontRendererObj.getStringWidth(title) * 2) / 2),25,0xFFFFFF,2);
        super.drawScreen(x,y,delta);
        for(GuiButton button : this.buttonList){
            if(button instanceof AbstractSettingOptionButton){
                ((AbstractSettingOptionButton)button).update();
            }
        }
        for(AbstractSettingOptionTextField textField : optionTextField){
            textField.update();
        }
    }
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == BackButton) {
            for (AbstractSettingOptionTextField textField : optionTextField) {
                textField.setValue();
            }
            mc.displayGuiScreen(parentScreen);
        }
        if(button.id != 114514 && button.id != 1919810) {
            if (button instanceof AbstractSettingOptionButton) {
                if (button instanceof SettingBoolean) {
                    handleBoolean((SettingBoolean) button);
                }
                if (button instanceof SettingTypeSelector) {
                    handleTypeSelector((SettingTypeSelector) button);
                }
            }
        } else if(button.id == 114514){
            this.ButtonExcursion = ButtonExcursion - 10;
        } else {
            this.ButtonExcursion = ButtonExcursion + 10;
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
