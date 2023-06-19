package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
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
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.greencat.antimony.core.PlayerNameFilter.isValid;

public class Nuker extends FunctionStatusTrigger {
    nukerCore2 nuker = nukerWrapper.nuker;
    nukerCore core1 = new nukerCore();
    BlockPos pos;
    public Nuker(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @Override
    public String getName() {
        return "Nuker";
    }

    @Override
    public void post() {
        nukerWrapper.enable = false;
        nukerWrapper.disable();
        core1.pos = null;
        nukerCore.skipBPS = 0;
    }

    @Override
    public void init() {
        nukerWrapper.enable = true;
        nukerWrapper.enable();
        core1.pos = null;
        nukerCore.skipBPS = 0;
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        nukerCore.extraBPS = (Integer)ConfigInterface.get("Nuker","extraBPS");
        nukerCore.noDelay = (Boolean)ConfigInterface.get("Nuker","noDelay");
        nukerCore.noRotate = (Boolean)ConfigInterface.get("Nuker","noCore1Rotate");
        nukerCore.bpsLimit = (Integer)ConfigInterface.get("Nuker","bpsSkip");
        if(FunctionManager.getStatus("Nuker")) {
            int type = (Integer) ConfigInterface.get("Nuker","miningType");
            if(type == 0){
                nuker.miningType = nukerCore2.MiningType.NORMAL;
            } else if(type == 1){
                nuker.miningType = nukerCore2.MiningType.ONE_TICK;
            }
            if((Integer) ConfigInterface.get("Nuker","rotation") == 0){
                nuker.rotation = nukerCore2.RotationType.SERVER_ROTATION;
            } else if((Integer) ConfigInterface.get("Nuker","rotation") == 1){
                nuker.rotation = nukerCore2.RotationType.ROTATION;
            }   else if((Integer) ConfigInterface.get("Nuker","rotation") == 2){
                nuker.rotation = nukerCore2.RotationType.SMOOTH;
            }
            nuker.ignoreGround = (Boolean) ConfigInterface.get("Nuker","ignoreGround");
            pos = getBlock();
            if (type != 2) {
                if (nuker.requestBlock) {
                    nuker.putBlock(pos);
                }
            } else {
                try {
                    if (pos != null) {
                        core1.nuke(new Vec3(Objects.requireNonNull(pos)),this,this.getClass().getDeclaredMethod("getBlock"));
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
        int bound = (Integer) ConfigInterface.get("Nuker", "radius") * 2;
        List<EntityPlayer> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(xPos - (bound / 2d), yPos - (bound / 2d), zPos - (bound / 2d), xPos + (bound / 2d), yPos + (bound / 2d), zPos + (bound / 2d)), null);
        for(EntityPlayer entity : entities){
            if (isValid(entity,true)) {
                hasPlayer = true;
                break;
            }
        }
        if(hasPlayer && (!(Boolean) ConfigInterface.get("Nuker", "disable"))){
            hasPlayer = false;
        }
        if(!hasPlayer) {
            int nukerType = (Integer) ConfigInterface.get("Nuker", "type");
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
            if (nukerType == 14) {
                pos = nuker.closestCropBlock();
            }
            if (nukerType == 15) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.grass);
                oreList.add(Blocks.tallgrass);
                oreList.add(Blocks.red_flower);
                oreList.add(Blocks.yellow_flower);
                oreList.add(Blocks.leaves);
                oreList.add(Blocks.leaves2);
                pos = nuker.closestMineableBlock(oreList);
            }
            if (nukerType == 16) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.melon_block);
                pos = nuker.closestMineableBlock(oreList);
            }
            if (nukerType == 17) {
                List<Block> oreList = new ArrayList<Block>();
                oreList.add(Blocks.pumpkin);
                pos = nuker.closestMineableBlock(oreList);
            }
            if (nukerType == 18) {
                pos = nuker.closestCropBlock(Blocks.potatoes);
            }
            if (nukerType == 19) {
                pos = nuker.closestCropBlock(Blocks.wheat);
            }
            if (nukerType == 20) {
                pos = nuker.closestCropBlock(Blocks.carrots);
            }
            if (nukerType == 21) {
                pos = nuker.closestCropBlock(Blocks.nether_wart);
            }
            if (nukerType == 22) {
                pos = nuker.closestMineableBlockCheckDown(Blocks.reeds);
            }
            if (nukerType == 23) {
                pos = nuker.closestMineableBlockCheckDown(Blocks.cactus);
            }
            if (nukerType == 24) {
                pos = nuker.closestCropBlock(Blocks.cocoa);
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
    @SubscribeEvent
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if (FunctionManager.getStatus("Nuker")) {
            Utils.print("检测到世界服务器改变,自动关闭Nuker");
            FunctionManager.setStatus("Nuker", false);
        }
    }
}
