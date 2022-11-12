package com.greencat.antimony.core.gui;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.common.function.title.TitleManager;
import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClickGUI extends GuiScreen {
    private int index = 1;
    private int ButtonListHeight;
    String guiName;
    private GuiScreen parentScreen;
    private GuiButton BackButton;
    HashMap<GuiButton, SelectObject> FunctionObjectMap = new HashMap<>();
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
                    GuiButton button;
                    if(object.getType().equals("function") && Objects.requireNonNull(FunctionManager.getFunctionByName(object.getName())).isConfigurable()) {
                        button = new GuiButton(index, (width / 2) - (((FunctionManager.getLongestTextWidthAdd20() + mc.fontRendererObj.getStringWidth(" [占位] ")) + 50) / 2), ButtonListHeight, FunctionManager.getLongestTextWidthAdd20() + mc.fontRendererObj.getStringWidth(" [占位] ") + 50 - 20, 18, object.getType().equals("function") ? "[功能] " + object.getName() : "[列表] " + object.getName());
                        this.buttonList.add(new GuiButtonSettings(-index,(width / 2) - ((FunctionManager.getLongestTextWidthAdd20() + mc.fontRendererObj.getStringWidth(" [占位] ") + 50) / 2) + FunctionManager.getLongestTextWidthAdd20() + mc.fontRendererObj.getStringWidth(" [占位] ") + 50 - 18, ButtonListHeight));
                    } else {
                        button = new GuiButton(index, (width / 2) - (((FunctionManager.getLongestTextWidthAdd20() + mc.fontRendererObj.getStringWidth(" [占位] ")) + 50) / 2), ButtonListHeight, FunctionManager.getLongestTextWidthAdd20() + mc.fontRendererObj.getStringWidth(" [占位] ") + 50, 18, object.getType().equals("function") ? "[功能] " + object.getName() : "[列表] " + object.getName());
                    }
                    this.buttonList.add(button);
                    FunctionObjectMap.put(button, object);
                    ButtonListHeight = ButtonListHeight + 20;
                    index = index + 1;
                }
            }
        }
        BackButton = new GuiButton(0,this.width / 2 - 100,this.height - 25,200,20,"返回");
        this.buttonList.add(BackButton);

    }
    public void drawScreen(int x, int y, float delta)
    {
        drawDefaultBackground();
        super.drawScreen(x,y,delta);
        Utils.drawStringScaled("antimony",mc.fontRendererObj,5,5,0xFFFFFF,5);
        Utils.drawStringScaled(" -- " + TitleManager.tips,mc.fontRendererObj,(mc.fontRendererObj.getStringWidth("antimony") * 5) + 5,35,0xFFFFFF,2);
    }
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == BackButton) {
            mc.displayGuiScreen(parentScreen);
        }
        if(button.id > 0) {
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
        } else if(button.id < 0){
            for (Map.Entry<GuiButton, SelectObject> entry : FunctionObjectMap.entrySet()) {
                if (-button.id == entry.getKey().id) {
                    mc.displayGuiScreen(new SettingsGUI(mc.currentScreen, Objects.requireNonNull(FunctionManager.getFunctionByName(entry.getValue().getName())).getName(), Objects.requireNonNull(FunctionManager.getFunctionByName(entry.getValue().getName())).getConfigurationList()));
                }
            }
        }
    }


}
