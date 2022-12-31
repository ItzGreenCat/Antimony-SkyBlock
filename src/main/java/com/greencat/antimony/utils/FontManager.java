package com.greencat.antimony.utils;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontManager {
    //get fonts
    public static Font getGothamRoundedFont(float size) {
        Font font = null;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation(Antimony.MODID,"font/Gotham_Rounded_Medium.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            font = new Font("default", 0, +10);
        }
        return font;
    }
    private static Font getExpressaSerialFont(float size) {
        Font font = null;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation(Antimony.MODID,"font/ExpressaSerial_Bold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            font = new Font("default", 0, +10);
        }
        return font;
    }
    private static Font getSTXINWEIFont(float size) {
        Font font = null;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation(Antimony.MODID,"font/STXINWEI.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            font = new Font("default", 0, +10);
        }
        return font;
    }
    private static Font getQuicksandFont(float size) {
        Font font = null;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation(Antimony.MODID,"font/quicksand.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            font = new Font("default", 0, +10);
        }
        return font;
    }
    private static Font getQuicksandBoldFont(float size) {
        Font font = null;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation(Antimony.MODID,"font/quicksand_bold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            font = new Font("default", 0, +10);
        }
        return font;
    }
    //create custom font render
    public static MinecraftFontRenderer GothamRoundedFont = new MinecraftFontRenderer(getGothamRoundedFont(20),true);
    public static MinecraftFontRenderer STXINWEIFont = new MinecraftFontRenderer(getSTXINWEIFont(20),true);
    public static MinecraftFontRenderer QuicksandBoldFont = new MinecraftFontRenderer(getQuicksandBoldFont(17),true);
    public static MinecraftFontRenderer QuicksandFont = new MinecraftFontRenderer(getQuicksandFont(20),true);
    public static MinecraftFontRenderer QuicksandFont24 = new MinecraftFontRenderer(getQuicksandFont(24),true);
    public static MinecraftFontRenderer QuicksandFont35 = new MinecraftFontRenderer(getQuicksandFont(35),true);
    public static MinecraftFontRenderer QuicksandFont47 = new MinecraftFontRenderer(getQuicksandFont(47),true);
    public static MinecraftFontRenderer ExpressaSerialBigFont = new MinecraftFontRenderer(getExpressaSerialFont(40),true);
}
