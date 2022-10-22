package com.greencat.utils;

import com.greencat.common.mixins.ShaderGroupAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.Matrix4f;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.util.*;

public class Blur {
    static Minecraft mc = Minecraft.getMinecraft();
    private final ResourceLocation shader = new ResourceLocation("shaders/post/CustomBlur.json");
    private ShaderGroup blurShader;
    private int lastScale;
    private int lastScaleWidth;
    private int lastScaleHeight;
    private  Framebuffer buffer;
    private static HashMap<Float, Blur.OutputStuff> blurOutput = new HashMap();
    private static HashMap<Float, Long> lastBlurUse = new HashMap();
    private static HashSet<Float> requestedBlurs = new HashSet();
    private static int fogColour = 0;
    private static boolean registered = false;
    private static Framebuffer blurOutputHorz = null;

    public void initFboAndShader() {
        try {

            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            buffer = ((ShaderGroupAccessor) blurShader).getBuffer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setShaderConfigs(float intensity, float blurWidth, float blurHeight, float opacity) {
        try {
            ((ShaderGroupAccessor) blurShader).getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
            ((ShaderGroupAccessor) blurShader).getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);

            ((ShaderGroupAccessor) blurShader).getShaders().get(0).getShaderManager().getShaderUniform("Opacity").set(opacity);
            ((ShaderGroupAccessor) blurShader).getShaders().get(1).getShaderManager().getShaderUniform("Opacity").set(opacity);

            ((ShaderGroupAccessor) blurShader).getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
            ((ShaderGroupAccessor) blurShader).getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
        } catch (Exception e){
                e.printStackTrace();
            }
    }

    public void blurAreaBoarder(float x, float y, float width, float height, float intensity, float opacity, float blurWidth,
                                       float blurHeight) {
        try {
            ScaledResolution scale = new ScaledResolution(mc);
            int factor = scale.getScaleFactor();
            int factor2 = scale.getScaledWidth();
            int factor3 = scale.getScaledHeight();
            if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                    || blurShader == null) {
                initFboAndShader();
            }
            lastScale = factor;
            lastScaleWidth = factor2;
            lastScaleHeight = factor3;

            GL11.glScissor((int) (x * factor), (int) ((mc.displayHeight - (y * factor) - height * factor)) + 1, (int) (width * factor),
                    (int) (height * factor));
            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            setShaderConfigs(intensity, blurWidth, blurHeight, opacity);
            buffer.bindFramebuffer(true);
            Minecraft.getMinecraft().entityRenderer.loadShader(shader);

            mc.getFramebuffer().bindFramebuffer(true);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void stopBlur(){
        mc.entityRenderer.stopUseShader();
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new Blur());
    }
    public static void processBlurs() {
        long currentTime = System.currentTimeMillis();

        float blur;
        Blur.OutputStuff output;
        for(Iterator var2 = requestedBlurs.iterator(); var2.hasNext(); blurBackground(output, blur)) {
            blur = (Float)var2.next();
            lastBlurUse.put(blur, currentTime);
            int width = Minecraft.getMinecraft().displayWidth;
            int height = Minecraft.getMinecraft().displayHeight;
            output = (Blur.OutputStuff)blurOutput.computeIfAbsent(blur, (k) -> {
                Framebuffer fb = new Framebuffer(width, height, false);
                fb.setFramebufferFilter(9728);
                return new Blur.OutputStuff(fb, (Shader)null, (Shader)null);
            });
            if (output.framebuffer.framebufferWidth != width || output.framebuffer.framebufferHeight != height) {
                output.framebuffer.createBindFramebuffer(width, height);
                if (output.blurShaderHorz != null) {
                    output.blurShaderHorz.setProjectionMatrix(createProjectionMatrix(width, height));
                }

                if (output.blurShaderVert != null) {
                    output.blurShaderVert.setProjectionMatrix(createProjectionMatrix(width, height));
                }
            }
        }

        Set<Float> remove = new HashSet();
        Iterator var8 = lastBlurUse.entrySet().iterator();

        Map.Entry entry;
        while(var8.hasNext()) {
            entry = (Map.Entry)var8.next();
            if (currentTime - (Long)entry.getValue() > 30000L) {
                remove.add((Float) entry.getKey());
            }
        }

        var8 = blurOutput.entrySet().iterator();

        while(var8.hasNext()) {
            entry = (Map.Entry)var8.next();
            if (remove.contains(entry.getKey())) {
                ((Blur.OutputStuff)entry.getValue()).framebuffer.deleteFramebuffer();
                ((Blur.OutputStuff)entry.getValue()).blurShaderHorz.deleteShader();
                ((Blur.OutputStuff)entry.getValue()).blurShaderVert.deleteShader();
            }
        }

        lastBlurUse.keySet().removeAll(remove);
        blurOutput.keySet().removeAll(remove);
        requestedBlurs.clear();
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public void onScreenRender(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            processBlurs();
        }

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(false);
    }

    private static Matrix4f createProjectionMatrix(int width, int height) {
        Matrix4f projMatrix = new Matrix4f();
        projMatrix.setIdentity();
        projMatrix.m00 = 2.0F / (float)width;
        projMatrix.m11 = 2.0F / (float)(-height);
        projMatrix.m22 = -0.0020001999F;
        projMatrix.m33 = 1.0F;
        projMatrix.m03 = -1.0F;
        projMatrix.m13 = 1.0F;
        projMatrix.m23 = -1.0001999F;
        return projMatrix;
    }

    private static void blurBackground(Blur.OutputStuff output, float blurFactor) {
        if (OpenGlHelper.isFramebufferEnabled() && OpenGlHelper.areShadersSupported()) {
            int width = Minecraft.getMinecraft().displayWidth;
            int height = Minecraft.getMinecraft().displayHeight;
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, (double)width, (double)height, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            if (blurOutputHorz == null) {
                blurOutputHorz = new Framebuffer(width, height, false);
                blurOutputHorz.setFramebufferFilter(9728);
            }

            if (blurOutputHorz != null && output != null) {
                if (blurOutputHorz.framebufferWidth != width || blurOutputHorz.framebufferHeight != height) {
                    blurOutputHorz.createBindFramebuffer(width, height);
                    Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(false);
                }

                if (output.blurShaderHorz == null) {
                    try {
                        output.blurShaderHorz = new Shader(Minecraft.getMinecraft().getResourceManager(), "blur", output.framebuffer, blurOutputHorz);
                        output.blurShaderHorz.getShaderManager().getShaderUniform("BlurDir").set(1.0F, 0.0F);
                        output.blurShaderHorz.setProjectionMatrix(createProjectionMatrix(width, height));
                    } catch (Exception var6) {
                    }
                }

                if (output.blurShaderVert == null) {
                    try {
                        output.blurShaderVert = new Shader(Minecraft.getMinecraft().getResourceManager(), "blur", blurOutputHorz, output.framebuffer);
                        output.blurShaderVert.getShaderManager().getShaderUniform("BlurDir").set(0.0F, 1.0F);
                        output.blurShaderVert.setProjectionMatrix(createProjectionMatrix(width, height));
                    } catch (Exception var5) {
                    }
                }

                if (output.blurShaderHorz != null && output.blurShaderVert != null) {
                    if (output.blurShaderHorz.getShaderManager().getShaderUniform("Radius") == null) {
                        return;
                    }

                    output.blurShaderHorz.getShaderManager().getShaderUniform("Radius").set(blurFactor);
                    output.blurShaderVert.getShaderManager().getShaderUniform("Radius").set(blurFactor);
                    GL11.glPushMatrix();
                    GL30.glBindFramebuffer(36008, Minecraft.getMinecraft().getFramebuffer().framebufferObject);
                    GL30.glBindFramebuffer(36009, output.framebuffer.framebufferObject);
                    GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, output.framebuffer.framebufferWidth, output.framebuffer.framebufferHeight, 16384, 9728);
                    output.blurShaderHorz.loadShader(0.0F);
                    output.blurShaderVert.loadShader(0.0F);
                    GlStateManager.enableDepth();
                    GL11.glPopMatrix();
                }

                Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(false);
            }
        }
    }

