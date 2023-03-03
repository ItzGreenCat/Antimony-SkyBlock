package com.greencat.antimony.common;

import com.greencat.Antimony;
import com.greencat.antimony.common.key.KeyLoader;
import com.greencat.antimony.common.mixins.GuiScreenAccessor;
import com.greencat.antimony.common.test.Screenshot;
import com.greencat.antimony.core.CustomSizeBackground;
import com.greencat.antimony.core.EtherwarpTeleport;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.FunctionManager.SelectGuiFunctionExecutant;
import com.greencat.antimony.core.config.EtherwarpWaypoints;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.gui.ClickGUI;
import com.greencat.antimony.core.gui.KeyBindsGUI;
import com.greencat.antimony.core.gui.SettingsGUI;
import com.greencat.antimony.core.storage.SelectGUIStorage;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.core.ui.QSF.QSFFunctionList;
import com.greencat.antimony.core.ui.QSF.QSFSelectGUI;
import com.greencat.antimony.core.ui.classic.ClassicFunctionList;
import com.greencat.antimony.core.ui.classic.ClassicSelectGUI;
import com.greencat.antimony.core.ui.transparent.FunctionList;
import com.greencat.antimony.core.ui.transparent.SelectGUI;
import com.greencat.antimony.core.ui.white.NewFunctionList;
import com.greencat.antimony.core.ui.white.NewSelectGUI;
import com.greencat.antimony.develop.Console;
import com.greencat.antimony.utils.sound.SoundPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;

import static com.greencat.Antimony.mouseX;
import static com.greencat.Antimony.mouseY;

public class EventLoader {
    ClassicSelectGUI classicSelectGui = new ClassicSelectGUI();
    NewSelectGUI newSelectGui = new NewSelectGUI();
    NewFunctionList newFunctionList = new NewFunctionList();
    ClassicFunctionList classicFunctions = new ClassicFunctionList();
    SelectGUI transparentSelectGUI = new SelectGUI();
    FunctionList transparentFunctionList = new FunctionList();
    QSFSelectGUI qsfSelectGUI = new QSFSelectGUI();
    QSFFunctionList qsfFunctionList = new QSFFunctionList();
    SelectGuiFunctionExecutant exec = new SelectGuiFunctionExecutant();

    int KeyTime = 0;

    Boolean NowFunctionKeyStatus = true;

    int index = 0;

