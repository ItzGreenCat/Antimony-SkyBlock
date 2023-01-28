package com.greencat.antimony.core.pathfinding;

import com.greencat.antimony.core.Pathfinding;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Comparator;

public class EtherwarpPathfinder {
    private Vec3 startVec3;
    private Vec3 endVec3;
    private ArrayList<Vec3> path = new ArrayList();
    private ArrayList<EtherwarpPathfinder.Hub> hubs = new ArrayList();
    private ArrayList<EtherwarpPathfinder.Hub> hubsToWork = new ArrayList();
    private double minDistanceSquared;
    private boolean nearest = true;
    private static Vec3[] flatCardinalDirections = new Vec3[]{new Vec3(1.0D, 0.0D, 0.0D), new Vec3(-1.0D, 0.0D, 0.0D), new Vec3(0.0D, 0.0D, 1.0D), new Vec3(0.0D, 0.0D, -1.0D)};
    private static Minecraft mc = Minecraft.getMinecraft();

    public EtherwarpPathfinder(Vec3 startVec3, Vec3 endVec3, double minDistanceSquared) {
        this.startVec3 = Utils.floorVec(startVec3);
        this.endVec3 = Utils.floorVec(endVec3);
        this.minDistanceSquared = minDistanceSquared;
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public void compute() {
        this.compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        try {
            path.clear();
            hubsToWork.clear();
            ArrayList<Vec3> initPath = new ArrayList<>();
            initPath.add(startVec3);
            hubsToWork.add(new Hub(startVec3, null, initPath, startVec3.squareDistanceTo(endVec3), 0.0, 0.0));
            search:
            for (int i = 0; i < loops; ++i) {
                hubsToWork.sort(new CompareHub());
                int j = 0;
                if (hubsToWork.size() == 0) {
                    break;
                }
                for (Hub hub : new ArrayList<>(hubsToWork)) {
                    if (++j > depth) {
                        break;
                    }

                    hubsToWork.remove(hub);
                    hubs.add(hub);

                    for (BlockPos blockPos : Utils.getAllTeleportableBlocksNew(Utils.ceilVec(hub.getLoc()).addVector(0.5, 1.62 - 0.08, 0.5), 16)) {
                        Vec3 loc = new Vec3(blockPos);
                        if (addHub(hub, loc, 0)) {
                            break search;
                        }
                    }
                }
            }
            path = hubs.get(0).getPath();
        } catch(Exception ignored){

        }
    }

    public static boolean checkPositionValidity(Vec3 loc, boolean checkGround) {
        return checkPositionValidity((int)loc.xCoord, (int)loc.yCoord, (int)loc.zCoord, checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        BlockPos block2 = new BlockPos(x, y + 1, z);
        BlockPos block3 = new BlockPos(x, y - 1, z);
        return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(block3);
    }

    public static boolean isSlab(Vec3 loc, BlockSlab.EnumBlockHalf slabType) {
        IBlockState bs = mc.theWorld.getBlockState(new BlockPos(loc));
        return bs.getBlock() instanceof BlockSlab && bs.getValue(BlockSlab.HALF) == slabType;
    }

    private static boolean isBlockSolid(BlockPos block) {
        IBlockState bs = mc.theWorld.getBlockState(block);
        if (bs == null) {
            return false;
        } else {
            Block b = bs.getBlock();
            return mc.theWorld.isBlockFullCube(block) || b instanceof BlockSlab || b instanceof BlockStairs || b instanceof BlockCactus || b instanceof BlockChest || b instanceof BlockEnderChest || b instanceof BlockSkull || b instanceof BlockPane || b instanceof BlockFence || b instanceof BlockWall || b instanceof BlockGlass || b instanceof BlockPistonBase || b instanceof BlockPistonExtension || b instanceof BlockPistonMoving || b instanceof BlockStainedGlass || b instanceof BlockTrapDoor || b == Blocks.soul_sand;
        }
    }

    private static boolean isSafeToWalkOn(BlockPos block) {
        IBlockState bs = mc.theWorld.getBlockState(block);
        if (bs == null) {
            return false;
        } else {
            Block b = bs.getBlock();
            return !(b instanceof BlockFence) && !(b instanceof BlockWall);
        }
    }


    public class CompareHub implements Comparator<EtherwarpPathfinder.Hub> {
        public CompareHub() {
        }

        public int compare(EtherwarpPathfinder.Hub o1, EtherwarpPathfinder.Hub o2) {
            return (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
        }
    }
    public static boolean checkPositionValidity(Vec3 loc) {
        return checkPositionValidity(new BlockPos((int) loc.xCoord, (int) loc.yCoord, (int) loc.zCoord));
    }

    public static boolean checkPositionValidity(BlockPos blockPos) {
        return canTeleportTo(blockPos);
    }

    private static boolean canVecBeSeen(Vec3 from, Vec3 to) {
        return Utils.canVecBeSeenFromVec(from.addVector(0.5, 0.5, 0.5), to.addVector(0.5, 0.5, 0.5), 0.1f);
    }

    private static boolean canTeleportTo(BlockPos blockPos) {
        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
        Block block = blockState.getBlock();
        return block.isCollidable() && block != Blocks.carpet && block != Blocks.skull &&
                block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, blockPos, blockState) != null &&
                block != Blocks.wall_sign && block != Blocks.standing_sign &&
                Minecraft.getMinecraft().theWorld.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.air &&
                Minecraft.getMinecraft().theWorld.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.air;
    }

    public Hub isHubExisting(Vec3 loc) {
        for (Hub hub : hubs) {
            if (hub.getLoc().xCoord == loc.xCoord && hub.getLoc().yCoord == loc.yCoord && hub.getLoc().zCoord == loc.zCoord) {
                return hub;
            }
        }
        for (Hub hub : hubsToWork) {
            if (hub.getLoc().xCoord == loc.xCoord && hub.getLoc().yCoord == loc.yCoord && hub.getLoc().zCoord == loc.zCoord) {
                return hub;
            }
        }
        return null;
    }

    public boolean addHub(Hub parent, Vec3 loc, double cost) {
        Hub existingHub = isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getTotalCost();
        }
        if (existingHub == null) {
            if ((loc.xCoord == endVec3.xCoord && loc.yCoord == endVec3.yCoord && loc.zCoord == endVec3.zCoord) || (minDistanceSquared != 0.0 && loc.squareDistanceTo(endVec3) <= minDistanceSquared)) {
                path.clear();
                (path = parent.getPath()).add(loc);
                return true;
            }
            ArrayList<Vec3> path = new ArrayList<>(parent.getPath());
            path.add(loc);
            hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(endVec3), cost, totalCost));
        } else if (existingHub.getCost() > cost) {
            ArrayList<Vec3> path = new ArrayList<>(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(endVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }
        return false;
    }

    private class Hub {
        private Vec3 loc = null;
        private EtherwarpPathfinder.Hub parent = null;
        private ArrayList<Vec3> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;

        public Hub(Vec3 loc, EtherwarpPathfinder.Hub parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }

        public Vec3 getLoc() {
            return this.loc;
        }

        public EtherwarpPathfinder.Hub getParent() {
            return this.parent;
        }

        public ArrayList<Vec3> getPath() {
            return this.path;
        }

        public double getSquareDistanceToFromTarget() {
            return this.squareDistanceToFromTarget;
        }

        public double getCost() {
            return this.cost;
        }

        public void setLoc(Vec3 loc) {
            this.loc = loc;
        }

        public void setParent(EtherwarpPathfinder.Hub parent) {
            this.parent = parent;
        }

        public void setPath(ArrayList<Vec3> path) {
            this.path = path;
        }

        public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getTotalCost() {
            return this.totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
    }
}
