package com.greencat.antimony.core;

import com.greencat.antimony.utils.Utils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class CPathfinder{
    private Vec3 startVec3;
    private Vec3 endVec3;
    private ArrayList<Vec3> path = new ArrayList();
    private ArrayList<CPathfinder.Hub> hubs = new ArrayList();
    private ArrayList<CPathfinder.Hub> hubsToWork = new ArrayList();
    private double minDistanceSquared;
    private boolean nearest = true;
    private static Vec3[] flatCardinalDirections = new Vec3[]{new Vec3(1.0D, 0.0D, 0.0D), new Vec3(-1.0D, 0.0D, 0.0D), new Vec3(0.0D, 0.0D, 1.0D), new Vec3(0.0D, 0.0D, -1.0D)};
    private static Minecraft mc = Minecraft.getMinecraft();

    public CPathfinder(Vec3 startVec3, Vec3 endVec3, double minDistanceSquared) {
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
            this.path.clear();
            this.hubsToWork.clear();
            ArrayList<Vec3> initPath = new ArrayList();
            initPath.add(this.startVec3);
            this.hubsToWork.add(new CPathfinder.Hub(this.startVec3, (CPathfinder.Hub) null, initPath, this.startVec3.squareDistanceTo(this.endVec3), 0.0D, 0.0D));

            label96:
            for (int i = 0; i < loops; ++i) {
                Collections.sort(this.hubsToWork, new CPathfinder.CompareHub());
                int j = 0;
                if (this.hubsToWork.size() == 0) {
                    break;
                }

                Iterator var6 = (new ArrayList(this.hubsToWork)).iterator();

                while (var6.hasNext()) {
                    CPathfinder.Hub hub = (CPathfinder.Hub) var6.next();
                    ++j;
                    if (j > depth) {
                        break;
                    }

                    this.hubsToWork.remove(hub);
                    this.hubs.add(hub);
                    Vec3[] var8 = flatCardinalDirections;
                    int var9 = var8.length;

                    int var10;
                    Vec3 direction;
                    Vec3 loc;
                    for (var10 = 0; var10 < var9; ++var10) {
                        direction = var8[var10];
                        loc = Utils.ceilVec(hub.getLoc().add(direction));
                        if (checkPositionValidity(loc, true)) {
                            if (isSlab(loc.addVector(0.0D, -1.0D, 0.0D), BlockSlab.EnumBlockHalf.BOTTOM)) {
                                if (this.addHub(hub, loc.addVector(0.0D, -0.5D, 0.0D), 0.0D)) {
                                    break label96;
                                }
                            } else if (this.addHub(hub, loc, 0.0D)) {
                                break label96;
                            }
                        }
                    }

                    var8 = flatCardinalDirections;
                    var9 = var8.length;

                    for (var10 = 0; var10 < var9; ++var10) {
                        direction = var8[var10];
                        loc = Utils.ceilVec(hub.getLoc().add(direction).addVector(0.0D, 1.0D, 0.0D));
                        if (checkPositionValidity(loc, true) && checkPositionValidity(hub.getLoc().addVector(0.0D, 1.0D, 0.0D), false)) {
                            if (isSlab(loc.addVector(0.0D, -1.0D, 0.0D), BlockSlab.EnumBlockHalf.BOTTOM)) {
                                if (this.addHub(hub, loc.addVector(0.0D, -0.5D, 0.0D), 0.0D)) {
                                    break label96;
                                }
                            } else if (!isSlab(hub.getLoc(), BlockSlab.EnumBlockHalf.BOTTOM) && this.addHub(hub, loc, 0.0D)) {
                                break label96;
                            }
                        }
                    }

                    var8 = flatCardinalDirections;
                    var9 = var8.length;

                    for (var10 = 0; var10 < var9; ++var10) {
                        direction = var8[var10];
                        loc = Utils.ceilVec(hub.getLoc().add(direction).addVector(0.0D, -1.0D, 0.0D));
                        if (checkPositionValidity(loc, true) && checkPositionValidity(loc.addVector(0.0D, 1.0D, 0.0D), false)) {
                            if (isSlab(loc, BlockSlab.EnumBlockHalf.BOTTOM)) {
                                if (this.addHub(hub, loc.addVector(0.0D, 0.5D, 0.0D), 0.0D)) {
                                    break label96;
                                }
                            } else if (this.addHub(hub, loc, 0.0D)) {
                                break label96;
                            }
                        }
                    }
                }
            }

            if (this.nearest) {
                this.hubs.sort(new CPathfinder.CompareHub());
                this.path = ((CPathfinder.Hub) this.hubs.get(0)).getPath();
            }
        } catch(Exception e){

        }
    }

    public void compute(int loops) {
        this.compute(loops, 4);
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
            return mc.theWorld.isBlockFullCube(block) || b instanceof BlockSlab || b instanceof BlockStairs || b instanceof BlockCactus || b instanceof BlockChest || b instanceof BlockEnderChest || b instanceof BlockSkull || b instanceof BlockPane || b instanceof BlockFence || b instanceof BlockWall || b instanceof BlockGlass || b instanceof BlockPistonBase || b instanceof BlockPistonExtension || b instanceof BlockPistonMoving || b instanceof BlockStainedGlass || b instanceof BlockTrapDoor;
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

    public CPathfinder.Hub isHubExisting(Vec3 loc) {
        Iterator var2 = this.hubs.iterator();

        CPathfinder.Hub hub;
        do {
            if (!var2.hasNext()) {
                var2 = this.hubsToWork.iterator();

                do {
                    if (!var2.hasNext()) {
                        return null;
                    }

                    hub = (CPathfinder.Hub)var2.next();
                } while(hub.getLoc().xCoord != loc.xCoord || hub.getLoc().yCoord != loc.yCoord || hub.getLoc().zCoord != loc.zCoord);

                return hub;
            }

            hub = (CPathfinder.Hub)var2.next();
        } while(hub.getLoc().xCoord != loc.xCoord || hub.getLoc().yCoord != loc.yCoord || hub.getLoc().zCoord != loc.zCoord);

        return hub;
    }

    public boolean addHub(CPathfinder.Hub parent, Vec3 loc, double cost) {
        CPathfinder.Hub existingHub = this.isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost = cost + parent.getTotalCost();
        }

        ArrayList path;
        if (existingHub == null) {
            if (loc.xCoord == this.endVec3.xCoord && loc.yCoord == this.endVec3.yCoord && loc.zCoord == this.endVec3.zCoord || this.minDistanceSquared != 0.0D && loc.squareDistanceTo(this.endVec3) <= this.minDistanceSquared) {
                this.path.clear();
                this.path = parent.getPath();
                this.path.add(loc);
                return true;
            }

            path = new ArrayList(parent.getPath());
            path.add(loc);
            this.hubsToWork.add(new CPathfinder.Hub(loc, parent, path, loc.squareDistanceTo(this.endVec3), cost, totalCost));
        } else if (existingHub.getCost() > cost) {
            path = new ArrayList(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(this.endVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }

        return false;
    }

    public class CompareHub implements Comparator<CPathfinder.Hub> {
        public CompareHub() {
        }

        public int compare(CPathfinder.Hub o1, CPathfinder.Hub o2) {
            return (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
        }
    }

    private class Hub {
        private Vec3 loc = null;
        private CPathfinder.Hub parent = null;
        private ArrayList<Vec3> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;

        public Hub(Vec3 loc, CPathfinder.Hub parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
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

        public CPathfinder.Hub getParent() {
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

        public void setParent(CPathfinder.Hub parent) {
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