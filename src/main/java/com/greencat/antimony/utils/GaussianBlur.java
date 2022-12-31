package com.greencat.antimony.utils;

import com.greencat.antimony.utils.render.ShaderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniform1;

public class GaussianBlur {

    public static ShaderUtils blurShader = new ShaderUtils("misc/gaussian.frag");

    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void setupUniforms(float dir1, float dir2, float radius) {
        blurShader.setUniformi("textureIn", 0);
        blurShader.setUniformf("texelSize", 1.0F / (float) mc.displayWidth, 1.0F / (float) mc.displayHeight);
        blurShader.setUniformf("direction", dir1, dir2);
        blurShader.setUniformf("radius", radius);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(Utils.calculateGaussianValue(i, radius / 2));
        }

        weightBuffer.rewind();
        glUniform1(blurShader.getUniform("weights"), weightBuffer);
    }

    public static void renderBlur(float radius) {
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        framebuffer = Utils.createFrameBuffer(framebuffer);
        GlStateManager.pushAttrib();
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        blurShader.init();
        setupUniforms(1, 0, radius);

        Utils.bindTexture(mc.getFramebuffer().framebufferTexture);

        ShaderUtils.drawQuads();
        framebuffer.unbindFramebuffer();
        blurShader.unload();

        mc.getFramebuffer().bindFramebuffer(true);
        blurShader.init();
        setupUniforms(0, 1, radius);

        Utils.bindTexture(framebuffer.framebufferTexture);
        ShaderUtils.drawQuads();
        blurShader.unload();

        GlStateManager.resetColor();
        //GlStateManager.bindTexture(0);
        GlStateManager.popAttrib();
    }
	public static void drawBlurRect(int x,int y,int w,int h,int radius){
        ScaledResolution scale = new ScaledResolution(mc);
		GlStateManager.pushMatrix();
        int factor = scale.getScaleFactor();
        GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - h * factor), w * factor, h * factor);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
		renderBlur(radius);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();
	}
}