    public EventLoader() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void RenderEvent(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
            int hidePart = (Integer) getConfigByFunctionName.get("HUD", "hide");
            int style = (Integer) getConfigByFunctionName.get("HUD", "style");
            if (FunctionManager.getStatus("HUD")) {
                if (!(Minecraft.getMinecraft().currentScreen instanceof ClickGUI) && !(Minecraft.getMinecraft().currentScreen instanceof SettingsGUI)) {
                    if (style == 0) {
                        classicSelectGui.draw();
                        classicFunctions.draw();
                    }
                    if (style == 1) {
                        newSelectGui.draw();
                        newFunctionList.draw();
                    }
                    if (style == 2) {
                        transparentSelectGUI.draw();
                        transparentFunctionList.draw();
                    }
                    if (style == 3) {
                        qsfSelectGUI.draw();
                        qsfFunctionList.draw();
                    }
                }
            } else {
                if (hidePart == 0) {
                    if (style == 0) {
                        classicFunctions.draw();
                    }
                    if (style == 1) {
                        newFunctionList.draw();
                    }
                    if (style == 2) {
                        transparentFunctionList.draw();
                    }
                    if(style == 3){
                        qsfFunctionList.draw();
                    }
                }
                if (hidePart == 1) {
                    if (style == 0) {
                        classicSelectGui.draw();
                    }
                    if (style == 1) {
                        newSelectGui.draw();
                    }
                    if (style == 2) {
                        transparentSelectGUI.draw();
                    }
                    if (style == 3) {
                        qsfSelectGUI.draw();
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void RenderEvent(RenderGameOverlayEvent event) {
        if (!Antimony.shouldRenderBossBar) {
            if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void OnKeyPressed(InputEvent.KeyInputEvent event) throws Exception {
        if (FunctionManager.getStatus("HUD") || (Integer) getConfigByFunctionName.get("HUD", "style") == 1) {
            if (KeyLoader.SelectGuiUP.isPressed()) {
                for (SelectTable table : SelectGUIStorage.TableStorage) {
                    if (table.getID().equals(Antimony.PresentGUI)) {
                        if (index - 1 < 0) {
                            index = table.getList().size() - 1;
                        } else {
                            index = index - 1;
                        }
                        if (index >= 0 && index < table.getList().size()) {
                            Antimony.PresentFunction = table.getList().get(index).getName();
                        }
                    }
                }
            }
            if (KeyLoader.SelectGuiDown.isPressed()) {
                for (SelectTable table : SelectGUIStorage.TableStorage) {
                    if (table.getID().equals(Antimony.PresentGUI)) {
                        if (index + 1 > table.getList().size() - 1) {
                            index = 0;
                        } else {
                            index = index + 1;
                        }
                        if (index >= 0 && index < table.getList().size()) {
                            Antimony.PresentFunction = table.getList().get(index).getName();
                        }
                    }
                }
            }
            if (KeyLoader.SelectGuiEnter.isPressed()) {
                if (KeyTime == 0) {
                    KeyTime = 1;
                    for (SelectTable table : SelectGUIStorage.TableStorage) {
                        if (table.getID().equals(Antimony.PresentGUI)) {
                            if (table.getList().get(index).getType().equals("table")) {
                                exec.EnterTable(table.getList().get(index).getName());
                                exec.SetRunFunctionStatus(false);
                                NowFunctionKeyStatus = false;
                                index = 0;
                            } else if (table.getList().get(index).getType().equals("function")) {

                                if (!exec.getRunFunctionStatus()) {
                                    if (NowFunctionKeyStatus) {
                                        exec.SetRunFunctionStatus(true);
                                        exec.RunFunction(table.getList().get(index).getName());
                                    } else {
                                        NowFunctionKeyStatus = true;
                                    }
                                } else {
                                    if (NowFunctionKeyStatus) {
                                        exec.RunFunction(table.getList().get(index).getName());
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                KeyTime = 0;
            }
            if (KeyLoader.SelectGuiBack.isPressed()) {
                Antimony.PresentGUI = "root";
                for (SelectTable table : SelectGUIStorage.TableStorage) {
                    if (table.getID().equals(Antimony.PresentGUI)) {
                        Antimony.PresentFunction = table.getList().get(0).getName();
                    }
                }
                index = 0;
            }
        }
        if (KeyLoader.HugeScreenshot.isPressed()) {
            Screenshot screenshot = new Screenshot();
            screenshot.CreateScreenshot(Antimony.ImageScaling);
        }
        if (KeyLoader.OpenClickGUI.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ClickGUI(Minecraft.getMinecraft().currentScreen, "root"));
        }
        if (KeyLoader.OpenBindGUI.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new KeyBindsGUI(Minecraft.getMinecraft().currentScreen));
        }
        if (KeyLoader.OpenConsole.isPressed()) {
            Console.frame.setVisible(true);
        }
        if(KeyLoader.AddAOTV.isPressed()){
            EtherwarpTeleport.add(new BlockPos(Minecraft.getMinecraft().thePlayer.getPositionVector()).down());
        }
        if(KeyLoader.ClearAOTV.isPressed()){
            EtherwarpTeleport.clear();
        }
        if(KeyLoader.UndoAOTV.isPressed()){
            EtherwarpTeleport.undo();
        }
        if(KeyLoader.LoadAOTV.isPressed()){
            EtherwarpWaypoints.load();
        }
    }

    @SubscribeEvent
    public void handleFunctionKeyBinds(InputEvent.KeyInputEvent event) {
        for(Map.Entry<AntimonyFunction,Integer> entry : Antimony.KeyBinding.entrySet()){
            if(Keyboard.getEventKey() == entry.getValue()) {
                if (Keyboard.getEventKeyState()) {
                    FunctionManager.switchStatus(entry.getKey().getName());
                }
            }
        }
    }
    GuiButton prevButton;
    @SubscribeEvent
    public void handleGuiSound(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().currentScreen != null){
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            List<? extends GuiButton> buttons = ((GuiScreenAccessor)screen).getButton();
            if(buttons.isEmpty()){
                return;
            }
            for(GuiButton button : buttons){
                if(mouseX >= button.xPosition && mouseY >= button.yPosition && mouseX < button.xPosition + button.width && mouseY < button.yPosition + button.height) {
                    if (prevButton == null) {
                        prevButton = button;
                        SoundPlayer.play(new ResourceLocation("antimony:button_select"));
                    } else if (prevButton != button) {
                        prevButton = button;
                        SoundPlayer.play(new ResourceLocation("antimony:button_select"));
                    }
                    return;
                }
            }
            prevButton = null;
        }
    }
    static double prevLocation = 1.0D;
    static long lastCheck = 0L;
    @SubscribeEvent
    public void handleBackgroundAnimation(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.END){
            if(System.currentTimeMillis() - lastCheck > 1000){
                lastCheck = System.currentTimeMillis();
                if(CustomSizeBackground.animation.xCoord == prevLocation) {
                    CustomSizeBackground.animation.xCoord = 1;
                    CustomSizeBackground.next = 1;
                }
                prevLocation = CustomSizeBackground.animation.xCoord;
            }
        }
    }
    /*@SubscribeEvent
    public void clientRender(RenderWorldLastEvent event) {
        Utils.OutlinedBoxWithESP(new BlockPos(Minecraft.getMinecraft().thePlayer.getPositionVector()).down(),Color.CYAN,false,2.5F);
    }*/
}

