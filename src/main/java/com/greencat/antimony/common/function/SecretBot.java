package com.greencat.antimony.common.function;

import com.google.common.collect.Iterables;
import com.greencat.Antimony;
import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.common.key.KeyLoader;
import com.greencat.antimony.utils.Utils;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class SecretBot extends FunctionStatusTrigger {
    public SecretBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    List<BlockPos> clickedBlock = new ArrayList<BlockPos>();
    public boolean isInTheCatacombs = false;

    @Override
    public String getName() {
        return "SecretBot";
    }

    @Override
    public void post() {

    }

    @Override
    public void init() {
        clickedBlock.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("SecretBot")) {
            if (Minecraft.getMinecraft().theWorld != null) {
                try {
                    Vec3i vec3i = new Vec3i(10, 10, 10);
                    if (isInDungeon()) {
                        for (BlockPos blockPos : BlockPos.getAllInBox((new BlockPos(Minecraft.getMinecraft().thePlayer.getPosition())).add(vec3i), new BlockPos(Minecraft.getMinecraft().thePlayer.getPosition().subtract(vec3i)))) {
                            if (Minecraft.getMinecraft().thePlayer.getDistance((double) blockPos.getX(), (double) ((float) blockPos.getY() - Minecraft.getMinecraft().thePlayer.getEyeHeight()), (double) blockPos.getZ()) <= 5) {
                                if (Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() == Blocks.chest || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() == Blocks.lever || getWitherEssence(blockPos)) {
                                    if (!isGhost()) {
                                        if (!clickedBlock.contains(blockPos)) {
                                            this.interactBlock(blockPos);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @SubscribeEvent
    public void AutoRefreshInDungeon(ClientChatReceivedEvent event) {
        if (FunctionManager.getStatus("SecretBot")) {
            String msg = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).toLowerCase();
            if (msg.contains("entered") && msg.contains("catacomb")) {
                clickedBlock.clear();
                Utils.print("检测到加入地牢,已经刷新SecretBot");
            }
            if (msg.contains("warped") && msg.contains("dungeon")) {
                clickedBlock.clear();
                Utils.print("检测到加入地牢,已经刷新SecretBot");
            }
        }
    }

    private void interactBlock(BlockPos pos) {
        Minecraft.getMinecraft().playerController.onPlayerRightClick(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), pos, EnumFacing.fromAngle((double) Minecraft.getMinecraft().thePlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.0D));
        clickedBlock.add(pos);
    }

    private boolean isGhost() {
        for (int i = 0; i < 8; ++i) {
            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
            if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains("haunt")) {
                return true;
            }
        }
        return false;
    }

    private boolean isInDungeon() {
        isInTheCatacombs = false;
        Utils utils = Antimony.instance.utils;
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        final String combatZoneName = "the catacombs";
        final String clearedName = "dungeon cleared";
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), clearedName)) {
                isInTheCatacombs = true;
            }
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), combatZoneName) && !scoreBoardLines.get(size - i).toLowerCase().contains("to")) {
                isInTheCatacombs = true;
            }
        }
        return isInTheCatacombs;
    }
    private boolean getWitherEssence(BlockPos pos){
        if(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.skull){
            TileEntitySkull tileEntity = (TileEntitySkull)Minecraft.getMinecraft().theWorld.getTileEntity(pos);
            if(tileEntity.getSkullType() == 3 && tileEntity.getPlayerProfile() != null && tileEntity.getPlayerProfile().getProperties() != null) {
                Property property = (Property) Iterables.getFirst(tileEntity.getPlayerProfile().getProperties().get("textures"), (Object)null);
                return property != null && property.getValue().equals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=");
            }
        }
        return false;
    }
}
