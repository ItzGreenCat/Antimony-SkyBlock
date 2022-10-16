package com.greencat.common;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.FunctionManager.SelectGuiFunctionExecutant;
import com.greencat.common.config.ConfigLoader;
import com.greencat.common.gui.ClickGUI;
import com.greencat.common.key.KeyLoader;
import com.greencat.common.storage.SelectGUIStorage;
import com.greencat.common.ui.*;
import com.greencat.test.Screenshot;
import com.greencat.type.SelectTable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class EventLoader {
    NoticeManager n = new NoticeManager();
    ClassicSelectGUI classicSelectGui = new ClassicSelectGUI();
    NewSelectGUI newSelectGui = new NewSelectGUI();
    NewFunctionList newFunctionList = new NewFunctionList();
    ClassicFunctionList classicFunctions = new ClassicFunctionList();
    SelectGuiFunctionExecutant exec = new SelectGuiFunctionExecutant();

    int KeyTime = 0;

    Boolean NowFunctionKeyStatus = true;

    int index = 0;

    public EventLoader() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void RenderEvent(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
            n.Notice();
            if(FunctionManager.getStatus("HUD")) {
                if (FunctionManager.getStatus("ClassicGui")) {
                    classicSelectGui.draw();
                    classicFunctions.draw();
                } else {
                    newSelectGui.draw();
                    newFunctionList.draw();
                }
            }


        }
        if(!Antimony.shouldRenderBossBar) {
            if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void OnKeyPressed(InputEvent.KeyInputEvent event) throws Exception {
        if(FunctionManager.getStatus("HUD")) {
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
        if(KeyLoader.OpenClickGUI.isPressed()){
            Minecraft.getMinecraft().displayGuiScreen(new ClickGUI(Minecraft.getMinecraft().currentScreen,"root"));
        }

        }
    }

