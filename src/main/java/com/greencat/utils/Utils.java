package com.greencat.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.ConfigLoader;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.common.mixins.EntityPlayerSPAccessor;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.sqrt;

public class Utils {
    public static float lastReportedPitch;
    public static int lastReportedSlot;
    public static ArrayList<Packet<?>> noEvent = new ArrayList();
    public void print(String message) {
        if(Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[" + EnumChatFormatting.WHITE + "Antimony" + EnumChatFormatting.LIGHT_PURPLE + "] " + message + "."));
        }
    }
    public void printAntimonyChannel(String message) {
        if(FunctionManager.getStatus("AntimonyChannel")) {
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
        if(Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "[" + EnumChatFormatting.GOLD + "Antimony" + EnumChatFormatting.DARK_RED + "Dev" + EnumChatFormatting.DARK_GREEN + "]" + EnumChatFormatting.YELLOW + message + "."));
        }
    }

    public Boolean ModLoadCheck(String ModID) {
        Boolean isLoaded = Loader.isModLoaded(ModID);
        return isLoaded;
    }
    public double FlatAngle(double x1,double z1,double x2,double z2){
        double AX = Math.abs(x1 - x2);
        double AZ = Math.abs(z1 - z2);
        double angle = Math.round(Math.asin(AZ / sqrt(AX * AX + AZ * AZ)) / Math.PI * 180);
        if(x1 > x2 && z1 > z2){
            return angle + 90;
        } else if (x1 < x2 && z1 < z2){
            return angle - 90;
        } else if(x1 > x2 && z1 < z2){
            return 90 - angle;
        } else if(x1 < x2 && z1 > z2){
            return 0 - (angle - 270);
        }
        return 0;
    }
    public double ErectAngleX(double x1,double y1,double x2,double y2){
        double AX = Math.abs(x1 - x2);
        double AY = Math.abs(y1 - y2);
        double angle = Math.round(Math.asin(AY / sqrt(AX * AX + AY * AY)) / Math.PI * 180);
        if(y1 < y2){
          return 0 - angle;
        }
        if(y1 > y2){
            return angle;
        }
        return 0;
    }
    public double ErectAngleZ(double z1,double y1,double z2,double y2){
        double AZ = Math.abs(z1 - z2);
        double AY = Math.abs(y1 - y2);
        double angle = Math.round(Math.asin(AY / sqrt(AZ * AZ + AY * AY)) / Math.PI * 180);
        if(y1 < y2){
            return 0 - angle;
        }
        if(y1 > y2){
            return angle;
        }
        return 0;
    }
    public double ErectAngle(double[] Location1,double[] Location2){
        if(Minecraft.getMinecraft().thePlayer.rotationYaw <= 135 && Minecraft.getMinecraft().thePlayer.rotationYaw > 45){
            return ErectAngleX(Location1[0],Location1[1],Location2[0],Location2[1]);
        }else if(Minecraft.getMinecraft().thePlayer.rotationYaw <= -45 && Minecraft.getMinecraft().thePlayer.rotationYaw > -90){
            return ErectAngleX(Location1[0],Location1[1],Location2[0],Location2[1]);
        }else if (Minecraft.getMinecraft().thePlayer.rotationYaw <= -90 && Minecraft.getMinecraft().thePlayer.rotationYaw > -135){
            return ErectAngleZ(Location1[0], Location1[1], Location2[0], Location2[1]);
        }else {
            return ErectAngleZ(Location1[2], Location1[1], Location2[2], Location2[1]);
        }
    }
    public boolean RangeInDefined(double number,double range1,double range2){
        if(number > range1 && number < range2){
            return true;
        }
        return false;
    }
    public static void BoxWithESP(BlockPos pos, Color c,boolean Blend) {
            RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
            Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
            block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            if(Blend){
                GlStateManager.enableBlend();
            }
            GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
            AxisAlignedBB box = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
            BoxWithoutESPRender(box);
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
    }
    public static void BoxWithoutESP(BlockPos pos, Color c,boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
        GlStateManager.disableLighting();
        if(Blend){
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        BoxWithoutESPRender(box);
        GlStateManager.disableBlend();
    }
    public static void BoxWithESP(AxisAlignedBB aabb, Color c,boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if(Blend){
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = aabb.offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        BoxWithoutESPRender(box);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }
    public static void OutlinedBoxWithESP(BlockPos pos, Color c,boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if(Blend){
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }
    public static void OutlinedBoxWithESP(BlockPos pos, Color c,boolean Blend,float width) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if(Blend){
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
    public static void OutlinedBoxWithESP(AxisAlignedBB aabb, Color c,boolean Blend) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if(Blend){
            GlStateManager.enableBlend();
        }
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = aabb.offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }
    public static void OutlinedBoxWithESP(AxisAlignedBB aabb, Color c,boolean Blend,float width) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if(Blend){
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


    public static void BoxWithoutESPRender(AxisAlignedBB box){
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
    public void getNetworth(final String name) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String content = "";
                    URL url = new URL("https://spillager.live/skyblock/networth/" + name);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String input;
                    while ((input = reader.readLine()) != null) {
                        content += input;
                    }
                    reader.close();
                    String ign = "玩家名称:" + content.split("<div class=\"col\">")[1].split("<div>")[0];
                    String ProfileName = "存档名称:" + content.split("<div class=\"col\">")[1].split("<div>")[1].split("</b> ")[1].split("</div>")[0];
                    String Networth = "玩家净值:" + content.split("<div style=\"margin-left: 0;\" class=\"overview_element_value mt-2\">")[1].split("<b class=\"ml-1\">")[0].split("</b> ")[1] + "coins " + content.split("<div style=\"margin-left: 0;\" class=\"overview_element_value mt-2\">")[1].split("</b>")[1].split("<b class=\"ml-1\">")[1];
                    print(ign);
                    print(ProfileName);
                    print(Networth);
                } catch (IOException malformedURLException) {
                    print(malformedURLException.toString());
                    print("获取失败,请重试");
                } catch (ArrayIndexOutOfBoundsException exception) {
                    print(exception.toString());
                    print("玩家名错误");
                }
            }
        };


        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void getNetworth(final String name, final String Profile) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String content = "";
                    URL url = new URL("https://spillager.live/skyblock/networth/" + name + "/" + Profile);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String input;
                    while ((input = reader.readLine()) != null) {
                        content += input;
                    }
                    reader.close();
                    String ign = "玩家名称:" + content.split("<div class=\"col\">")[1].split("<div>")[0];
                    String ProfileName = "存档名称:" + content.split("<div class=\"col\">")[1].split("<div>")[1].split("</b> ")[1].split("</div>")[0];
                    String Networth = "玩家净值:" + content.split("<div style=\"margin-left: 0;\" class=\"overview_element_value mt-2\">")[1].split("<b class=\"ml-1\">")[0].split("</b> ")[1] + "coins " + content.split("<div style=\"margin-left: 0;\" class=\"overview_element_value mt-2\">")[1].split("</b>")[1].split("<b class=\"ml-1\">")[1];
                    print(ign);
                    print(ProfileName);
                    print(Networth);
                } catch (IOException malformedURLException) {
                    print(malformedURLException.toString());
                    print("获取失败,请重试");
                } catch (ArrayIndexOutOfBoundsException exception) {
                    print(exception.toString());
                    print("玩家名错误");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public static int FindPickaxeInHotBar() {
        for (int i = 0; i < 8; ++i) {
            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
            if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains("gauntlet") || (stack.getItem() == Items.wooden_pickaxe || stack.getItem() == Items.stone_pickaxe || stack.getItem() == Items.golden_pickaxe || stack.getItem() == Items.iron_pickaxe || stack.getItem() == Items.diamond_pickaxe || stack.getItem() == Items.prismarine_shard)) {
                return i;
            }
        }
        return 0;
    }
    public static String Dec2Hex(int dec){
        StringBuilder sb = new StringBuilder();
        String Hex;
        char[] HexList = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(dec != 0){
            sb = sb.append(HexList[dec % 16]);
            dec = dec / 16;
        }
        Hex = sb.reverse().toString();
        return Hex;
    }
    public static int Hex2Dec(String Hex){
        Hex = Hex.replace(" ","");
        int length = Hex.length();
        byte[] bytes = new byte[length / 2];
        for(int i = 0;i < length;i += 2){
            bytes[i / 2] = (byte)((Character.digit(Hex.charAt(i),16) << 4) + Character.digit(Hex.charAt(i + 1), 16));
        }
        return bytesToInt(bytes);
    }
    public static int bytesToInt(byte[] intByte) {
        int fromByte = 0;
        for (int i = 0; i < 2; i++) {
            int n = (intByte[i] < 0 ? (int)intByte[i] + 256 : (int)intByte[i]) << (8 * i);
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
    public static int ItemInHandIndex() {
        InventoryPlayer PlayerInventory = Minecraft.getMinecraft().thePlayer.inventory;
        return PlayerInventory.currentItem;
    }
    public static void setHeldItemIndex(int index) {
        if (!(index < 0 || index > 8)) {
            InventoryPlayer PlayerInv = Minecraft.getMinecraft().thePlayer.inventory;
            PlayerInv.currentItem = index;
        }
    }
    public static ItemStack getHeldItem() {;
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

    public static void renderText(String str,BlockPos pos,float partialTicks) {
        //From NotEnoughUpdates SourceCode(https://github.com/Moulberry/NotEnoughUpdates/blob/master/src/main/java/io/github/moulberry/notenoughupdates/core/util/render/RenderUtils.java)
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
        //From NotEnoughUpdates SourceCode(https://github.com/Moulberry/NotEnoughUpdates/blob/master/src/main/java/io/github/moulberry/notenoughupdates/core/util/render/RenderUtils.java)
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
        List<Score> list = Lists.newArrayList(Iterables.filter(scores,new Predicate<Score>()  {
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
    public static boolean containedByCharSequence(String s1,String s2) {

        char[] c = s2.toCharArray();
        char[] s = s1.toCharArray();
        int cIdx = 0;
        for (int i = 0;i < s.length && cIdx < c.length;i++) {
            if (s[i] == c[cIdx]) cIdx++;
        }

        return cIdx == c.length;

    }
    public static void drawStringScaled(String str, FontRenderer fr, float x, float y, int colour, float scale) {
        GlStateManager.scale(scale, scale, 1);
        fr.drawString(str, (int)(x / scale), (int)(y / scale), colour);
        GlStateManager.scale(1 / scale, 1 / scale, 1);
    }
    public static float[] getServerAngles(Entity en) {
        return getServerAngles(new Vec3(en.posX, en.posY + ((double) en.getEyeHeight() - (double) en.height / 1.5D) + 0.5D, en.posZ));
    }
    public static float[] getServerAngles(Vec3 vec) {
        double diffX = vec.xCoord - Minecraft.getMinecraft().thePlayer.posX;
        double diffY = vec.yCoord - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double diffZ = vec.zCoord - Minecraft.getMinecraft().thePlayer.posZ;
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
        return new float[]{((EntityPlayerSPAccessor)Minecraft.getMinecraft().thePlayer).getLastReportedYaw() + MathHelper.wrapAngleTo180_float(yaw - ((EntityPlayerSPAccessor)Minecraft.getMinecraft().thePlayer).getLastReportedYaw()), ((EntityPlayerSPAccessor)Minecraft.getMinecraft().thePlayer).getLastReportedPitch() + MathHelper.wrapAngleTo180_float(pitch - ((EntityPlayerSPAccessor)Minecraft.getMinecraft().thePlayer).getLastReportedPitch())};
    }
    public static boolean isWithinFOV(EntityLivingBase entity, double fov) {
        float yawDifference = Math.abs(getAngles((Entity)entity)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw);
        return (double)yawDifference < fov && (double)yawDifference > -fov;
    }
    public static boolean isWithinPitch(EntityLivingBase entity, double pitch) {
        float pitchDifference = Math.abs(getAngles((Entity)entity)[1] - Minecraft.getMinecraft().thePlayer.rotationPitch);
        return (double)pitchDifference < pitch && (double)pitchDifference > -pitch;
    }
    public static float[] getAngles(Entity en) {
        return getAngles(new Vec3(en.posX, en.posY + ((double)en.getEyeHeight() - (double)en.height / 1.5D) + 0.5D, en.posZ));
    }
    public static float[] getAngles(Vec3 vec) {
        double diffX = vec.xCoord - Minecraft.getMinecraft().thePlayer.posX;
        double diffY = vec.yCoord - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double diffZ = vec.zCoord - Minecraft.getMinecraft().thePlayer.posZ;
        double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
        return new float[]{Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)};
    }
    public static boolean isOnSkyBlock() {
        try {
            //ScoreObjective titleObjective = Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
            return Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(0) != null ? ChatFormatting.stripFormatting(Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(0).getDisplayName()).contains("SKYBLOCK") : ChatFormatting.stripFormatting(Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()).contains("SKYBLOCK");
        } catch (Exception var1) {
            return false;
        }
    }
    @SubscribeEvent
    public void onPacketSent(CustomEventHandler.PacketSentEvent event) {
        if (event.packet instanceof C09PacketHeldItemChange) {
            lastReportedSlot = ((C09PacketHeldItemChange)event.packet).getSlotId();
        }
    }

    public static void updateItem() {
        if (lastReportedSlot != Minecraft.getMinecraft().thePlayer.inventory.currentItem) {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(Minecraft.getMinecraft().thePlayer.inventory.currentItem));
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
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
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
    public static void RenderTargetHUD(EntityLivingBase entity, Color c,float width,float size,Double[] currentHeight,Boolean[] currentStatus) {
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
        wr.pos(x + (size / 2),y,z + size).endVertex();
        wr.pos(x + size,y,z + (size / 2)).endVertex();
        wr.pos(x + size,y,z - (size / 2)).endVertex();
        wr.pos(x + (size / 2),y,z - size).endVertex();
        wr.pos(x - (size / 2),y,z - size).endVertex();
        wr.pos(x - size,y,z - (size / 2)).endVertex();
        wr.pos(x - size,y,z + (size / 2)).endVertex();
        wr.pos(x - (size / 2),y,z + size).endVertex();
        wr.pos(x + (size / 2),y,z + size).endVertex();
        tessellator.draw();
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x + (size / 2),y + entityHeight,z + size).endVertex();
        wr.pos(x + size,y + entityHeight,z + (size / 2)).endVertex();
        wr.pos(x + size,y + entityHeight,z - (size / 2)).endVertex();
        wr.pos(x + (size / 2),y + entityHeight,z - size).endVertex();
        wr.pos(x - (size / 2),y + entityHeight,z - size).endVertex();
        wr.pos(x - size,y + entityHeight,z - (size / 2)).endVertex();
        wr.pos(x - size,y + entityHeight,z + (size / 2)).endVertex();
        wr.pos(x - (size / 2),y + entityHeight,z + size).endVertex();
        wr.pos(x + (size / 2),y + entityHeight,z + size).endVertex();
        tessellator.draw();
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x + (size / 2),y,z + size).endVertex();
        wr.pos(x + (size / 2),y + entityHeight,z + size).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x + size,y,z + (size / 2)).endVertex();
        wr.pos(x + size,y + entityHeight,z + (size / 2)).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x + size,y,z - (size / 2)).endVertex();
        wr.pos(x + size,y + entityHeight,z - (size / 2)).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x + (size / 2),y,z - size).endVertex();
        wr.pos(x + (size / 2),y + entityHeight,z - size).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x - (size / 2),y,z - size).endVertex();
        wr.pos(x - (size / 2),y + entityHeight,z - size).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x - size,y,z - (size / 2)).endVertex();
        wr.pos(x - size,y + entityHeight,z - (size / 2)).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x - size,y,z + (size / 2)).endVertex();
        wr.pos(x - size,y + entityHeight,z + (size / 2)).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x - (size / 2),y,z + size).endVertex();
        wr.pos(x - (size / 2),y + entityHeight,z + size).endVertex();
        tessellator.draw();
        if(currentStatus[0] && currentHeight[0] + 0.02 > entityHeight){
            currentStatus[0] = false;
        }
        if(!currentStatus[0] && currentHeight[0] - 0.02 < 0){
            currentStatus[0] = true;
        }
        if(currentStatus[0]){
            currentHeight[0] = currentHeight[0] + 0.02;
        } else {
            currentHeight[0] = currentHeight[0] - 0.02;
        }
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x + (size / 2),y + currentHeight[0],z + size).endVertex();
        wr.pos(x + size,y + currentHeight[0],z + (size / 2)).endVertex();
        wr.pos(x + size,y + currentHeight[0],z - (size / 2)).endVertex();
        wr.pos(x + (size / 2),y + currentHeight[0],z - size).endVertex();
        wr.pos(x - (size / 2),y + currentHeight[0],z - size).endVertex();
        wr.pos(x - size,y + currentHeight[0],z - (size / 2)).endVertex();
        wr.pos(x - size,y + currentHeight[0],z + (size / 2)).endVertex();
        wr.pos(x - (size / 2),y + currentHeight[0],z + size).endVertex();
        wr.pos(x + (size / 2),y + currentHeight[0],z + size).endVertex();
        tessellator.draw();



        GL11.glLineWidth(1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }
    

}
