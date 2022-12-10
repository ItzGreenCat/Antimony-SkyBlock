package com.greencat.antimony.core.gui;

import com.greencat.Antimony;
import com.greencat.antimony.common.function.title.TitleManager;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.settings.*;
import com.greencat.antimony.utils.FontManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsGUI extends GuiScreen {
    //go to see ClickGUI.java
    String guiName;
    private int index = 1;
    private GuiScreen parentScreen;
    private GuiButton BackButton;
    private int ButtonListHeight;
    private final int widthBound = FunctionManager.getLongestTextWidthAdd20() + 160;
    private int pos;
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
        pos = widthBound;
        index = 1;
        ButtonListHeight = 70;
        optionTextField.clear();
        for(ISettingOption option : options){
            if(option instanceof AbstractSettingOptionButton) {
                AbstractSettingOptionButton button = (AbstractSettingOptionButton) option;
                button.setButtonID(index);
                button.setX(25);
                button.setY(ButtonListHeight);
                button.setWidth(widthBound - 50);
                button.setHeight(18);
                ButtonListHeight = ButtonListHeight + 30;
                index = index + 1;
                this.buttonList.add(button);
            }
            if(option instanceof AbstractSettingOptionTextField){
                AbstractSettingOptionTextField textField = (AbstractSettingOptionTextField) option;
                textField.init();
                textField.setX(25);
                textField.setY(ButtonListHeight);
                textField.setWidth(widthBound - 50);
                textField.setHeight(18);
                ButtonListHeight = ButtonListHeight + 30;
                index = index + 1;
                optionTextField.add(textField);
            }
        }
        BackButton = new GuiClickGUIButton(0,10,this.height - 25,widthBound - 10,18,"Save and Back",new ResourceLocation(Antimony.MODID,"clickgui/back.png"));;
        this.buttonList.add(BackButton);
    }
    public void drawScreen(int x, int y, float delta)
    {
        if (pos > 0) {
            if(Minecraft.getDebugFPS() != 0) {
                pos = (int) (pos - (widthBound / Minecraft.getDebugFPS() * 2.5F));
            } else {
                pos = 0;
            }
        } else {
            pos = 0;
        }
        drawRect(0,0,scaledResolution.getScaledWidth(),20,new Color(23,135,183).getRGB());
        FontManager.QuicksandFont35.drawSmoothString("Antimony",2,2,new Color(255,255,255).getRGB());
        drawRect(0,20,widthBound - pos,scaledResolution.getScaledHeight(),new Color(255,255,255,128).getRGB());
        String title = guiName + " 配置";
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.ButtonExcursion = this.ButtonExcursion - 10;
        } else if (dWheel > 0) {
            this.ButtonExcursion = this.ButtonExcursion + 10;
        }
        for(GuiButton button : this.buttonList){
            if(button instanceof AbstractSettingOptionButton){
                button.xPosition = ((AbstractSettingOptionButton)button).OriginalXPos - pos;
            }
        }
        for(AbstractSettingOptionTextField textField : optionTextField){
             textField.xPosition = textField.OriginalXPos - pos;
        }
        for(GuiButton button : this.buttonList){
                if (button instanceof AbstractSettingOptionButton) {
                    ((AbstractSettingOptionButton) button).Excursion = this.ButtonExcursion;
                }
        }
        for(AbstractSettingOptionTextField textField : optionTextField){
            textField.Excursion = this.ButtonExcursion;
        }
        for(AbstractSettingOptionTextField textField : optionTextField){
            textField.visible = !(textField.yPosition < 53 || textField.yPosition > (this.height - 25 - 18));
        }
        for(GuiButton button : this.buttonList){
            button.visible = button.yPosition >= 53 && (button.yPosition <= this.height - 25 - 18 || button.id == 0);
        }
        Utils.drawStringScaled(title,mc.fontRendererObj,(widthBound / 2.0F) - ((mc.fontRendererObj.getStringWidth(title) * 2) / 2.0F),25,0xFFFFFF,2);
        drawRect(5,44,widthBound - pos  - 5,46,new Color(0,0,0).getRGB());
        for(GuiButton button : this.buttonList){
            if(button instanceof AbstractSettingOptionButton){
                ((AbstractSettingOptionButton)button).update();
            }
        }
        for(AbstractSettingOptionTextField textField : optionTextField){
            textField.update();
        }
        super.drawScreen(x,y,delta);
    }
    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.visible) {
            if (button == BackButton) {
                for (AbstractSettingOptionTextField textField : optionTextField) {
                    textField.setValue();
                }
                mc.displayGuiScreen(parentScreen);
            }

                if (button instanceof AbstractSettingOptionButton) {
                    if (button instanceof SettingBoolean) {
                        handleBoolean((SettingBoolean) button);
                    }
                    if (button instanceof SettingTypeSelector) {
                        handleTypeSelector((SettingTypeSelector) button);
                    }
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
                if(textField.getVisible()) {
                    textField.mouseClicked(par1, par2, par3);
                }
            }
        }
        super.mouseClicked(par1, par2, par3);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
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
    public boolean isInclude(List<Integer> list, int number){
        for(int num : list){
            if(num == number){
                return true;
            }
        }
        return false;
    }
}
