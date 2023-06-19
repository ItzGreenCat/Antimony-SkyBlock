package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiObbyTrap {
    public AntiObbyTrap(){
        MinecraftForge.EVENT_BUS.register(this);
        //CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void /*onMotion*/onTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null && FunctionManager.getStatus("AntiObbyTrap")) {
            BlockPos obsidianpos = new BlockPos(new Vec3(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + 1, Minecraft.getMinecraft().thePlayer.posZ));
            Block obsidianblock = Minecraft.getMinecraft().theWorld.getBlockState(obsidianpos).getBlock();
            if (Block.getIdFromBlock(obsidianblock) == 49) {
                bestTool(Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX(), Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY(),
                        Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ());
                BlockPos downpos = new BlockPos(new Vec3(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - 1, Minecraft.getMinecraft().thePlayer.posZ));
                Minecraft.getMinecraft().playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
            }
        }
    }

    private void bestTool(int x,int y,int z) {
        int blockId = Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
        int bestSlot = 0;
        float f = -1.0f;
        for (int i = 0; i < 8; ++i) {
            try {
                ItemStack curSlot = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                if ((curSlot.getItem() instanceof ItemTool || curSlot.getItem() instanceof ItemSword || curSlot.getItem() instanceof ItemShears) && curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f) {
                    bestSlot = i;
                    f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
                }
            } catch (Exception ignored) {
            }
        }
        if (f != -1.0f) {
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = bestSlot;
            Minecraft.getMinecraft().playerController.updateController();
        }
    }
}
