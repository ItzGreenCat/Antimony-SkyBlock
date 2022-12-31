package com.greencat.antimony.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class Scissor {
    public static void enableScissor(){
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }
    public static void disableScissor(){
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
    public static void cut(int x,int y,int w,int h){
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - h * factor), w * factor, h * factor);
    }
}
