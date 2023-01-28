package com.greencat.antimony.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.Pathfinding;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.common.mixins.EntityPlayerSPAccessor;
import com.greencat.antimony.core.type.Rotation;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.Math.*;

public class Utils {
    public static float lastReportedPitch;
    public static int lastReportedSlot;
    public static ArrayList<Packet<?>> noEvent = new ArrayList();
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Utils() {
        keyBindMap.put(0, Minecraft.getMinecraft().gameSettings.keyBindForward);
        keyBindMap.put(90, Minecraft.getMinecraft().gameSettings.keyBindLeft);
        keyBindMap.put(180, Minecraft.getMinecraft().gameSettings.keyBindBack);
        keyBindMap.put(270, Minecraft.getMinecraft().gameSettings.keyBindRight);
    }
    public static void swingItem() {
        MovingObjectPosition movingObjectPosition = Minecraft.getMinecraft().objectMouseOver;
        if (movingObjectPosition != null && movingObjectPosition.entityHit == null) {
            Minecraft.getMinecraft().thePlayer.swingItem();
        }
    }
    public static float[] getRotation(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - Minecraft.getMinecraft().thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - Minecraft.getMinecraft().thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double d1 = Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight() - ((double)block.getY() + 0.5);
        double d3 = (double)MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360.0F;
        }

