package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.EtherwarpTeleport;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.nukerCore2;
import com.greencat.antimony.core.nukerWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GemstoneBot extends FunctionStatusTrigger {
    public static boolean isEnable = false;
    public static long latest;
    public static long latestFinish = 0;
    public static int stage = 0;
    static nukerCore2 nuker = nukerWrapper.nuker;
    public static BlockPos pos = null;
    public static BlockPos nukerPos = null;
    public GemstoneBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("GemstoneBot")){
            isEnable = true;
        } else {
            isEnable = false;
        }
    }

    @Override
    public String getName() {
        return "GemstoneBot";
    }
    @Override
    public void init(){
        stage = -1;
        pos = null;
        nukerWrapper.enable = true;
        nukerWrapper.enable();
    }
    @Override
    public void post(){
        stage = -1;
        pos = null;
        nukerWrapper.enable = false;
        nukerWrapper.disable();
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        try {
            if (isEnable && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                if (stage == -1) {
                    if (System.currentTimeMillis() - latestFinish >= (Integer) ConfigInterface.get("GemstoneBot","delay")) {
                        pos = EtherwarpTeleport.position.get(0);
                        if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() != Blocks.air) {
                            EtherwarpTeleport.next();
                            stage = -2;
                        }
                    }
                }
                if (stage == -2) {
                    BlockPos posDown = new BlockPos(Minecraft.getMinecraft().thePlayer.getPositionVector()).down();
                    if (pos.getX() == posDown.getX() && pos.getY() == posDown.getY() && pos.getZ() == posDown.getZ()) {
                        stage = 0;
                    }
                }
                if (stage == 0) {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                    checkSwitch("Gauntlet");
                    checkSwitch("Drill");
                    stage = 1;
                }
                if (stage == 1) {
                    nuker.miningType = nukerCore2.MiningType.NORMAL;
                    nuker.rotation = nukerCore2.RotationType.SMOOTH;
                    if (!FunctionManager.getStatus("NukerWrapper")) {
                        nukerWrapper.enable();
                    }
                    if(nuker.requestBlock) {
                        if((Boolean) ConfigInterface.get("GemstoneBot","panel")) {
                            BlockPos pos1 = nuker.closestMineableBlock(Blocks.stained_glass);
                            BlockPos pos2 = nuker.closestMineableBlock(Blocks.stained_glass_pane);
                            nukerPos = nuker.BlockPosMin(pos1, pos2);
                        } else {
                            nukerPos = nuker.closestMineableBlock(Blocks.stained_glass);
                        }
                        if (nukerPos == null) {
                            stage = 2;
                        } else {
                            nuker.putBlock(nukerPos);
                        }
                    }
                }
                if (stage == 2) {
                    latestFinish = System.currentTimeMillis();
                    stage = -1;
                }
            }
        } catch(Exception ignored){

        }
    }
    public static void checkSwitch(String name){
        try {
            if (System.currentTimeMillis() - latest >= 0) {
                latest = System.currentTimeMillis();
                ItemStack hand = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (hand == null || !StringUtils.stripControlCodes(hand.getDisplayName().toLowerCase()).contains(name.toLowerCase())) {
                    for (int i = 0; i < 8; ++i) {
                        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                        if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName().toLowerCase()).contains(name.toLowerCase())) {
                            Minecraft.getMinecraft().thePlayer.inventory.currentItem = i;
                            break;
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
