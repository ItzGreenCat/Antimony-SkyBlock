package com.greencat.antimony.core.pathfinding;

import com.greencat.antimony.utils.Utils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class WalkPathfinder{
    private Vec3 startVec3;
    private Vec3 endVec3;
    private Node finalNode = null;
    private ArrayList<Vec3> path = new ArrayList<>();
    private HashSet<Node> edge = new HashSet<>();
    private HashSet<Node> all = new HashSet<>();
    public WalkPathfinder(Vec3 startVec3, Vec3 endVec3) {
        this.startVec3 = Utils.floorVec(startVec3);
        this.endVec3 = Utils.floorVec(endVec3);
    }
    public WalkPathfinder(Vec3 startVec3, Vec3 endVec3,double unused) {
        this(startVec3,endVec3);
    }


    public void compute() {
        this.compute(2000);
    }

    public void compute(int loops, int depth) {
        this.compute(loops);
    }

    public void compute(int loop) {
        path.clear();
        edge.clear();
        all.clear();
        finalNode = null;
        edge.add(new Node(startVec3,null,0,startVec3.squareDistanceTo(endVec3)));
        all.addAll(edge);
        for(int i = 0;i < loop;++i){
            if(finalNode != null){
                break;
            }
            double smallest = 1145141919.0D;
            Node smallNode = null;
            for(Node node : edge){
                double weight = node.currentDistanceToStart + node.currentDistanceToEnd;
                if(weight < smallest){
                    smallest = weight;
                    smallNode = node;
                }
            }
            if (smallNode != null) {
                edge.remove(smallNode);
                Vector<Node> Nodes = new Vector<>();
                Vector<Node> Nodes2 = new Vector<>();
                Nodes.add(new Node(smallNode.currentLocation.addVector(1.0D,0.0D,0.0D),smallNode,smallNode.currentDistanceToStart + 1,smallNode.currentLocation.addVector(1.0D,0.0D,0.0D).squareDistanceTo(endVec3)));
                Nodes.add(new Node(smallNode.currentLocation.addVector(0.0D,0.0D,1.0D),smallNode,smallNode.currentDistanceToStart + 1,smallNode.currentLocation.addVector(1.0D,0.0D,0.0D).squareDistanceTo(endVec3)));
                Nodes.add(new Node(smallNode.currentLocation.addVector(0.0D,0.0D,-1.0D),smallNode,smallNode.currentDistanceToStart + 1,smallNode.currentLocation.addVector(1.0D,0.0D,0.0D).squareDistanceTo(endVec3)));
                Nodes.add(new Node(smallNode.currentLocation.addVector(-1.0D,0.0D,0.0D),smallNode,smallNode.currentDistanceToStart + 1,smallNode.currentLocation.addVector(1.0D,0.0D,0.0D).squareDistanceTo(endVec3)));
                Nodes2.add(new Node(smallNode.currentLocation.addVector(1.0D,0.0D,1.0D),smallNode,smallNode.currentDistanceToStart + 1.4,smallNode.currentLocation.addVector(1.0D,0.0D,0.0D).squareDistanceTo(endVec3)));
                Nodes2.add(new Node(smallNode.currentLocation.addVector(1.0D,0.0D,-1.0D),smallNode,smallNode.currentDistanceToStart + 1.4,smallNode.currentLocation.addVector(1.0D,0.0D,0.0D).squareDistanceTo(endVec3)));
                Nodes2.add(new Node(smallNode.currentLocation.addVector(-1.0D,0.0D,1.0D),smallNode,smallNode.currentDistanceToStart + 1.4,smallNode.currentLocation.addVector(1.0D,0.0D,0.0D).squareDistanceTo(endVec3)));
                Nodes2.add(new Node(smallNode.currentLocation.addVector(-1.0D,0.0D,-1.0D),smallNode,smallNode.currentDistanceToStart + 1.4,smallNode.currentLocation.addVector(1.0D,0.0D,0.0D).squareDistanceTo(endVec3)));
                for(Node node : Nodes){
                    if(!isInAll(node)){
                        if (new BlockPos(node.currentLocation).equals(new BlockPos(endVec3))) {
                            finalNode = node;
                            break;
                        }
                        if(!(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation)).getBlock() instanceof BlockLiquid)
                        && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation)).getBlock() != Blocks.air
                        && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation).up()).getBlock() == Blocks.air
                        && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation).up(2)).getBlock() == Blocks.air){
                            edge.add(node);
                            all.add(node);
                        } else if (!(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation).up()).getBlock() instanceof BlockLiquid)
                                && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation).up()).getBlock() != Blocks.air
                                && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation).up(2)).getBlock() == Blocks.air
                                && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation).up(3)).getBlock() == Blocks.air
                        && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(smallNode.currentLocation).up()).getBlock() == Blocks.air
                                && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(smallNode.currentLocation).up(2)).getBlock() == Blocks.air
                                && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(smallNode.currentLocation).up(3)).getBlock() == Blocks.air){
                            Node newNode = new Node(node.currentLocation.addVector(0.0D,1.0D,0.0D),smallNode,smallNode.currentDistanceToStart + 1.4,node.currentLocation.addVector(0.0D,1.0D,0.0D).squareDistanceTo(endVec3));
                            edge.add(newNode);
                            all.add(newNode);
                        }
                    }
                }
                for(Node node : Nodes2){
                    if(!isInAll(node)){
                        if (new BlockPos(node.currentLocation).equals(new BlockPos(endVec3))) {
                            finalNode = node;
                            break;
                        }
                        if(!(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation)).getBlock() instanceof BlockLiquid)
                                && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation)).getBlock() != Blocks.air
                                && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation).up()).getBlock() == Blocks.air
                                && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(node.currentLocation).up(2)).getBlock() == Blocks.air){
                            edge.add(node);
                            all.add(node);
                        }
                    }
                }
            }
        }
        calcPath();
    }

    public List<Vec3> getPath() {
        return this.path;
    }

    public void calcPath(){
        if(finalNode != null){
            ArrayList<Vec3> path = new ArrayList<>();
            Node currentNode = finalNode;
            if(currentNode.parent == null) {
                path.add(currentNode.currentLocation);
                this.path = path;
                return;
            }
            do {
                path.add(finalNode.currentLocation);
                currentNode = currentNode.parent;
            } while (currentNode.parent != null);
            this.path = path;
        }
    }
    public boolean isInAll(Node node){
        return all.contains(node);
    }
    class Node{
        public Vec3 currentLocation;
        public Node parent;
        public double currentDistanceToStart;
        public double currentDistanceToEnd;
        public Node(Vec3 currentLocation,Node parent,double distToStart,double distToEnd){
            this.currentLocation = currentLocation;
            this.parent = parent;
            this.currentDistanceToStart = distToStart;
            this.currentDistanceToEnd = distToEnd;
        }
        public boolean equals(Object o){
            if(o instanceof Node){
                Node node = (Node)o;
                if(currentLocation.xCoord == node.currentLocation.xCoord &&
                        currentLocation.yCoord == node.currentLocation.yCoord &&
                        currentLocation.zCoord == node.currentLocation.zCoord){
                    if(parent == null && node.parent == null){
                        return true;
                    } else return parent != null && node.parent != null && parent.equals(node.parent);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        int hashcode(){
            int h = 0;
            h = h + 31 + currentLocation.hashCode();
            h = h + 31 + parent.hashCode();
            return h;
        }
    }
}
