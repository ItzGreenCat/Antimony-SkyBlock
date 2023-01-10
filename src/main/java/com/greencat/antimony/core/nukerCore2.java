package com.greencat.antimony.core;

import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class nukerCore2 {
    public BlockPos pos;
    public BlockPos lastPos;
    public boolean active = false;
    public boolean enable = false;
    public byte hitDelay = 0;
    public RotationType rotation = RotationType.SERVER_ROTATION;
    public MiningType miningType = MiningType.NORMAL;
    public boolean requestBlock = false;
    private float damageProgress;
    private String wrapperName;
    private int refreshTick = 0;
    List<BlockPos> ignoreList = new ArrayList<>();

    public nukerCore2(String wrapperName) {
            MinecraftForge.EVENT_BUS.register(this);
            CustomEventHandler.EVENT_BUS.register(this);
            this.wrapperName = wrapperName;
    }
    public void setActive(boolean isActive){
        damageProgress = 0;
        active = isActive;
    }
    public void putBlock(BlockPos pos){
        this.pos = pos;
        this.requestBlock = false;
    }
    public void init(){
        setActive(true);
        pos = null;
        requestBlock = false;
        lastPos = null;
    }
    public void post(){
        setActive(false);
        pos = null;
        requestBlock = false;
        lastPos = null;
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(!this.enable && nukerWrapper.enable){
            nukerWrapper.enable();
        }
        if (refreshTick + 1 > 10) {
            refreshTick = 0;
            ignoreList.clear();
        } else {
            refreshTick = refreshTick + 1;
        }
    }
    @SubscribeEvent
    public void BlockChangeEvent(CustomEventHandler.BlockChangeEvent event) {
        if(BlockPosEquals(pos,event.pos) && enable && miningType == MiningType.NORMAL){
            requestBlock = true;
            post();
            nukerWrapper.disable();
        }
    }
    @SubscribeEvent
    public void onMotionChange(CustomEventHandler.MotionChangeEvent.Pre event) {
        if(enable) {
            requestBlock = active && (pos == null || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.air);
            if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && active && pos != null) {
                if (Minecraft.getMinecraft().thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) > 5.0) {
                    pos = null;
                    requestBlock = true;
                    post();
                } else {
                    if (damageProgress > 100.0F) {
                        damageProgress = 0.0F;
                    }

                    if (pos != null && Minecraft.getMinecraft().theWorld != null) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
                        if (blockState.getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air) {
                            damageProgress = 0.0F;
                        }
                    }
                    if (pos != null) {
                        if (this.hitDelay > 0) {
                            --this.hitDelay;
                            return;
                        }
                        //new Utils().devLog("dmg progress:" + damageProgress + " last pos:" + lastPos + " pos:" + pos);
                        if (damageProgress == 0.0F && (lastPos == null || pos != lastPos)) {
                            this.lastPos = pos;
                            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                            if(miningType == MiningType.ONE_TICK){
                                if(ignoreList.size() + 1 > 10){
                                    ignoreList.clear();
                                }
                                ignoreList.add(pos);
                                requestBlock = true;
                                post();
                                nukerWrapper.disable();
                            }
                        }

                        Minecraft.getMinecraft().thePlayer.swingItem();
                        ++damageProgress;
                    }
                }
            }
            if (active && pos != null) {
                if (rotation == RotationType.SERVER_ROTATION) {
                    float[] angles = Utils.getRotation(pos, Utils.getClosestEnum(pos));
                    event.yaw = angles[0];
                    event.pitch = angles[1];
                }
                if (rotation == RotationType.ROTATION) {
                    float[] angles = Utils.getRotation(pos, Utils.getClosestEnum(pos));
                    Minecraft.getMinecraft().thePlayer.rotationYaw = angles[0];
                    Minecraft.getMinecraft().thePlayer.rotationPitch = angles[1];
                }
            }
        }
    }
    public BlockPos closestMineableBlock(Block block) {
        int r = 6;
        if (Minecraft.getMinecraft().thePlayer == null) return null;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                if (blockState.getBlock() == block) {
                    if(miningType == MiningType.NORMAL) {
                        chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    } else {
                        Vec3 vec3 = new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                        if(!isIgnored(new BlockPos(vec3.xCoord, vec3.yCoord, vec3.zCoord))){
                            chests.add(vec3);
                        }
                    }
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 chest : chests) {
            double dist = chest.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = chest;
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }
    public BlockPos closestMineableBlock(List<Block> block) {
        int r = 6;
        if (Minecraft.getMinecraft().thePlayer == null) return null;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                for(Block b : block) {
                    if (blockState.getBlock() == b) {
                        if(miningType == MiningType.NORMAL) {
                            chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        } else {
                            Vec3 vec3 = new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                            if(!isIgnored(new BlockPos(vec3.xCoord, vec3.yCoord, vec3.zCoord))){
                                chests.add(vec3);
                            }
                        }
                    }
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 chest : chests) {
            double dist = chest.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = chest;
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }
    public BlockPos closestMineableBlock(List<Block> block,Boolean titanium) {
        int r = 6;
        if (Minecraft.getMinecraft().thePlayer == null) return null;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                for(Block b : block) {
                    if (blockState.getBlock() == b) {
                        if(titanium && b == Blocks.stone) {
                            int meta = blockState.getValue(BlockStone.VARIANT).getMetadata();
                            if(meta == 4) {
                                if(miningType == MiningType.NORMAL) {
                                    chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                                } else {
                                    Vec3 vec3 = new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                                    if(!isIgnored(new BlockPos(vec3.xCoord, vec3.yCoord, vec3.zCoord))){
                                        chests.add(vec3);
                                    }
                                }
                            }
                        } else {
                            if(miningType == MiningType.NORMAL) {
                                chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            } else {
                                Vec3 vec3 = new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                                if(!isIgnored(new BlockPos(vec3.xCoord, vec3.yCoord, vec3.zCoord))){
                                    chests.add(vec3);
                                }
                            }
                        }
                    }
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 chest : chests) {
            double dist = chest.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = chest;
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }
    public boolean BlockPosEquals(BlockPos pos1,BlockPos pos2) {
        if(pos1 != null && pos2 != null) {
            return pos1.getX() == pos1.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
        }
        return false;
    }
    public BlockPos BlockPosMin(BlockPos pos1,BlockPos pos2){
        if(pos1 != null && pos2 != null) {
            double pos1Distance = pos1.distanceSq(Minecraft.getMinecraft().thePlayer.getPosition());
            double pos2Distance = pos2.distanceSq(Minecraft.getMinecraft().thePlayer.getPosition());
            double diff = pos1Distance - pos2Distance;
            if(diff < 0){
                return pos1;
            } else if(diff > 0){
                return pos2;
            } else {
                return pos1;
            }
        }
        return null;
    }
    public boolean isIgnored(BlockPos pos){
        boolean isIgnored = false;
        for(BlockPos ignored : ignoreList){
            if(BlockPosEquals(pos,ignored)){
                isIgnored = true;
                break;
            }
        }
        return isIgnored;
    }
    public enum RotationType{
        SERVER_ROTATION,ROTATION
    }
    public enum MiningType{
        NORMAL,ONE_TICK
    }
}
