package com.greencat.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.common.function.ItemTranslate;
import com.greencat.common.mixins.EntityPlayerSPAccessor;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.sqrt;

public class Utils {
    public static float lastReportedPitch;
    public static int lastReportedSlot;
    public static ArrayList<Packet<?>> noEvent = new ArrayList();
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void BoxWithESP(BlockPos pos, Color c, boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if (Blend) {
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        BoxWithoutESPRender(box);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }

    public static void BoxWithoutESP(BlockPos pos, Color c, boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
        GlStateManager.disableLighting();
        if (Blend) {
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        BoxWithoutESPRender(box);
        GlStateManager.disableBlend();
    }

    public static void BoxWithESP(AxisAlignedBB aabb, Color c, boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if (Blend) {
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = aabb.offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        BoxWithoutESPRender(box);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }

    public static void OutlinedBoxWithESP(BlockPos pos, Color c, boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if (Blend) {
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }

    public static void OutlinedBoxWithESP(BlockPos pos, Color c, boolean Blend, float width) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if (Blend) {
            GlStateManager.enableBlend();
        }
        GL11.glLineWidth(width);
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GL11.glLineWidth(1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }

    public static void OutlinedBoxWithESP(AxisAlignedBB aabb, Color c, boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if (Blend) {
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = aabb.offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }

    public static void OutlinedBoxWithESP(AxisAlignedBB aabb, Color c, boolean Blend, float width) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if (Blend) {
            GlStateManager.enableBlend();
        }
        GL11.glLineWidth(width);
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = aabb.offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GL11.glLineWidth(1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }

    public static void BoxWithoutESPRender(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        tessellator.draw();


    }

    public static void OutlinedBoxWithoutESPRender(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        tessellator.draw();
    }

    public static String Dec2Hex(int dec) {
        StringBuilder sb = new StringBuilder();
        String Hex;
        char[] HexList = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (dec != 0) {
            sb = sb.append(HexList[dec % 16]);
            dec = dec / 16;
        }
        Hex = sb.reverse().toString();
        return Hex;
    }

    public static int Hex2Dec(String Hex) {
        Hex = Hex.replace(" ", "");
        int length = Hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(Hex.charAt(i), 16) << 4) + Character.digit(Hex.charAt(i + 1), 16));
        }
        return bytesToInt(bytes);
    }

    public static int bytesToInt(byte[] intByte) {
        int fromByte = 0;
        for (int i = 0; i < 2; i++) {
            int n = (intByte[i] < 0 ? (int) intByte[i] + 256 : (int) intByte[i]) << (8 * i);
            fromByte += n;
        }
        return fromByte;
    }

    public static NBTTagCompound getSkyBlockNBT(ItemStack stack) {
        if (stack != null) {
            return stack.getSubCompound("ExtraAttributes", false);
        }
        return null;
    }

    public static String getSkyBlockCustomItemExtraTag(ItemStack stack, String key) {
        NBTTagCompound itemNBT = Utils.getSkyBlockNBT(stack);
        if (itemNBT != null || itemNBT.hasKey(key)) {
            return itemNBT.getString(key);
        }
        return "不管怎么说反正就是没找到那个Key";
    }

    public static String getSkyBlockCustomItemID(ItemStack stack) {
        return Utils.getSkyBlockCustomItemExtraTag(stack, "id");
    }

    public static ItemStack getHeldItem() {
        return Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
    }

    public static ItemStack getBoots() {
        return Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(1);
    }

    public static ItemStack getLeggings() {
        return Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(2);
    }

    public static ItemStack getChestplate() {
        return Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(3);
    }

    public static ItemStack getHelmet() {
        return Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(4);
    }

    public static void renderText(String str, BlockPos pos, float partialTicks) {
        Vector3f loc = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
        GlStateManager.alphaFunc(516, 0.1F);

        GlStateManager.pushMatrix();

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = loc.x - viewerX + 0.5f;
        double y = loc.y - viewerY - viewer.getEyeHeight();
        double z = loc.z - viewerZ + 0.5f;

        double distSq = x * x + y * y + z * z;
        double dist = Math.sqrt(distSq);
        if (distSq > 144) {
            x *= 12 / dist;
            y *= 12 / dist;
            z *= 12 / dist;
        }
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0, viewer.getEyeHeight(), 0);

        renderNametag(str);

        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0, -0.25f, 0);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        renderNametag(EnumChatFormatting.LIGHT_PURPLE.toString() + Math.round(dist) + "米");

        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
    }

