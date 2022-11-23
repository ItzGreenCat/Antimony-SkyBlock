package com.greencat.antimony.utils;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontManager {
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
    public static CustomFont getCustomSizeGothamRounded(int scaling){
        return CustomFont.createFontRenderer(getGothamRoundedFont(20 * scaling));
    }
    public static CustomFont GothamRoundedFont = CustomFont.createFontRenderer(getGothamRoundedFont(20));
    public static CustomFont STXINWEIFont = CustomFont.createFontRenderer(getSTXINWEIFont(20));
    public static CustomFont QuicksandFont = CustomFont.createFontRenderer(getQuicksandFont(20));
    public static CustomFont QuicksandFont35 = CustomFont.createFontRenderer(getQuicksandFont(35));
    public static CustomFont ExpressaSerialBigFont = CustomFont.createFontRenderer(getExpressaSerialFont(40));
}
