package com.greencat.antimony.common.function;

import com.greencat.antimony.core.EtherwarpTeleport;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.type.Rotation;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AutoArmadillo {
    public static boolean isEnable = false;
    public static long latest;
    public static long latestSnake = 0;
    public static long latestFinish = 0;
    public static long latestSwitch = 0;
    public static int stage = 0;
    public static BlockPos pos = null;
    public AutoArmadillo() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("AutoArmadillo")){
            isEnable = true;
        } else {
            isEnable = false;
        }
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event){
        if(event.function.getName().equals("AutoArmadillo")){
            post();
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionDisabledEvent event){
        if(event.function.getName().equals("AutoArmadillo")){
            init();
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("AutoArmadillo")){
            if(event.status){
                init();
            } else {
                post();
            }
        }
    }
    private void init(){
        stage = -1;
        pos = null;
    }
    private void post(){
        stage = -1;
        pos = null;
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        try {
            if (isEnable && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                if (stage == -1) {
                    if (System.currentTimeMillis() - latestFinish >= 250) {
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
                    SwitchRod();
                    stage = 1;
                }
                if (stage == 1) {
                    if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    }
                    stage = 2;
                }
                if (stage == 2) {
                    double playerX = Minecraft.getMinecraft().thePlayer.posX;
                    double playerY = Minecraft.getMinecraft().thePlayer.posY;
                    double playerZ = Minecraft.getMinecraft().thePlayer.posZ;
                    int bound = 3;
                    List<EntityArmorStand> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(playerX - (bound / 2d), playerY - (256 / 2d), playerZ - (bound / 2d), playerX + (bound / 2d), playerY + (256 / 2d), playerZ + (bound / 2d)), null);
                    for (EntityArmorStand entity : entityList) {
                        if (entity.getEquipmentInSlot(4) != null) {
                            ItemStack stack = entity.getEquipmentInSlot(4);
                            if (stack.getItem() == Items.skull) {
                                if (Minecraft.getMinecraft().thePlayer.isRiding()) {
                                    stage = 3;
                                } else {
                                    C02PacketUseEntity c02 = new C02PacketUseEntity(entity, C02PacketUseEntity.Action.INTERACT);
                                    Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(c02);
                                }
                                checkSwitch("Drill");
                                checkSwitch("Gauntlet");
                                break;
                            }
                        }
                    }
                    Minecraft.getMinecraft().thePlayer.rotationPitch = 0;
                }
                if (stage == 3) {
                    if (!Minecraft.getMinecraft().thePlayer.isRiding()) {
                        stage = 2;
                    }
                    Minecraft.getMinecraft().thePlayer.rotationPitch = 0;
                    BlockPos ArmadilloPos = new BlockPos(Minecraft.getMinecraft().thePlayer.getPositionVector()).down();
                    List<BlockPos> posList = new ArrayList<BlockPos>();
                    for (BlockPos pos : BlockPos.getAllInBox(ArmadilloPos.add(-1, 0, -1), ArmadilloPos.add(1, 0, 1))) {
                        if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.stained_glass || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.stained_glass_pane) {
                            posList.add(pos);
                        }
                    }
                    if (posList.isEmpty()) {
                        if (System.currentTimeMillis() - latestSwitch > 45 && Minecraft.getMinecraft().theWorld.getBlockState(ArmadilloPos.down()).getBlock() != Blocks.air) {
                            stage = 4;
                        }
                    } else {
                        for (BlockPos pos : posList) {
                            Rotation rotation = Utils.getRotation(pos);
                            Minecraft.getMinecraft().thePlayer.rotationPitch = 0;
                            Minecraft.getMinecraft().thePlayer.rotationYaw = rotation.getYaw();
                        }
                        posList.clear();
                    }
                }
                if (stage == 4) {
                    if (!Minecraft.getMinecraft().thePlayer.isRiding()) {
                        stage = 2;
                    }
                    BlockPos ArmadilloPos = new BlockPos(Minecraft.getMinecraft().thePlayer.getPositionVector()).down();
                    if (Minecraft.getMinecraft().theWorld.getBlockState(ArmadilloPos.down()).getBlock() == Blocks.stained_glass || Minecraft.getMinecraft().theWorld.getBlockState(ArmadilloPos.down()).getBlock() == Blocks.stained_glass_pane ||
                            Minecraft.getMinecraft().theWorld.getBlockState(ArmadilloPos.down(2)).getBlock() == Blocks.stained_glass || Minecraft.getMinecraft().theWorld.getBlockState(ArmadilloPos.down(2)).getBlock() == Blocks.stained_glass_pane
                    ) {
                        Minecraft.getMinecraft().thePlayer.rotationPitch = 90;
                        stage = 3;
                        latestSwitch = System.currentTimeMillis();
                    } else {
                        List<BlockPos> posList = new ArrayList<BlockPos>();
                        for (BlockPos pos : BlockPos.getAllInBox(ArmadilloPos.add(-1, 0, -1), ArmadilloPos.add(1, 0, 1))) {
                            if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.stained_glass || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.stained_glass_pane) {
                                posList.add(pos);
                            }
                        }
                        if (posList.isEmpty()) {
                            stage = 5;
                        } else {
                            Minecraft.getMinecraft().thePlayer.rotationPitch = 90;
                            stage = 3;
                            latestSwitch = System.currentTimeMillis();
                        }
                    }
                }
                if (stage == 5) {
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                    latestSnake = System.currentTimeMillis();
                    stage = 6;
                }
                if (stage == 6) {
                    if (System.currentTimeMillis() - latestSnake > 200) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
                        latestFinish = System.currentTimeMillis();
                        stage = -1;
                    }
                }
            }
        } catch(Exception ignored){

        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if(isEnable && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            BlockPos ArmadilloPos = new BlockPos(Minecraft.getMinecraft().thePlayer.getPositionVector()).down();
            for (BlockPos pos : BlockPos.getAllInBox(ArmadilloPos.add(-1, 0, -1), ArmadilloPos.add(1, 0, 1))) {
                Utils.OutlinedBoxWithESP(pos, Color.CYAN,false);
            }
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
    public static void SwitchRod(){
        try {
            if (System.currentTimeMillis() - latest >= 0) {
                latest = System.currentTimeMillis();
                ItemStack hand = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (hand == null || hand.getItem() != Items.fishing_rod) {
                    for (int i = 0; i < 8; ++i) {
                        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
                        if (stack != null && stack.getItem() == Items.fishing_rod) {
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
