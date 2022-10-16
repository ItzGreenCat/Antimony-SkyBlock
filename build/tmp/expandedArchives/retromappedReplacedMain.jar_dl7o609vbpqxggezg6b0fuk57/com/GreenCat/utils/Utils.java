package com.GreenCat.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    public void print(String message) {
        if(Minecraft.func_71410_x().field_71441_e != null) {
            Minecraft.func_71410_x().field_71439_g.func_145747_a(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[" + EnumChatFormatting.WHITE + "Antimony" + EnumChatFormatting.LIGHT_PURPLE + "] " + message + "."));
        }
    }
    public void devLog(String message) {
        if(Minecraft.func_71410_x().field_71441_e != null) {
            Minecraft.func_71410_x().field_71439_g.func_145747_a(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "[" + EnumChatFormatting.GOLD + "Antimony" + EnumChatFormatting.DARK_RED + "Dev" + EnumChatFormatting.DARK_GREEN + "]" + EnumChatFormatting.YELLOW + message + "."));
        }
    }

    public Boolean ModLoadCheck(String ModID) {
        Boolean isLoaded = Loader.isModLoaded(ModID);
        return isLoaded;
    }
    public double FlatAngle(double x1,double z1,double x2,double z2){
        double AX = Math.abs(x1 - x2);
        double AZ = Math.abs(z1 - z2);
        double angle = Math.round(Math.asin(AZ / Math.sqrt(AX * AX + AZ * AZ)) / Math.PI * 180);
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
        double angle = Math.round(Math.asin(AY / Math.sqrt(AX * AX + AY * AY)) / Math.PI * 180);
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
        double angle = Math.round(Math.asin(AY / Math.sqrt(AZ * AZ + AY * AY)) / Math.PI * 180);
        if(y1 < y2){
            return 0 - angle;
        }
        if(y1 > y2){
            return angle;
        }
        return 0;
    }
    public double ErectAngle(double[] Location1,double[] Location2){
        if(Minecraft.func_71410_x().field_71439_g.field_70177_z <= 135 && Minecraft.func_71410_x().field_71439_g.field_70177_z > 45){
            return ErectAngleX(Location1[0],Location1[1],Location2[0],Location2[1]);
        }else if(Minecraft.func_71410_x().field_71439_g.field_70177_z <= -45 && Minecraft.func_71410_x().field_71439_g.field_70177_z > -90){
            return ErectAngleX(Location1[0],Location1[1],Location2[0],Location2[1]);
        }else if (Minecraft.func_71410_x().field_71439_g.field_70177_z <= -90 && Minecraft.func_71410_x().field_71439_g.field_70177_z > -135){
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
            RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
            Block block = Minecraft.func_71410_x().field_71441_e.func_180495_p(pos).func_177230_c();
            block.func_180654_a(Minecraft.func_71410_x().field_71441_e, pos);
            GlStateManager.func_179097_i();
            GlStateManager.func_179090_x();
            GlStateManager.func_179140_f();
            if(Blend){
                GlStateManager.func_179147_l();
            }
            GlStateManager.func_179131_c((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
            AxisAlignedBB box = block.func_180646_a(Minecraft.func_71410_x().field_71441_e, pos).func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n).func_72314_b(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
            BoxWithoutESPRender(box);
            GlStateManager.func_179084_k();
            GlStateManager.func_179098_w();
            GlStateManager.func_179126_j();
    }
    public static void BoxWithoutESP(BlockPos pos, Color c,boolean Blend) {
        RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        Block block = Minecraft.func_71410_x().field_71441_e.func_180495_p(pos).func_177230_c();
        block.func_180654_a(Minecraft.func_71410_x().field_71441_e, pos);
        GlStateManager.func_179140_f();
        if(Blend){
            GlStateManager.func_179147_l();
        }
        GlStateManager.func_179131_c((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.func_180646_a(Minecraft.func_71410_x().field_71441_e, pos).func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n).func_72314_b(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        BoxWithoutESPRender(box);
        GlStateManager.func_179084_k();
    }
    public static void BoxWithESP(AxisAlignedBB aabb, Color c,boolean Blend) {
        RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        if(Blend){
            GlStateManager.func_179147_l();
        }
        GlStateManager.func_179131_c((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = aabb.func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n).func_72314_b(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        BoxWithoutESPRender(box);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
    }
    public static void OutlinedBoxWithESP(BlockPos pos, Color c,boolean Blend) {
        RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        Block block = Minecraft.func_71410_x().field_71441_e.func_180495_p(pos).func_177230_c();
        block.func_180654_a(Minecraft.func_71410_x().field_71441_e, pos);
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        if(Blend){
            GlStateManager.func_179147_l();
        }
        GlStateManager.func_179131_c((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.func_180646_a(Minecraft.func_71410_x().field_71441_e, pos).func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n).func_72314_b(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
    }
    public static void OutlinedBoxWithESP(BlockPos pos, Color c,boolean Blend,float width) {
        RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        Block block = Minecraft.func_71410_x().field_71441_e.func_180495_p(pos).func_177230_c();
        block.func_180654_a(Minecraft.func_71410_x().field_71441_e, pos);
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        if(Blend){
            GlStateManager.func_179147_l();
        }
        GL11.glLineWidth(width);
        GlStateManager.func_179131_c((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = block.func_180646_a(Minecraft.func_71410_x().field_71441_e, pos).func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n).func_72314_b(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GL11.glLineWidth(1.0F);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
    }
    public static void OutlinedBoxWithESP(AxisAlignedBB aabb, Color c,boolean Blend) {
        RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        if(Blend){
            GlStateManager.func_179147_l();
        }
        GlStateManager.func_179131_c((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = aabb.func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n).func_72314_b(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
    }
    public static void OutlinedBoxWithESP(AxisAlignedBB aabb, Color c,boolean Blend,float width) {
        RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        if(Blend){
            GlStateManager.func_179147_l();
        }
        GL11.glLineWidth(width);
        GlStateManager.func_179131_c((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
        AxisAlignedBB box = aabb.func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n).func_72314_b(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
        OutlinedBoxWithoutESPRender(box);
        GL11.glLineWidth(1.0F);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
    }


    public static void BoxWithoutESPRender(AxisAlignedBB box){
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72334_f).func_181675_d();
        tessellator.func_78381_a();


    }
    public static void OutlinedBoxWithoutESPRender(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(box.field_72340_a, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(box.field_72340_a, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(box.field_72336_d, box.field_72337_e, box.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(box.field_72336_d, box.field_72338_b, box.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
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
        int itemindex = 0;
        for(int i = 0; i < 8; ++i) {
            ItemStack stack = Minecraft.func_71410_x().field_71439_g.field_71071_by.field_70462_a[i];
            if(stack != null) {
                if(stack.func_77973_b() == Items.field_151039_o || stack.func_77973_b() == Items.field_151050_s || stack.func_77973_b() == Items.field_151005_D || stack.func_77973_b() == Items.field_151035_b || stack.func_77973_b() == Items.field_151046_w || stack.func_77973_b() == Items.field_179562_cC) {
                    itemindex = i;
                }
                if(stack.func_77973_b() == Items.field_151144_bL) {
                    if(stack.func_82833_r().contains("Gauntlet")) {
                        itemindex = i;
                    }
                }
            }
        }

        return itemindex;
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


}