        return new float[]{yaw, pitch};
    }
    public static EnumFacing getClosestEnum(BlockPos pos) {
        EnumFacing closestEnum = EnumFacing.UP;
        float rotations = MathHelper.wrapAngleTo180_float(getRotation(pos, EnumFacing.UP)[0]);
        if (rotations >= 45.0F && rotations <= 135.0F) {
            closestEnum = EnumFacing.EAST;
        } else if ((!(rotations >= 135.0F) || !(rotations <= 180.0F)) && (!(rotations <= -135.0F) || !(rotations >= -180.0F))) {
            if (rotations <= -45.0F && rotations >= -135.0F) {
                closestEnum = EnumFacing.WEST;
            } else if (rotations >= -45.0F && rotations <= 0.0F || rotations <= 45.0F && rotations >= 0.0F) {
                closestEnum = EnumFacing.NORTH;
            }
        } else {
            closestEnum = EnumFacing.SOUTH;
        }

        if (MathHelper.wrapAngleTo180_float(getRotation(pos, EnumFacing.UP)[1]) > 75.0F || MathHelper.wrapAngleTo180_float(getRotation(pos, EnumFacing.UP)[1]) < -75.0F) {
            closestEnum = EnumFacing.UP;
        }

        return closestEnum;
    }
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

    public static void drawBorderedRoundedRect(float x, float y, float width, float height, float radius, float linewidth, int insideC, int borderC) {
        drawRoundRect(x, y, x + width, y + height, radius, insideC);
        drawOutlinedRoundedRect(x, y, width, height, radius, linewidth, borderC);
    }

    public static void drawOutlinedRoundedRect(float x, float y, float width, float height, float radius, float linewidth, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
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
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double) (x + radius) + sin((double) i * 3.141592653589793D / 180.0D) * (double) (radius * -1.0F), (double) (y + radius) + cos((double) i * 3.141592653589793D / 180.0D) * (double) (radius * -1.0F));
        }

        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double) (x + radius) + sin((double) i * 3.141592653589793D / 180.0D) * (double) (radius * -1.0F), y1 - (double) radius + cos((double) i * 3.141592653589793D / 180.0D) * (double) (radius * -1.0F));
        }

        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - (double) radius + sin((double) i * 3.141592653589793D / 180.0D) * (double) radius, y1 - (double) radius + cos((double) i * 3.141592653589793D / 180.0D) * (double) radius);
        }

        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - (double) radius + sin((double) i * 3.141592653589793D / 180.0D) * (double) radius, (double) (y + radius) + cos((double) i * 3.141592653589793D / 180.0D) * (double) radius);
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

        f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(right - radius, top - radius, 0.0D).endVertex();
        worldrenderer.pos(right, top - radius, 0.0D).endVertex();
        worldrenderer.pos(right, bottom + radius, 0.0D).endVertex();
        worldrenderer.pos(right - radius, bottom + radius, 0.0D).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, top - radius, 0.0D).endVertex();
        worldrenderer.pos(left + radius, top - radius, 0.0D).endVertex();
        worldrenderer.pos(left + radius, bottom + radius, 0.0D).endVertex();
        worldrenderer.pos(left, bottom + radius, 0.0D).endVertex();
        tessellator.draw();
        drawArc(right, bottom + radius, radius, 180);
        drawArc(left, bottom + radius, radius, 90);
        drawArc(right, top - radius, radius, 270);
        drawArc(left, top - radius, radius, 0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundRect2(float x, float y, float width, float height, float radius, int color) {
        width += x;
        x += radius;
        width -= radius;
        float f3;
        if (x < width) {
            f3 = x;
            x = width;
            width = f3;
        }

        height += y;
        if (y < height) {
            f3 = y;
            y = height;
            height = f3;
        }

        f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, height, 0.0D).endVertex();
        worldrenderer.pos(width, height, 0.0D).endVertex();
        worldrenderer.pos(width, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(width - radius, y - radius, 0.0D).endVertex();
        worldrenderer.pos(width, y - radius, 0.0D).endVertex();
        worldrenderer.pos(width, height + radius, 0.0D).endVertex();
        worldrenderer.pos(width - radius, height + radius, 0.0D).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y - radius, 0.0D).endVertex();
        worldrenderer.pos(x + radius, y - radius, 0.0D).endVertex();
        worldrenderer.pos(x + radius, height + radius, 0.0D).endVertex();
        worldrenderer.pos(x, height + radius, 0.0D).endVertex();
        tessellator.draw();
        drawArc(width, height + radius, radius, 180);
        drawArc(x, height + radius, radius, 90);
        drawArc(width, y - radius, radius, 270);
        drawArc(x, y - radius, radius, 0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawArc(float x, float y, float radius, int angleStart) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        GlStateManager.translate(x, y, 0.0D);
        worldrenderer.pos(0.0D, 0.0D, 0.0D).endVertex();
        int points = 21;

        for (double i = 0.0D; i < (double) points; ++i) {
            double radians = Math.toRadians(i / (double) points * 90.0D + (double) angleStart);
            worldrenderer.pos((double) radius * sin(radians), (double) radius * cos(radians), 0.0D).endVertex();
        }

        tessellator.draw();
        GlStateManager.translate(-x, -y, 0.0D);
    }

    public static void print(String message) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[" + EnumChatFormatting.WHITE + "Antimony" + EnumChatFormatting.AQUA + "]" + EnumChatFormatting.WHITE + " -> " + message + "."));
        }
    }

    public static void printAntimonyChannel(String name,String message) {
        if (FunctionManager.getStatus("AntimonyChannel")) {
            if (Minecraft.getMinecraft().theWorld != null) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[" + EnumChatFormatting.WHITE + "ⒶⓂⒸ" + EnumChatFormatting.AQUA + "] -> " + EnumChatFormatting.GOLD + name + EnumChatFormatting.WHITE + ": " + message));
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

    public static float[] getBowAngles(Entity entity) {
        double xDelta = (entity.posX - entity.lastTickPosX) * 0.4D;
        double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4D;
        double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8D;
        double xMulti = d / 0.8D * xDelta;
        double zMulti = d / 0.8D * zDelta;
        double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
        double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
        double y = Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight() - (entity.posY + (double) entity.getEyeHeight());
        double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0F;
        double d1 = MathHelper.sqrt_double(x * x + z * z);
        float pitch = (float) (-(Math.atan2(y, d1) * 180.0D / 3.141592653589793D)) + (float) dist * 0.11F;
        return new float[]{yaw, -pitch};
    }

    public static float getYawDifference(EntityLivingBase entity1, EntityLivingBase entity2) {
        return Math.abs(getAngles(entity1)[0] - getAngles(entity2)[0]);
    }

    public static float getYawDifference(EntityLivingBase entity1) {
        return Math.abs(Minecraft.getMinecraft().thePlayer.rotationYaw - getAngles(entity1)[0]);
    }

    private static final HashMap<Integer, String> itemUuidCache = new HashMap<>();

    public static String getUuidForItem(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;

        int nbtHash = stack.getTagCompound().hashCode();

        if (itemUuidCache.containsKey(nbtHash)) {
            return itemUuidCache.get(nbtHash);
        }

        String uuid = getUUIDForItem(stack);

        itemUuidCache.put(nbtHash, uuid);
        return uuid;
    }

    public static String getUUIDForItem(ItemStack stack) {
        if (stack == null) return null;
        NBTTagCompound tag = stack.getTagCompound();
        return getUUIDFromNBT(tag);
    }

    public static String getUUIDFromNBT(NBTTagCompound tag) {
        String uuid = null;
        if (tag != null && tag.hasKey("ExtraAttributes", 10)) {
            NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

            if (ea.hasKey("uuid", 8)) {
                uuid = ea.getString("uuid");
            }
        }
        return uuid;
    }
    public static Vec3 ceilVec(Vec3 vec3) {
        return new Vec3(Math.ceil(vec3.xCoord), Math.ceil(vec3.yCoord), Math.ceil(vec3.zCoord));
    }

    public static Vec3 floorVec(Vec3 vec3) {
        return new Vec3(Math.floor(vec3.xCoord), Math.floor(vec3.yCoord), Math.floor(vec3.zCoord));
    }

    public static Rotation startRot;
    public static Rotation neededChange;
    public static Rotation endRot;
    public static long startTime;
    public static long endTime;
    public static boolean done = true;
    private static final float[][] BLOCK_SIDES = new float[][]{{0.5F, 0.01F, 0.5F}, {0.5F, 0.99F, 0.5F}, {0.01F, 0.5F, 0.5F}, {0.99F, 0.5F, 0.5F}, {0.5F, 0.5F, 0.01F}, {0.5F, 0.5F, 0.99F}};

    public static Rotation getRotation(Vec3 vec) {
        Vec3 eyes = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0F);
        return getRotation(eyes, vec);
    }

    public static Rotation getRotation(BlockPos bp) {
        Vec3 vec = new Vec3((double) bp.getX() + 0.5D, (double) bp.getY() + 0.5D, (double) bp.getZ() + 0.5D);
        return getRotation(vec);
    }

    public static Rotation getRotation(Vec3 from, Vec3 to) {
        double diffX = to.xCoord - from.xCoord;
        double diffY = to.yCoord - from.yCoord;
        double diffZ = to.zCoord - from.zCoord;
        return new Rotation(MathHelper.wrapAngleTo180_float((float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D)), (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))));
    }

    public static void setup(Rotation rot, Long aimTime) {
        Minecraft mc = Minecraft.getMinecraft();
        done = false;
        startRot = new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        neededChange = getNeededChange(startRot, rot);
        endRot = new Rotation(startRot.getYaw() + neededChange.getYaw(), startRot.getPitch() + neededChange.getPitch());
        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis() + aimTime;
    }

    public static Rotation getNeededChange(Rotation startRot, Rotation endRot) {
        float yawChng = MathHelper.wrapAngleTo180_float(endRot.getYaw()) - MathHelper.wrapAngleTo180_float(startRot.getYaw());
        if (yawChng <= -180.0F) {
            yawChng += 360.0F;
        } else if (yawChng > 180.0F) {
            yawChng += -360.0F;
        }

        /*if () {
            if (yawChng < 0.0F) {
                yawChng += 360.0F;
            } else {
                yawChng -= 360.0F;
            }
        }*/

        return new Rotation(yawChng, endRot.getPitch() - startRot.getPitch());
    }

    public static void reset() {
        done = true;
        startRot = null;
        neededChange = null;
        endRot = null;
        startTime = 0L;
        endTime = 0L;
    }

    public static void update() {
        Minecraft mc = Minecraft.getMinecraft();
        if (System.currentTimeMillis() <= endTime) {
            mc.thePlayer.rotationYaw = interpolate(startRot.getYaw(), endRot.getYaw());
            mc.thePlayer.rotationPitch = interpolate(startRot.getPitch(), endRot.getPitch());
        } else if (!done) {
            mc.thePlayer.rotationYaw = endRot.getYaw();
            mc.thePlayer.rotationPitch = endRot.getPitch();
            reset();
        }

    }

    private static float interpolate(float start, float end) {
        float spentMillis = (float) (System.currentTimeMillis() - startTime);
        float relativeProgress = spentMillis / (float) (endTime - startTime);
        return (end - start) * easeOutCubic(relativeProgress) + start;
    }

    public static float easeOutCubic(double number) {
        return (float) Math.max(0.0D, Math.min(1.0D, 1.0D - Math.pow(1.0D - number, 3.0D)));
    }

    public static double getHorizontalDistance(Vec3 vec1, Vec3 vec2) {
        double d0 = vec1.xCoord - vec2.xCoord;
        double d2 = vec1.zCoord - vec2.zCoord;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2);
    }

    public static Rotation getNeededChange(Rotation endRot) {
        Minecraft mc = Minecraft.getMinecraft();
        Rotation startRot = new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        return getNeededChange(startRot, endRot);
    }

    public static void stopMovement() {
        Minecraft mc = Minecraft.getMinecraft();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
    }

    private static final Map<Integer, KeyBinding> keyBindMap = new HashMap<Integer, KeyBinding>();

    public static ArrayList<KeyBinding> getNeededKeyPresses(Vec3 from, Vec3 to) {
        ArrayList<KeyBinding> neededKeyPresses = new ArrayList();
        Rotation neededRot = Utils.getNeededChange(Utils.getRotation(from, to));
        double neededYaw = neededRot.getYaw() * -1.0F;
        keyBindMap.forEach((k, v) -> {
            if (Math.abs((double) k - neededYaw) < 67.5D || Math.abs((double) k - (neededYaw + 360.0D)) < 67.5D) {
                neededKeyPresses.add(v);
            }

        });
        return neededKeyPresses;
    }
    public static Vec3 getLook(final Vec3 vec) {
        final double diffX = vec.xCoord - mc.thePlayer.posX;
        final double diffY = vec.yCoord - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord - mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        return getVectorForRotation((float)(-(MathHelper.atan2(diffY, dist) * 180.0 / 3.141592653589793)), (float)(MathHelper.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0));
    }
    public static Vec3 scaleVec(Vec3 vec, float scale) {
        return new Vec3(vec.xCoord * scale, vec.yCoord * scale, vec.zCoord * scale);
    }

    public static void drawLines(List<Vec3> poses, float thickness, float partialTicks) {
        Entity render = Minecraft.getMinecraft().getRenderViewEntity();
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * (double) partialTicks;
        double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * (double) partialTicks;
        double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * (double) partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translate(-realX, -realY, -realZ);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDisable(3553);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GL11.glLineWidth(thickness);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);

        for (Vec3 pos : poses) {
            int i = Chroma.color.getRGB();
            worldRenderer.pos(pos.xCoord + 0.5D, pos.yCoord + 0.5D, pos.zCoord + 0.5D).color((float) (i >> 16 & 255) / 255.0F, (float) (i >> 8 & 255) / 255.0F, (float) (i & 255) / 255.0F, (float) (i >> 24 & 255) / 255.0F).endVertex();
        }

        Tessellator.getInstance().draw();
        GlStateManager.translate(realX, realY, realZ);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    //Jrojro728改变开始

    /**
     * 从json文件读取HashMap<String, String>的数据并返回
     * @param FilePath Json文件路径，可以是全局文件，也可以是存储在resources的。
     * @return 读取到的HashMap对象
     */
    public static HashMap<String, String> getHashMapInJsonFile(String FilePath) {
        /*InputStream path = Utils.class.getResourceAsStream(FilePath);
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
        return gson.fromJson(content.toString(), type);*/
        //有人跟我报告这部分代码导致无法启动，先注释了
        return null;
    }

    /**
     * 解码json字符串到某个对象
     * @param jsonString json源字符串
     * @param tClass 要解码对象的class
     * @param <T> 对象泛型
     * @return 解码后的对象
     */
    public static <T> T decodeJsonToBean(String jsonString, Class<T> tClass) { return gson.fromJson(jsonString, tClass); }
    public static void renderTrace(BlockPos from,BlockPos to,Color c,float width){
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        GL11.glLineWidth(width);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_LINE_STRIP,DefaultVertexFormats.POSITION);
        worldrenderer.pos(from.getX() - renderManager.viewerPosX,from.getY() - renderManager.viewerPosY,from.getZ() - renderManager.viewerPosZ).endVertex();
        worldrenderer.pos(to.getX() - renderManager.viewerPosX,to.getY() - renderManager.viewerPosY,to.getZ() - renderManager.viewerPosZ).endVertex();
        tessellator.draw();
        GL11.glLineWidth(1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();

    }
    public static void renderTrace(Vec3 from,Vec3 to,Color c,float width){
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        GL11.glLineWidth(width);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_LINE_STRIP,DefaultVertexFormats.POSITION);
        worldrenderer.pos(from.xCoord - renderManager.viewerPosX,from.yCoord - renderManager.viewerPosY,from.zCoord - renderManager.viewerPosZ).endVertex();
        worldrenderer.pos(to.xCoord - renderManager.viewerPosX,to.yCoord - renderManager.viewerPosY,to.zCoord - renderManager.viewerPosZ).endVertex();
        tessellator.draw();
        GL11.glLineWidth(1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();

    }
    public static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    public static final int alphabetLength;

    public static String nextString(int lenght, Random random) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < lenght; ++i) {
            sb.append(alphabet[random.nextInt(alphabet.length)]);
        }

        return sb.toString();
    }
    public static boolean canVecBeSeenFromVec(Vec3 from, Vec3 to, float step) {
        double dist = from.distanceTo(to);

        for (double d = step; d < dist; d += step) {

            Vec3 pos1 = new Vec3(
                    from.xCoord + (to.xCoord - from.xCoord) *  d / dist,
                    from.yCoord + (to.yCoord - from.yCoord) * d / dist,
                    from.zCoord + (to.zCoord - from.zCoord) * d / dist
            );

            if ((int) pos1.xCoord == (int) from.xCoord &&
                    (int) pos1.yCoord == (int) from.yCoord &&
                    (int) pos1.zCoord == (int) from.zCoord) {
                continue;
            }

            if ((int) pos1.xCoord == (int) to.xCoord &&
                    (int) pos1.yCoord == (int) to.yCoord &&
                    (int) pos1.zCoord == (int) to.zCoord) {
                continue;
            }

            if (mc.theWorld.getBlockState(new BlockPos(pos1)).getBlock() != Blocks.air) {
                return false;
            }
        }

        return true;
    }
    static Minecraft mc = Minecraft.getMinecraft();
    public static ArrayList<BlockPos> getAllTeleportableBlocksNew(Vec3 vec, float range) {
        long timestamp = System.currentTimeMillis();
        BlockPos origin = new BlockPos(vec);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(origin.subtract(new Vec3i(range, 16, range)), origin.add(new Vec3i(range, 16, range)));
        ArrayList<BlockPos> validBlocks = (ArrayList<BlockPos>) StreamSupport.stream(
                blocks.spliterator(), false
        ).filter(blockPos ->
                mc.theWorld.getBlockState(blockPos).getBlock().isCollidable() && mc.theWorld.getBlockState(blockPos).getBlock() != Blocks.carpet &&
                        mc.theWorld.getBlockState(blockPos).getBlock() != Blocks.skull && mc.theWorld.getBlockState(blockPos).getBlock() != Blocks.wall_sign &&
                        mc.theWorld.getBlockState(blockPos).getBlock() != Blocks.standing_sign &&
                        !(mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockFence) &&
                        !(mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockFenceGate) &&
                        mc.theWorld.getBlockState(blockPos).getBlock().getCollisionBoundingBox(mc.theWorld, blockPos, mc.theWorld.getBlockState(blockPos)) != null &&
                        mc.theWorld.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.air &&
                        mc.theWorld.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.air &&
                        vec.distanceTo(new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.95, blockPos.getZ() + 0.5)) <= 61 &&
                        canBlockBeeSeenFromVecNew(vec, blockPos)
        ).collect(Collectors.toList());
        return validBlocks;
    }

    public static boolean canBlockBeeSeenFromVecNew(Vec3 from, BlockPos blockPos) {
        Vec3 to = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.95, blockPos.getZ() + 0.5);
        MovingObjectPosition movingObjectPosition = mc.theWorld.rayTraceBlocks(from, to);
        return movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && movingObjectPosition.getBlockPos().equals(blockPos);
    }

    public static String nextString(int lenght) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < lenght; ++i) {
            sb.append(alphabet[random.nextInt(alphabet.length)]);
        }

        return sb.toString();
    }

    static {
        alphabetLength = alphabet.length;
    }
    public static EnumFacing calculateEnumfacingLook(Vec3 look) {
        MovingObjectPosition position = calculateInterceptAABBLook(getBlockAABB(new BlockPos(look)), look);
        return position != null ? position.sideHit : Minecraft.getMinecraft().thePlayer.getHorizontalFacing().getOpposite();
    }
    public static MovingObjectPosition calculateInterceptAABBLook(AxisAlignedBB aabb, Vec3 look) {
        return aabb.calculateIntercept(getPositionEyes(), look);
    }
    public static AxisAlignedBB getBlockAABB(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
        return block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos);
    }
    public static Vec3 getPositionEyes() {
        return new Vec3(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + (double)(Minecraft.getMinecraft().thePlayer.isSneaking() ? 1.54F : 1.62F), Minecraft.getMinecraft().thePlayer.posZ);
    }
    static String MarketingAccountTemplate = "&name&&event&是怎么回事呢？&name&相信大家都很熟悉，但是&name&&event&是怎么回事呢，下面就让小编带大家一起了解吧。" +
            "" +
            "&name&&event&，其实就是&explain&，大家可能会很惊讶&name&怎么会&event&呢？但事实就是这样，小编也感到非常惊讶。" +
            "" +
            "这就是关于&name&&event&的事情了，大家有什么想法呢，欢迎在评论区告诉小编一起讨论哦！" +
            "";
    public static String MarketingAccountGenerator(String name,String event,String explain){
        return MarketingAccountTemplate.replace("&name&",name).replace("&event&",event).replace("&explain&",explain);
    }
    private boolean checkArea(String areaName) {
        boolean isInArea = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if(Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), areaName)){
                isInArea = true;
            }
        }
        return isInArea;
    }
    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        Minecraft mc = Minecraft.getMinecraft();
        if (framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth
                || framebuffer.framebufferHeight != mc.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        }
        return framebuffer;
    }
    public static void bindTexture(int texture) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }
    public static Float getSpeed(){
        Minecraft mc = Minecraft.getMinecraft();
        return ((Double)sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ)).floatValue();
    }
    public static Double getDirection(){
        Minecraft mc = Minecraft.getMinecraft();
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0f) rotationYaw += 180f;
        float forward = 1f;
        if (mc.thePlayer.moveForward < 0f) {forward = -0.5f;} else if (mc.thePlayer.moveForward > 0f) {forward = 0.5f;}
        if (mc.thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (mc.thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward;
        return Math.toRadians(((Float)rotationYaw).doubleValue());
    }
    public static void strafe(){
        if (isMoving()) {
            Minecraft.getMinecraft().thePlayer.motionX = -sin(getDirection()) * getSpeed();
            Minecraft.getMinecraft().thePlayer.motionZ = cos(getDirection()) * getSpeed();
        }
    }
    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos((float) (Math.toRadians(-yaw) - (float) Math.PI));
        float f1 = MathHelper.sin((float) (Math.toRadians(-yaw) - (float) Math.PI));
        float f2 = -MathHelper.cos((float) Math.toRadians(-pitch));
        float f3 = MathHelper.sin((float) Math.toRadians(-pitch));
        return new Vec3(f1 * f2, f3, f * f2);
    }

}
