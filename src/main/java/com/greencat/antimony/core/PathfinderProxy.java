package com.greencat.antimony.core;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PathfinderProxy {
    public static boolean running = false;
    static Minecraft mc = Minecraft.getMinecraft();
    public static void calcPathDistance(BlockPos target, double distance){
        if(!running && !FunctionManager.getStatus("Pathfinding")) {
            new Thread(() -> {
                Utils.print("PathfinderProxy已接受寻路请求");
                running = true;
                WorldClient world = mc.theWorld;
                List<BlockPos> possibleBlocks = new ArrayList<>();
                for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(target.getX() + distance, target.getY() + distance, target.getZ() + distance), new BlockPos(target.getX() - distance, target.getY() - distance, target.getZ() - distance))) {
                    if (!(world.getBlockState(pos).getBlock() instanceof BlockLiquid) && !(world.getBlockState(pos).getBlock() instanceof BlockDoublePlant) && !(world.getBlockState(pos).getBlock() == Blocks.air) && !(world.getBlockState(pos).getBlock() == Blocks.stained_glass_pane)  && (world.getBlockState(pos.up(1)).getBlock() == Blocks.air) && (world.getBlockState(pos.up(2)).getBlock() == Blocks.air)) {
                        possibleBlocks.add(pos);
                    }
                }
                Utils.print("可能的方块数量: " + possibleBlocks.size());
                Deque<BlockPos> sortList = new ArrayDeque<>();
                double smallest = 9999;
                BlockPos closest = null;
                for(BlockPos pos : possibleBlocks){
                    double dist = pos.distanceSq(target);
                    if (dist < smallest) {
                        smallest = dist;
                        closest = pos;
                        sortList.addFirst(pos);
                    } else {
                        sortList.addLast(pos);
                    }
                }
                for (BlockPos pos : sortList) {
                    Pathfinder.setup(new BlockPos(Utils.floorVec(Minecraft.getMinecraft().thePlayer.getPositionVector())), pos, 0.0D);
                    if (Pathfinder.hasPath()) {
                        Utils.print("PathfinderProxy确定目标: " + pos);
                        break;
                    }
                }
                if (!Pathfinder.hasPath()) {
                    Pathfinder.setup(new BlockPos(Utils.floorVec(Minecraft.getMinecraft().thePlayer.getPositionVector())), closest, 0.0D);
                    if (!Pathfinder.hasPath()) {
                        Utils.print("PathfinderProxy无法确定目标");
                    } else {
                        Utils.print("PathfinderProxy确定目标: " + closest);
                    }
                }
                running = false;
            }).start();
        }
    }
    public static void calcPathDistance(BlockPos target, double distance,int loop) {
        if (!running && !FunctionManager.getStatus("Pathfinding")) {
            new Thread(() -> {
                Utils.print("PathfinderProxy已接受寻路请求");
                running = true;
                WorldClient world = mc.theWorld;
                List<BlockPos> possibleBlocks = new ArrayList<>();
                for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(target.getX() + distance, target.getY() + distance, target.getZ() + distance), new BlockPos(target.getX() - distance, target.getY() - distance, target.getZ() - distance))) {
                    if (!(world.getBlockState(pos).getBlock() instanceof BlockLiquid) && !(world.getBlockState(pos).getBlock() instanceof BlockDoublePlant) && !(world.getBlockState(pos).getBlock() == Blocks.air) && !(world.getBlockState(pos).getBlock() == Blocks.stained_glass_pane) && (world.getBlockState(pos.up(1)).getBlock() == Blocks.air) && (world.getBlockState(pos.up(2)).getBlock() == Blocks.air)) {
                        possibleBlocks.add(pos);
                    }
                }
                Utils.print("可能的方块数量: " + possibleBlocks.size());
                Deque<BlockPos> sortList = new ArrayDeque<>();
                double smallest = 9999;
                BlockPos closest = null;
                for (BlockPos pos : possibleBlocks) {
                    double dist = pos.distanceSq(target);
                    if (dist < smallest) {
                        smallest = dist;
                        closest = pos;
                        sortList.addFirst(pos);
                    } else {
                        sortList.addLast(pos);
                    }
                }
                for (BlockPos pos : sortList) {
                    Pathfinder.setup(new BlockPos(Utils.floorVec(Minecraft.getMinecraft().thePlayer.getPositionVector())), pos, 0.0D,loop);
                    if (Pathfinder.hasPath()) {
                        Utils.print("PathfinderProxy确定目标: " + pos);
                        break;
                    }
                }
                if (!Pathfinder.hasPath()) {
                    Pathfinder.setup(new BlockPos(Utils.floorVec(Minecraft.getMinecraft().thePlayer.getPositionVector())), closest, 0.0D,loop);
                    if (!Pathfinder.hasPath()) {
                        Utils.print("PathfinderProxy无法确定目标");
                    } else {
                        Utils.print("PathfinderProxy确定目标: " + closest);
                    }
                }
                running = false;
            }).start();
        }
    }
}