    public static void renderNametag(String str) {
        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
        GlStateManager.depthMask(true);

        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);

        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static boolean containedByCharSequence(String s1, String s2) {

        char[] c = s2.toCharArray();
        char[] s = s1.toCharArray();
        int cIdx = 0;
        for (int i = 0; i < s.length && cIdx < c.length; i++) {
            if (s[i] == c[cIdx]) cIdx++;
        }

        return cIdx == c.length;

    }

    public static void drawStringScaled(String str, FontRenderer fr, float x, float y, int colour, float scale) {
        GlStateManager.scale(scale, scale, 1);
        fr.drawString(str, (int) (x / scale), (int) (y / scale), colour);
        GlStateManager.scale(1 / scale, 1 / scale, 1);
    }

    public static float[] getServerAngles(Entity en) {
        return getServerAngles(new Vec3(en.posX, en.posY + ((double) en.getEyeHeight() - (double) en.height / 1.5D) + 0.5D, en.posZ));
    }

    public static float[] getServerAngles(Vec3 vec) {
        double diffX = vec.xCoord - Minecraft.getMinecraft().thePlayer.posX;
        double diffY = vec.yCoord - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double diffZ = vec.zCoord - Minecraft.getMinecraft().thePlayer.posZ;
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
        return new float[]{((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() + MathHelper.wrapAngleTo180_float(yaw - ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw()), ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() + MathHelper.wrapAngleTo180_float(pitch - ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch())};
    }

    public static boolean isWithinFOV(EntityLivingBase entity, double fov) {
        float yawDifference = Math.abs(getAngles(entity)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw);
        return (double) yawDifference < fov && (double) yawDifference > -fov;
    }

    public static boolean isWithinPitch(EntityLivingBase entity, double pitch) {
        float pitchDifference = Math.abs(getAngles(entity)[1] - Minecraft.getMinecraft().thePlayer.rotationPitch);
        return (double) pitchDifference < pitch && (double) pitchDifference > -pitch;
    }

    public static float[] getAngles(Entity en) {
        return getAngles(new Vec3(en.posX, en.posY + ((double) en.getEyeHeight() - (double) en.height / 1.5D) + 0.5D, en.posZ));
    }

    public static float[] getAngles(Vec3 vec) {
        double diffX = vec.xCoord - Minecraft.getMinecraft().thePlayer.posX;
        double diffY = vec.yCoord - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double diffZ = vec.zCoord - Minecraft.getMinecraft().thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
        return new float[]{Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)};
    }

    public static boolean isOnSkyBlock() {
        try {
            return Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(0) != null ? ChatFormatting.stripFormatting(Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(0).getDisplayName()).contains("SKYBLOCK") : ChatFormatting.stripFormatting(Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()).contains("SKYBLOCK");
        } catch (Exception var1) {
            return false;
        }
    }

    public static void updateItemNoEvent() {
        if (lastReportedSlot != Minecraft.getMinecraft().thePlayer.inventory.currentItem) {
            Utils.sendPacketNoEvent(new C09PacketHeldItemChange(Minecraft.getMinecraft().thePlayer.inventory.currentItem));
            lastReportedSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
        }

    }

    public static void sendPacketNoEvent(Packet<?> packet) {
        noEvent.add(packet);
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static boolean isNPC(Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        } else {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            return entity.getUniqueID().version() == 2 && entityLivingBase.getHealth() == 20.0F;
        }
    }

    public static boolean isTeamMember(EntityLivingBase e, EntityLivingBase e2) {
        if (e.getDisplayName().getUnformattedText().length() < 4) {
            return false;
        } else if (e.getDisplayName().getFormattedText().charAt(2) == 167 && e2.getDisplayName().getFormattedText().charAt(2) == 167) {
            if (isOnSkyBlock()) {
                return true;
            } else {
                return e.getDisplayName().getFormattedText().charAt(3) == e2.getDisplayName().getFormattedText().charAt(3);
            }
        } else {
            return false;
        }
    }

    public static boolean isMoving() {
        return Minecraft.getMinecraft().thePlayer != null && (Minecraft.getMinecraft().thePlayer.moveForward != 0.0F || Minecraft.getMinecraft().thePlayer.moveStrafing != 0.0F);
    }

    public static void RenderTargetESP(EntityLivingBase entity, Color c, float width, float size, Double[] currentHeight, Boolean[] currentStatus) {
        try {
            RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GL11.glLineWidth(width);
            GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
            double x = entity.posX - renderManager.viewerPosX;
            double y = entity.getEntityBoundingBox().minY - renderManager.viewerPosY;
            double z = entity.posZ - renderManager.viewerPosZ;
            double entityHeight = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY;

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer wr = tessellator.getWorldRenderer();
            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x + (size / 2), y, z + size).endVertex();
            wr.pos(x + size, y, z + (size / 2)).endVertex();
            wr.pos(x + size, y, z - (size / 2)).endVertex();
            wr.pos(x + (size / 2), y, z - size).endVertex();
            wr.pos(x - (size / 2), y, z - size).endVertex();
            wr.pos(x - size, y, z - (size / 2)).endVertex();
            wr.pos(x - size, y, z + (size / 2)).endVertex();
            wr.pos(x - (size / 2), y, z + size).endVertex();
            wr.pos(x + (size / 2), y, z + size).endVertex();
            tessellator.draw();
            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x + (size / 2), y + entityHeight, z + size).endVertex();
            wr.pos(x + size, y + entityHeight, z + (size / 2)).endVertex();
            wr.pos(x + size, y + entityHeight, z - (size / 2)).endVertex();
            wr.pos(x + (size / 2), y + entityHeight, z - size).endVertex();
            wr.pos(x - (size / 2), y + entityHeight, z - size).endVertex();
            wr.pos(x - size, y + entityHeight, z - (size / 2)).endVertex();
            wr.pos(x - size, y + entityHeight, z + (size / 2)).endVertex();
            wr.pos(x - (size / 2), y + entityHeight, z + size).endVertex();
            wr.pos(x + (size / 2), y + entityHeight, z + size).endVertex();
            tessellator.draw();
            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x + (size / 2), y, z + size).endVertex();
            wr.pos(x + (size / 2), y + entityHeight, z + size).endVertex();
            tessellator.draw();

            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x + size, y, z + (size / 2)).endVertex();
            wr.pos(x + size, y + entityHeight, z + (size / 2)).endVertex();
            tessellator.draw();

            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x + size, y, z - (size / 2)).endVertex();
            wr.pos(x + size, y + entityHeight, z - (size / 2)).endVertex();
            tessellator.draw();

            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x + (size / 2), y, z - size).endVertex();
            wr.pos(x + (size / 2), y + entityHeight, z - size).endVertex();
            tessellator.draw();

            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x - (size / 2), y, z - size).endVertex();
            wr.pos(x - (size / 2), y + entityHeight, z - size).endVertex();
            tessellator.draw();

            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x - size, y, z - (size / 2)).endVertex();
            wr.pos(x - size, y + entityHeight, z - (size / 2)).endVertex();
            tessellator.draw();

            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x - size, y, z + (size / 2)).endVertex();
            wr.pos(x - size, y + entityHeight, z + (size / 2)).endVertex();
            tessellator.draw();

            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x - (size / 2), y, z + size).endVertex();
            wr.pos(x - (size / 2), y + entityHeight, z + size).endVertex();
            tessellator.draw();
            if (currentStatus[0] && currentHeight[0] + 0.02 > entityHeight) {
                currentStatus[0] = false;
            }
            if (!currentStatus[0] && currentHeight[0] - 0.02 < 0) {
                currentStatus[0] = true;
            }
            if (currentStatus[0]) {
                currentHeight[0] = currentHeight[0] + 0.02;
            } else {
                currentHeight[0] = currentHeight[0] - 0.02;
            }
            wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            wr.pos(x + (size / 2), y + currentHeight[0], z + size).endVertex();
            wr.pos(x + size, y + currentHeight[0], z + (size / 2)).endVertex();
            wr.pos(x + size, y + currentHeight[0], z - (size / 2)).endVertex();
            wr.pos(x + (size / 2), y + currentHeight[0], z - size).endVertex();
            wr.pos(x - (size / 2), y + currentHeight[0], z - size).endVertex();
            wr.pos(x - size, y + currentHeight[0], z - (size / 2)).endVertex();
            wr.pos(x - size, y + currentHeight[0], z + (size / 2)).endVertex();
            wr.pos(x - (size / 2), y + currentHeight[0], z + size).endVertex();
            wr.pos(x + (size / 2), y + currentHeight[0], z + size).endVertex();
            tessellator.draw();


            GL11.glLineWidth(1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print(String message) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[" + EnumChatFormatting.WHITE + "Antimony" + EnumChatFormatting.LIGHT_PURPLE + "] " + message + "."));
        }
    }

    public void printAntimonyChannel(String message) {
        if (FunctionManager.getStatus("AntimonyChannel")) {
            if (Minecraft.getMinecraft().theWorld != null) {
                try {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[" + EnumChatFormatting.WHITE + "ⒶⓂⒸ" + EnumChatFormatting.LIGHT_PURPLE + "] -> " + EnumChatFormatting.GOLD + message.split("MSG-!-SPLIT")[0] + EnumChatFormatting.WHITE + ": " + message.split("MSG-!-SPLIT")[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void devLog(String message) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "[" + EnumChatFormatting.GOLD + "Antimony" + EnumChatFormatting.DARK_RED + "Dev" + EnumChatFormatting.DARK_GREEN + "]" + EnumChatFormatting.YELLOW + message + "."));
        }
    }

    public Boolean ModLoadCheck(String ModID) {
        Boolean isLoaded = Loader.isModLoaded(ModID);
        return isLoaded;
    }

    public double FlatAngle(double x1, double z1, double x2, double z2) {
        double AX = Math.abs(x1 - x2);
        double AZ = Math.abs(z1 - z2);
        double angle = Math.round(Math.asin(AZ / sqrt(AX * AX + AZ * AZ)) / Math.PI * 180);
        if (x1 > x2 && z1 > z2) {
            return angle + 90;
        } else if (x1 < x2 && z1 < z2) {
            return angle - 90;
        } else if (x1 > x2 && z1 < z2) {
            return 90 - angle;
        } else if (x1 < x2 && z1 > z2) {
            return 0 - (angle - 270);
        }
        return 0;
    }

    public double ErectAngleX(double x1, double y1, double x2, double y2) {
        double AX = Math.abs(x1 - x2);
        double AY = Math.abs(y1 - y2);
        double angle = Math.round(Math.asin(AY / sqrt(AX * AX + AY * AY)) / Math.PI * 180);
        if (y1 < y2) {
            return 0 - angle;
        }
        if (y1 > y2) {
            return angle;
        }
        return 0;
    }

    public double ErectAngleZ(double z1, double y1, double z2, double y2) {
        double AZ = Math.abs(z1 - z2);
        double AY = Math.abs(y1 - y2);
        double angle = Math.round(Math.asin(AY / sqrt(AZ * AZ + AY * AY)) / Math.PI * 180);
        if (y1 < y2) {
            return 0 - angle;
        }
        if (y1 > y2) {
            return angle;
        }
        return 0;
    }

    public double ErectAngle(double[] Location1, double[] Location2) {
        if (Minecraft.getMinecraft().thePlayer.rotationYaw <= 135 && Minecraft.getMinecraft().thePlayer.rotationYaw > 45) {
            return ErectAngleX(Location1[0], Location1[1], Location2[0], Location2[1]);
        } else if (Minecraft.getMinecraft().thePlayer.rotationYaw <= -45 && Minecraft.getMinecraft().thePlayer.rotationYaw > -90) {
            return ErectAngleX(Location1[0], Location1[1], Location2[0], Location2[1]);
        } else if (Minecraft.getMinecraft().thePlayer.rotationYaw <= -90 && Minecraft.getMinecraft().thePlayer.rotationYaw > -135) {
            return ErectAngleZ(Location1[0], Location1[1], Location2[0], Location2[1]);
        } else {
            return ErectAngleZ(Location1[2], Location1[1], Location2[2], Location2[1]);
        }
    }

    public boolean RangeInDefined(double number, double range1, double range2) {
        return number > range1 && number < range2;
    }

    public List<String> getSidebarLines() {

        List<String> lines = new ArrayList<String>();
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) {
            return lines;
        }

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);

        if (objective == null) {
            return lines;
        }

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> list = Lists.newArrayList(Iterables.filter(scores, new Predicate<Score>() {
            public boolean apply(Score s) {
                return s.getPlayerName() != null;
            }
        }));

        if (list.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        } else {
            scores = list;
        }

        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }

        return lines;
    }

    @SubscribeEvent
    public void onPacketSent(CustomEventHandler.PacketSentEvent event) {
        if (event.packet instanceof C09PacketHeldItemChange) {
            lastReportedSlot = ((C09PacketHeldItemChange) event.packet).getSlotId();
        }
    }
    public static void drawBorderedRoundedRect(float x, float y, float width, float height, float radius, float linewidth, int insideC, int borderC) {
        drawRoundRect(x, y, x + width, y + height, radius, insideC);
        drawOutlinedRoundedRect(x, y, width, height, radius, linewidth, borderC);
    }

    public static void drawOutlinedRoundedRect(float x, float y, float width, float height, float radius, float linewidth, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = (double)(x + width);
        double y1 = (double)(y + height);
        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0F;
        y *= 2.0F;
        x1 *= 2.0D;
        y1 *= 2.0D;
        GL11.glLineWidth(linewidth);
        GL11.glDisable(3553);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glEnable(2848);
        GL11.glBegin(2);

        int i;
        for(i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius) + Math.sin((double)i * 3.141592653589793D / 180.0D) * (double)(radius * -1.0F), (double)(y + radius) + Math.cos((double)i * 3.141592653589793D / 180.0D) * (double)(radius * -1.0F));
        }

        for(i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x + radius) + Math.sin((double)i * 3.141592653589793D / 180.0D) * (double)(radius * -1.0F), y1 - (double)radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * (double)(radius * -1.0F));
        }

        for(i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - (double)radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * (double)radius, y1 - (double)radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * (double)radius);
        }

        for(i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - (double)radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * (double)radius, (double)(y + radius) + Math.cos((double)i * 3.141592653589793D / 180.0D) * (double)radius);
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    public static void drawRoundRect(float left, float top, float right, float bottom, float radius, int color) {
        left += radius;
        right -= radius;
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
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)(right - radius), (double)(top - radius), 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)(top - radius), 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)(bottom + radius), 0.0D).endVertex();
        worldrenderer.pos((double)(right - radius), (double)(bottom + radius), 0.0D).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)(top - radius), 0.0D).endVertex();
        worldrenderer.pos((double)(left + radius), (double)(top - radius), 0.0D).endVertex();
        worldrenderer.pos((double)(left + radius), (double)(bottom + radius), 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)(bottom + radius), 0.0D).endVertex();
        tessellator.draw();
        drawArc(right, bottom + radius, radius, 180);
        drawArc(left, bottom + radius, radius, 90);
        drawArc(right, top - radius, radius, 270);
        drawArc(left, top - radius, radius, 0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    public static void drawArc(float x, float y, float radius, int angleStart) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        GlStateManager.translate((double)x, (double)y, 0.0D);
        worldrenderer.pos(0.0D, 0.0D, 0.0D).endVertex();
        int points = 21;

        for(double i = 0.0D; i < (double)points; ++i) {
            double radians = Math.toRadians(i / (double)points * 90.0D + (double)angleStart);
            worldrenderer.pos((double)radius * Math.sin(radians), (double)radius * Math.cos(radians), 0.0D).endVertex();
        }

        tessellator.draw();
        GlStateManager.translate((double)(-x), (double)(-y), 0.0D);
    }

    //Jrojro728改变开始

    /**
     * @param FilePath Json文件路径，可以是全局文件，也可以是存储在resources的。
     * @return 读取到的HashMap对象
     */
    public static HashMap<String, String> getHashMapInJsonFile(String FilePath) {
        InputStream path = Utils.class.getResourceAsStream(FilePath);
        StringBuffer content = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(path));
            String line = null;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        return gson.fromJson(content.toString(), type);
    }
    //Jrojro728改变结束
}
