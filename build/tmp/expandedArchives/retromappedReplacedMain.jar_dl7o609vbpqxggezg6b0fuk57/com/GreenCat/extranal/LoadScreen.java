package com.GreenCat.extranal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;

public class LoadScreen {
    Minecraft mc = Minecraft.func_71410_x();
    public static JFrame LoadingFrame = new JFrame("Antimony Test Log Frame");
    public static JPanel Panel = new JPanel();
    public static TextArea text = new TextArea();
    public LoadScreen() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
