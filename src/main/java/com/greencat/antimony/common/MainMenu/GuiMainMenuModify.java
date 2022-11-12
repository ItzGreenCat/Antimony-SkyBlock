package com.greencat.antimony.common.MainMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class GuiMainMenuModify {
    public GuiMainMenuModify() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void ModifyGUI(GuiScreenEvent.InitGuiEvent event){
        if(event.gui instanceof GuiMainMenu){
            List<GuiButton> removedButton = new ArrayList<GuiButton>();
            for(GuiButton button : event.buttonList){
                if(button.id == 0 || button.id == 4 || button.id == 5 || button.id == 1 || button.id == 2 || button.id == 14 || button.id == 6){
                    removedButton.add(button);
                }
            }
            if(!removedButton.isEmpty()){
                for(GuiButton button : removedButton){
                    event.buttonList.remove(button);
                }
            }
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            int ScreenWidth = scaledResolution.getScaledWidth();
            int ScreenHeight = scaledResolution.getScaledHeight();
            int Scaling;
            if(Minecraft.getMinecraft().gameSettings.guiScale != 0) {
                Scaling = 5 - Minecraft.getMinecraft().gameSettings.guiScale;
            } else {
                Scaling = 1;
            }
            
            GuiCustomButton SinglePlayerButton = new GuiCustomButton(1, (int) ((ScreenWidth / 2 - 12.5F * Scaling) - (45 * Scaling)), ScreenHeight / 2, 25 * Scaling, 25 * Scaling, "SinglePlayer", "single");
            GuiCustomButton MultiPlayerButton = new GuiCustomButton(2, (int) ((ScreenWidth / 2 - 12.5F * Scaling) - (15 * Scaling)), ScreenHeight / 2, 25 * Scaling, 25 * Scaling, "MultiPlayer", "multi");
            GuiCustomButton ModsButton = new GuiCustomButton(6, (int) ((ScreenWidth / 2 - 12.5F * Scaling) + (15 * Scaling)), ScreenHeight / 2, 25 * Scaling, 25 * Scaling, "Mods", "mods");
            GuiCustomButton LanguageButton = new GuiCustomButton(5, (int) ((ScreenWidth / 2 - 12.5F * Scaling) + (45 * Scaling)), ScreenHeight / 2, 25 * Scaling, 25 * Scaling, "Language", "language");
            GuiCustomButton SettingsButton = new GuiCustomButton(0, (int) ((ScreenWidth / 2 - 12.5F * Scaling) + (30 * Scaling)), (ScreenHeight / 2) + (40 * Scaling), 25 * Scaling, 25 * Scaling, "Settings", "settings");
            GuiCustomButton QuitButton = new GuiCustomButton(4, (int) ((ScreenWidth / 2 - 12.5F * Scaling) - (30 * Scaling)), (ScreenHeight / 2) + (40 * Scaling), 25 * Scaling, 25 * Scaling, "Quit", "quit");

            event.buttonList.add(SinglePlayerButton);
            event.buttonList.add(MultiPlayerButton);
            event.buttonList.add(ModsButton);
            event.buttonList.add(LanguageButton);
            event.buttonList.add(SettingsButton);
            event.buttonList.add(QuitButton);

        }
    }
}
