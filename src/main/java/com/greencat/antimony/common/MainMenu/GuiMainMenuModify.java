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
            /*int Scaling;
            if(Minecraft.getMinecraft().gameSettings.guiScale != 0) {
                Scaling = 5 - Minecraft.getMinecraft().gameSettings.guiScale;
            } else {
                Scaling = 1;
            }*/
            int MaxButtonY = (int) (ScreenHeight / 10 * 4.5D);
            int ButtonSpace = 10 / scaledResolution.getScaleFactor();
            int ButtonX = ScreenWidth / 12;
            int ButtonHeight = (int) (ScreenHeight / 19.0D);
            int counter = 0;
            GuiCustomButton SinglePlayerButton = new GuiCustomButton(1,ButtonX, MaxButtonY + counter * (ButtonHeight + ButtonSpace), ButtonHeight * 10, ButtonHeight, "SinglePlayer", "single");
            counter++;
            GuiCustomButton MultiPlayerButton = new GuiCustomButton(2, ButtonX, MaxButtonY + counter * (ButtonHeight + ButtonSpace), ButtonHeight * 10, ButtonHeight, "MultiPlayer", "multi");
            counter++;
            GuiCustomButton ModsButton = new GuiCustomButton(6,ButtonX, MaxButtonY + counter * (ButtonHeight + ButtonSpace), ButtonHeight * 10, ButtonHeight, "Mods", "mods");
            counter++;
            GuiCustomButton LanguageButton = new GuiCustomButton(5,ButtonX, MaxButtonY + counter * (ButtonHeight + ButtonSpace), ButtonHeight * 10, ButtonHeight, "Language", "language");
            counter++;
            GuiCustomButton SettingsButton = new GuiCustomButton(0,ButtonX, MaxButtonY + counter * (ButtonHeight + ButtonSpace), ButtonHeight * 10, ButtonHeight, "Settings", "settings");
            counter++;
            GuiCustomButton QuitButton = new GuiCustomButton(4,ButtonX, MaxButtonY + counter * (ButtonHeight + ButtonSpace), ButtonHeight * 10, ButtonHeight, "Quit", "quit");

            event.buttonList.add(SinglePlayerButton);
            event.buttonList.add(MultiPlayerButton);
            event.buttonList.add(ModsButton);
            event.buttonList.add(LanguageButton);
            event.buttonList.add(SettingsButton);
            event.buttonList.add(QuitButton);

        }
    }
}