    public static void renderBlur(float x, float y, float blurWidth, float blurHeight,float blurStrength) {
        ScaledResolution resolution = new ScaledResolution(mc);
        int screenWidth = resolution.getScaledWidth();
        int screenHeight = resolution.getScaledHeight();
        if (OpenGlHelper.isFramebufferEnabled() && OpenGlHelper.areShadersSupported()) {
            if (!((double)blurStrength < 0.5D)) {
                requestedBlurs.add(blurStrength);
                if (!blurOutput.isEmpty()) {
                    Blur.OutputStuff out = blurOutput.get(blurStrength);
                    if (out == null) {
                        out = blurOutput.values().iterator().next();
                    }

                    float uMin = x / (float)screenWidth;
                    float uMax = (x + blurWidth) / (float)screenWidth;
                    float vMin = ((float)screenHeight - y) / (float)screenHeight;
                    float vMax = ((float)screenHeight - y - blurHeight) / (float)screenHeight;
                    GlStateManager.depthMask(false);
                    drawRect(x, y, x + blurWidth, y + blurHeight, fogColour);
                    out.framebuffer.bindFramebufferTexture();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    drawTexturedRect(x, y, blurWidth, blurHeight, uMin, uMax, vMin, vMax, 9728);
                    out.framebuffer.unbindFramebufferTexture();
                    GlStateManager.depthMask(true);
                    GlStateManager.resetColor();
                }
            }
        }
    }
    public static void drawTexturedRect(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax, int filter) {
        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(770, 771, 1, 771);
        drawTexturedRectNoBlend(x, y, width, height, uMin, uMax, vMin, vMax, filter);
        GlStateManager.disableBlend();
    }

    public static void drawTexturedRectNoBlend(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax, int filter) {
        GlStateManager.enableTexture2D();
        GL11.glTexParameteri(3553, 10241, filter);
        GL11.glTexParameteri(3553, 10240, filter);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)uMin, (double)vMax).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)uMax, (double)vMax).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)uMax, (double)vMin).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)uMin, (double)vMin).endVertex();
        tessellator.draw();
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
    }
    private static class OutputStuff {
        public Framebuffer framebuffer;
        public Shader blurShaderHorz = null;
        public Shader blurShaderVert = null;

        public OutputStuff(Framebuffer framebuffer, Shader blurShaderHorz, Shader blurShaderVert) {
            this.framebuffer = framebuffer;
            this.blurShaderHorz = blurShaderHorz;
            this.blurShaderVert = blurShaderVert;
        }
    }
    public static void drawRect(float left, float top, float right, float bottom, int color) {
        float f3;
        if (left < right) {
            f3 = left;
            left = right;
            right = f3;
        }

        if (top < bottom) {
            f3 = top;
            top = bottom;
            bottom = f3;
        }

        f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


}
