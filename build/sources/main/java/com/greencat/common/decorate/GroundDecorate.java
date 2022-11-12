package com.greencat.antimony.common.decorate;

import com.greencat.Antimony;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GroundDecorate {
    public static void draw(double x,double y,double z,ResourceLocation texture){
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1, -20);
        GlStateManager.color(1.0F,1.0F,1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        //AxisAlignedBB box = new AxisAlignedBB(x + 2.5,y,z + 2.5,x - 2.5,y + 0.01,z - 2.5).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
        GL11.glLineWidth(1.0F);

        double viewerX = x - renderManager.viewerPosX;
        double viewerY = y - renderManager.viewerPosY;
        double viewerZ = z - renderManager.viewerPosZ;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS,DefaultVertexFormats.POSITION_TEX);
        /*worldRenderer.pos(box.minX, box.minY, box.minZ).tex(0,0).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).tex(0,1).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).tex(1,1).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).tex(1,0).endVertex();*/
        worldRenderer.pos(viewerX - 2.5, viewerY, viewerZ - 2.5).tex(0,0).endVertex();
        worldRenderer.pos(viewerX - 2.5, viewerY, viewerZ + 2.5).tex(0,1).endVertex();
        worldRenderer.pos(viewerX + 2.5, viewerY, viewerZ + 2.5).tex(1,1).endVertex();
        worldRenderer.pos(viewerX + 2.5, viewerY, viewerZ - 2.5).tex(1,0).endVertex();
        tessellator.draw();
        GlStateManager.disablePolygonOffset();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }
}
