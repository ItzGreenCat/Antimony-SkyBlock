package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.nukerCore;
import com.greencat.antimony.core.nukerCore2;
import com.greencat.antimony.core.nukerWrapper;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.greencat.antimony.core.PlayerNameFilter.isValid;

public class Nuker {
    nukerCore2 nuker = nukerWrapper.nuker;
    nukerCore core1 = new nukerCore();
    BlockPos pos;
    public Nuker(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("Nuker")){
            nukerWrapper.enable = false;
            nukerWrapper.disable();
            core1.pos = null;
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("Nuker")) {
            if (!event.status) {
                nukerWrapper.enable = false;
                nukerWrapper.disable();
                core1.pos = null;
            } else {
                nukerWrapper.enable = true;
                nukerWrapper.enable();
                core1.pos = null;
            }
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals("Nuker")){
            nukerWrapper.enable = true;
            nukerWrapper.enable();
            core1.pos = null;
        }
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("Nuker")) {
            int type = (Integer)getConfigByFunctionName.get("Nuker","miningType");
            if(type == 0){
                nuker.miningType = nukerCore2.MiningType.NORMAL;
            } else if(type == 1){
                nuker.miningType = nukerCore2.MiningType.ONE_TICK;
            }
            if((Integer)getConfigByFunctionName.get("Nuker","rotation") == 0){
                nuker.rotation = nukerCore2.RotationType.SERVER_ROTATION;
            } else if((Integer)getConfigByFunctionName.get("Nuker","rotation") == 1){
                nuker.rotation = nukerCore2.RotationType.ROTATION;
            }   else if((Integer)getConfigByFunctionName.get("Nuker","rotation") == 2){
                nuker.rotation = nukerCore2.RotationType.SMOOTH;
            }
            nuker.ignoreGround = (Boolean)getConfigByFunctionName.get("Nuker","ignoreGround");
            pos = getBlock();
            if (type != 2) {
                if (nuker.requestBlock) {
                    nuker.putBlock(pos);
                }
            } else {
                try {
                    if (pos != null) {
                        core1.nuke(new Vec3(Objects.requireNonNull(pos)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event){
        if(FunctionManager.getStatus("Nuker")){
            if (pos != null) {
                if(!isInCave()) {
                    Utils.OutlinedBoxWithESP(pos, Chroma.color, false, 2.5F);
                } else {
                    Utils.BoxWithESP(pos, Chroma.color, false);
                    Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPositionVector(),new Vec3(pos.add(0.5D,0.5D,0.5D)),Chroma.color,2.5F);
                }
            }
        }
    }
    private BlockPos getBlock() {
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null){
            return null;
        }
        BlockPos pos = null;
        boolean hasPlayer = false;
        double xPos = Minecraft.getMinecraft().thePlayer.posX;
        double yPos = Minecraft.getMinecraft().thePlayer.posY;
        double zPos = Minecraft.getMinecraft().thePlayer.posZ;
        int bound = (Integer) getConfigByFunctionName.get("Nuker", "radius") * 2;
        List<EntityPlayer> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(xPos - (bound / 2d), yPos - (bound / 2d), zPos - (bound / 2d), xPos + (bound / 2d), yPos + (bound / 2d), zPos + (bound / 2d)), null);
        for(EntityPlayer entity : entities){
            if (isValid(entity,true)) {
                hasPlayer = true;
                break;
            }
        }
        if(hasPlayer && (!(Boolean) getConfigByFunctionName.get("Nuker", "disable"))){
            hasPlayer = false;
        }
        if(!hasPlayer) {
            int nukerType = (Integer) getConfigByFunctionName.get("Nuker", "type");
            if (nukerType == 0) {
                pos = nuker.closestMineableBlock(Blocks.stained_glass);
            }
            if (nukerType == 1) {
                BlockPos pos1 = nuker.closestMineableBlock(Blocks.stained_glass);
                BlockPos pos2 = nuker.closestMineableBlock(Blocks.stained_glass_pane);
                pos = nuker.BlockPosMin(pos1, pos2);
            }
            if (nukerType == 2) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.coal_ore);
                oreList.add(Blocks.iron_ore);
                oreList.add(Blocks.gold_ore);
                oreList.add(Blocks.diamond_ore);
                oreList.add(Blocks.emerald_ore);
                oreList.add(Blocks.redstone_ore);
                oreList.add(Blocks.lapis_ore);
                oreList.add(Blocks.quartz_ore);
                pos = nuker.closestMineableBlock(oreList);
            }
            if (nukerType == 3) {
                pos = nuker.closestMineableBlock(Blocks.stone);
            }
            if (nukerType == 4) {
                pos = nuker.closestMineableBlock(Blocks.netherrack);
            }
            if (nukerType == 5) {
                pos = nuker.closestMineableBlock(Blocks.sand);
            }
            if (nukerType == 6) {
                pos = nuker.closestMineableBlock(Blocks.gold_block);
            }
            if (nukerType == 7) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.prismarine);
                oreList.add(Blocks.wool);
                oreList.add(Blocks.stained_hardened_clay);
                pos = nuker.closestMineableBlock(oreList);
            }
            if (nukerType == 8) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    double x = Minecraft.getMinecraft().thePlayer.posX;
                    double y = Minecraft.getMinecraft().thePlayer.posY;
                    double z = Minecraft.getMinecraft().thePlayer.posZ;
                    List<EntityArmorStand> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - (200 / 2d), y - (256 / 2d), z - (200 / 2d), x + (200 / 2d), y + (256 / 2d), z + (200 / 2d)), null);
                    for (EntityArmorStand entity : entityList) {
                        if (entity != null) {
                            if (entity.getEquipmentInSlot(4) != null && Minecraft.getMinecraft().theWorld.getBlockState(entity.getPosition().up()).getBlock() == Blocks.ice && isInCave()) {
                                BlockPos posTemp = entity.getPosition().up();
                                if (Minecraft.getMinecraft().thePlayer.getDistance(posTemp.getX(), posTemp.getY(), posTemp.getZ()) < 6.0) {
                                    pos = posTemp;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (nukerType == 9) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.prismarine);
                oreList.add(Blocks.wool);
                oreList.add(Blocks.stone);
                oreList.add(Blocks.stained_hardened_clay);
                pos = nuker.closestMineableBlock(oreList, true);
            }
            if (nukerType == 10) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.log);
                oreList.add(Blocks.log2);
                pos = nuker.closestMineableBlock(oreList);
            }
            if (nukerType == 11) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.stone);
                oreList.add(Blocks.cobblestone);
                pos = nuker.closestMineableBlock(oreList);
            }
            if (nukerType == 12) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.prismarine);
                oreList.add(Blocks.wool);
                oreList.add(Blocks.stone);
                oreList.add(Blocks.stained_hardened_clay);
                pos = nuker.closestMineableBlockBlueWool(oreList, true);
            }
            if (nukerType == 13) {
                pos = nuker.closestMineableBlock(Blocks.obsidian);
            }
            return pos;
        } else {
            return null;
        }
    }
    private boolean isInCave() {
        boolean isInCave = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if(Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), "glacial cave")){
                isInCave = true;
            }
        }
        return isInCave;
    }
}
