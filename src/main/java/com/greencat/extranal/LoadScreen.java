package com.greencat.extranal;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import javax.swing.*;
import java.awt.*;

public class LoadScreen {
    Minecraft mc = Minecraft.getMinecraft();
    public static JFrame LoadingFrame = new JFrame("Antimony Test Log Frame");
    public static JPanel Panel = new JPanel();
    public static TextArea text = new TextArea();
    public LoadScreen() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
