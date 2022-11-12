package com.greencat.antimony.utils;

import java.awt.*;

public class CustomFont {
    public static CustomFont createFontRenderer(Font font) {
        return new MinecraftFontRenderer(font, true, true);
    }
    public int drawSmoothString(String text, double x, float y, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
    public int getHeight() {
        return 0;
    }
    public double getStringWidth(String text) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
}
