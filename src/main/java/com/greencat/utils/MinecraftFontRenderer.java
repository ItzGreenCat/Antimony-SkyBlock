package com.greencat.utils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class MinecraftFontRenderer extends CFont{
    CharData[] boldChars = new CharData[256];
    CharData[] italicChars = new CharData[256];
    CharData[] boldItalicChars = new CharData[256];
    int[] colorCode = new int[32];
    String colorcodeIdentifiers = "0123456789abcdefklmnor";
    DynamicTexture texBold;
    DynamicTexture texItalic;
    DynamicTexture texItalicBold;
    public MinecraftFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
    }
    @Override
    public int drawSmoothString(String text, double x2, float y2, int color) {
        return (int)this.drawSmoothString(text, x2, y2, color, false);
    }
    public float drawSmoothString(String text, double x2, double y2, int color, boolean shadow) {
        x2 -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x2 *= 2.0 /* * scale*/;
        y2 = (y2 - 3.0) * 2.0/* * scale*/;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5 /* / scale*/, 0.5 /* / scale*/, 0.5  /* / scale*/);
        //GlStateManager.scale(0.5, 0.5, 0.5);
        //GlStateManager.scale(1,1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        int i2 = 0;
        while (i2 < size) {
            char character = text.charAt(i2);
            if (character == '\u00a7' && i2 < size) {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i2 + 1));
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0 || colorIndex > 15) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((float)(colorcode >> 16 & 255) / 255.0f, (float)(colorcode >> 8 & 255) / 255.0f, (float)(colorcode & 255) / 255.0f, alpha);
                } else if (colorIndex == 16) {
                    randomCase = true;
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texBold.getGlTextureId());
                        currentData = this.boldChars;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                        currentData = this.italicChars;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                }
                ++i2;
            } else if (character < currentData.length && character >= '\u0000') {
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x2, (float)y2);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x2, y2 + (double)(currentData[character].height / 2), x2 + (double)currentData[character].width - 8.0, y2 + (double)(currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x2, y2 + (double)currentData[character].height - 2.0, x2 + (double)currentData[character].width - 8.0, y2 + (double)currentData[character].height - 2.0, 1.0f);
                }
                x2 += (double)(currentData[character].width - 8.3f + this.charOffset);
            }
            ++i2;
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        return (float)x2 / 2.0f;
    }
    private void drawLine(double x2, double y2, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
}
