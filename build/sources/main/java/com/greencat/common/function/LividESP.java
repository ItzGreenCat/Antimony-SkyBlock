package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class LividESP {
    public LividESP(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderEvent(RenderLivingEvent.Specials.Pre<EntityLivingBase> event){
        if (FunctionManager.getStatus("LividESP")) {
            if (isInF5()) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    Entity entity = event.entity;
                    if (entity instanceof EntityArmorStand) {
                        if (entity.hasCustomName()) {
                            if (entity.getCustomNameTag().contains("Livid")) {
                                if (entity.getCustomNameTag().split("Livid")[0].contains(getColor((EnumDyeColor) Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(5, 109, 43)).getValue((IProperty) BlockStainedGlass.COLOR)).toString())) {
                                    Utils.OutlinedBoxWithESP(new AxisAlignedBB(event.entity.posX - 0.5D, event.entity.posY - 2.0D, event.entity.posZ - 0.5D, event.entity.posX + 0.5D, event.entity.posY, event.entity.posZ + 0.5D), new Color(255, 0, 182), false,3F);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void DrawBlockESP(RenderWorldLastEvent event){
        if (FunctionManager.getStatus("LividESP")) {
            if (isInF5()) {
                Utils.BoxWithESP(new BlockPos(5, 109, 43),new Color(255, 0, 182),false);
            }
        }
    }
    private boolean isInF5() {
        boolean isInTheCatacombs = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        final String combatZoneName = "the catacombs";
        final String clearedName = "dungeon cleared";
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if(Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), "5")){
                if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), clearedName)) {
                    isInTheCatacombs = true;
                }
                if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), combatZoneName) && !scoreBoardLines.get(size - i).toLowerCase().contains("to")) {
                    isInTheCatacombs = true;
                }
            }
        }
        return isInTheCatacombs;
    }
    private EnumChatFormatting getColor(EnumDyeColor color){
        if (color == EnumDyeColor.WHITE) {
            return EnumChatFormatting.WHITE;
        }
        if (color == EnumDyeColor.MAGENTA) {
            return EnumChatFormatting.LIGHT_PURPLE;
        }
        if (color == EnumDyeColor.PINK) {
            return EnumChatFormatting.LIGHT_PURPLE;
        }
        if (color == EnumDyeColor.RED) {
            return EnumChatFormatting.RED;
        }
        if (color == EnumDyeColor.SILVER ) {
            return EnumChatFormatting.GRAY;
        }
        if (color == EnumDyeColor.GRAY) {
            return EnumChatFormatting.GRAY;
        }
        if (color == EnumDyeColor.GREEN) {
            return EnumChatFormatting.DARK_GREEN;
        }
        if (color == EnumDyeColor.LIME) {
            return EnumChatFormatting.GREEN;
        }
        if (color == EnumDyeColor.BLUE) {
            return EnumChatFormatting.BLUE;
        }
        if (color == EnumDyeColor.PURPLE) {
            return EnumChatFormatting.DARK_PURPLE;
        }
        if (color == EnumDyeColor.YELLOW) {
            return EnumChatFormatting.YELLOW;
        }
        return EnumChatFormatting.WHITE;
    }
}
