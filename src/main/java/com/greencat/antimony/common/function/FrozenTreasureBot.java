package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.core.PathfinderProxy;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.nukerCore2;
import com.greencat.antimony.core.nukerWrapper;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.greencat.antimony.utils.Utils.print;

public class FrozenTreasureBot {
    public BlockPos pos = null;
    public BlockPos last = null;
    public BlockPos pathfinderPos = null;
    static nukerCore2 nuker = nukerWrapper.nuker;
    static List<BlockPos> ignoreList = new ArrayList<BlockPos>();
    int tick = 0;
    Utils utils = new Utils();
    public FrozenTreasureBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if (event.function.getName().equals("FrozenTreasureBot")) {
            nukerWrapper.enable = false;
            nukerWrapper.disable();
            pathfinderPos = null;
            FunctionManager.setStatus("Pathfinding", false);
        }
    }

    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {
        if (event.function.getName().equals("FrozenTreasureBot")) {
            if (!event.status) {
                nukerWrapper.enable = false;
                nukerWrapper.disable();
                pathfinderPos = null;
                FunctionManager.setStatus("Pathfinding", false);
            } else {
                nukerWrapper.enable = true;
                nukerWrapper.enable();
            }
        }
    }

    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event) {
        if (event.function.getName().equals("FrozenTreasureBot")) {
            nukerWrapper.enable = true;
            nukerWrapper.enable();
        }
    }

    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("FrozenTreasureBot")) {
            pos = getBlock();
            if(pathfinderPos == null && !FunctionManager.getStatus("Pathfinding") && tick + 1 > 20){
                pathfinderPos = closestTreasure();
                if(pathfinderPos != null){
                    if(!FunctionManager.getStatus("Pathfinding")) {
                        PathfinderProxy.calcPathDistance(pathfinderPos,6);
                        if (!PathfinderProxy.running && Pathfinder.hasPath()) {
                            FunctionManager.setStatus("Pathfinding", true);
                            if (ignoreList.size() + 1 > 3) {
                                ignoreList.clear();
                            }
                            ignoreList.add(pathfinderPos);
                            last = pathfinderPos;
                        } else {
                            print("无法找到的路径");
                        }
                    }
                }
                tick = 0;
            } else if(tick + 1 <= 20){
                tick = tick + 1;
            }
            pathfinderPos = null;
            if (nuker.requestBlock) {
                nuker.putBlock(pos);
            }
        }
    }
    @SubscribeEvent
    public void BlockChangeEvent(CustomEventHandler.BlockChangeEvent event) {
        if (FunctionManager.getStatus("FrozenTreasureBot")) {
            if (BlockPosEquals(event.pos, last)) {
                if(FunctionManager.getStatus("Pathfinding")) {
                    FunctionManager.setStatus("Pathfinding", false);
                    last = null;
                }
            }
        }
    }
    private BlockPos getBlock() {
        BlockPos pos = null;
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
        return pos;
    }
    private static BlockPos closestTreasure() {
        if (Minecraft.getMinecraft().theWorld != null) {
            double smallest = 9999;
            Vec3 closest = null;
            ArrayList<BlockPos> stone = new ArrayList<>();
            double x = Minecraft.getMinecraft().thePlayer.posX;
            double y = Minecraft.getMinecraft().thePlayer.posY;
            double z = Minecraft.getMinecraft().thePlayer.posZ;
            List<EntityArmorStand> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - (200 / 2d), y - (256 / 2d), z - (200 / 2d), x + (200 / 2d), y + (256 / 2d), z + (200 / 2d)), null);
            for (EntityArmorStand entity : entityList) {
                if (entity != null) {
                    BlockPos pos = entity.getPosition().up();
                    if (entity.getEquipmentInSlot(4) != null && (Minecraft.getMinecraft().theWorld.getBlockState(entity.getPosition().up()).getBlock() == Blocks.ice)) {
                        if (!isIgnored(pos)) {
                            stone.add(pos);
                        }
                    }
                }
            }
            if (!stone.isEmpty()) {
                for (BlockPos pos : stone) {
                    Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
                    Vec3 vec3 = new Vec3(pos);
                    double dist = vec3.distanceTo(playerVec);
                    if (dist < smallest) {
                        smallest = dist;
                        closest = vec3;
                    }
                }
            }
            if (closest != null) {
                return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
            }
        }
        return null;
    }
    private static boolean isInCave() {
        boolean isInCave = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), "glacial cave")) {
                isInCave = true;
            }
        }
        return isInCave;
    }
    private static boolean isIgnored(BlockPos pos){
        boolean isIgnored = false;
        for(BlockPos ignored : ignoreList){
            if(BlockPosEquals(pos,ignored)){
                isIgnored = true;
                break;
            }
        }
        return isIgnored;
    }
    public static boolean BlockPosEquals(BlockPos pos1,BlockPos pos2) {
        return pos1.equals(pos2);
    }
}
