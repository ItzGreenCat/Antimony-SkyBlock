package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.core.notice.Notice;
import com.greencat.antimony.core.notice.NoticeManager;
import com.greencat.antimony.core.nukerCore;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


import java.util.HashSet;
import java.util.Objects;

public class CropBot extends FunctionStatusTrigger {
    nukerCore nuker = new nukerCore();
    /*int StartY;
    int StartX;
    int StartZ;*/
    EntityPlayer player;
    /*int EndX;
    int EndY;
    int EndZ;
    int NowX;
    int NowY;
    int NowZ;
    int nearlyStartY;
    int nearlyStartX;
    int nearlyStartZ;
    int nearlyEndX;
    int nearlyEndY;
    int nearlyEndZ;
    int nearlyNowX;
    int nearlyNowY;
    int nearlyNowZ;*/
    static int nearlyCooldownTick = 0;
    static BlockPos target;
    static BlockPos nearlyTarget;
    static int finderCount = 0;
    static boolean searching = false;
    static long lastStart = 0L;
    Minecraft mc = Minecraft.getMinecraft();
    HashSet<BlockPos> ignoreList = new HashSet<BlockPos>();

    public CropBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @Override
    public String getName() {
        return "CropBot";
    }

    @Override
    public void post() {
        target = null;
        nearlyCooldownTick = 0;
        nearlyTarget = null;
        finderCount = 0;
        nuker.pos = null;
        ignoreList.clear();
        FunctionManager.setStatus("Pathfinding", false);
    }

    @Override
    public void init() {
        ignoreList.clear();
        searching = false;
        lastStart = System.currentTimeMillis();
        new Thread(() -> {
            if(searching){
                return;
            }
            Utils.print("搜寻中");
            Notice notice0 = new Notice("CropBot",true,"Searching...");
            NoticeManager.add(notice0);
            searching = true;
            if (getTarget(true) == null) {
                FunctionManager.setStatus("CropBot",false);
                Utils.print("无法找到对应作物");
                Notice notice = new Notice("CropBot-Enable",true,"Cannot find Crops");
                NoticeManager.add(notice);
            }
            searching = false;
        }).start();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(FunctionManager.getStatus("CropBot")) {
            if(System.currentTimeMillis() - lastStart < 2000){
                return;
            }
            if(searching){
                return;
            }
            if (target == null) {
                target = getNearlyTarget();
                if(target == null) {
                        target = getTarget(false);
                        if (target != null) {
                            Pathfinder.setup(new BlockPos(Utils.floorVec(mc.thePlayer.getPositionVector().addVector(0.0D,0.2D,0.0D))), target, 0.0D);
                            if (Pathfinder.hasPath()) {
                                FunctionManager.setStatus("Pathfinding", true);
                            } else {
                                target = null;
                            }
                        }
                }
            }
            if(target == null){
                new Thread(() -> {
                    if(searching){
                        return;
                    }
                    if(FunctionManager.getStatus("Pathfinding")){
                        return;
                    }
                    searching = true;
                    Utils.print("无法找到对应作物,启用大范围搜索");
                    Notice notice = new Notice("CropBot",true,"Cannot find Crops","Enable extensive search");
                    NoticeManager.add(notice);
                    target = getTarget(true);
                    if (target != null) {
                        Pathfinder.setup(new BlockPos(Utils.floorVec(mc.thePlayer.getPositionVector().addVector(0.0D,0.2D,0.0D))), target, 0.0D);
                        if (Pathfinder.hasPath()) {
                            FunctionManager.setStatus("Pathfinding", true);
                        } else {
                            target = null;
                        }
                    }
                    searching = false;
                    if(target == null && !FunctionManager.getStatus("Pathfinding")){
                        FunctionManager.setStatus("CropBot", false);
                        Utils.print("无法找到对应作物");
                        Notice notice2 = new Notice("CropBot",true,"Cannot find Crops");
                        NoticeManager.add(notice2);
                    }
                }).start();
            }
            if(searching){
                return;
            }
            if (!FunctionManager.getStatus("Pathfinding")) {
                if (finderCount > 10) {
                    target = null;
                    finderCount = 0;
                } else {
                    finderCount = finderCount + 1;
                }
            }
            if(nearlyTarget == null || mc.theWorld.getBlockState(nearlyTarget).getBlock() == Blocks.air) {
                if (nearlyCooldownTick > 1) {
                    nearlyTarget = getNearlyTarget();
                    nearlyCooldownTick = 0;
                } else {
                    nearlyCooldownTick = nearlyCooldownTick + 1;
                }
            }
            try {
                nuker.nuke(new Vec3(Objects.requireNonNull(nearlyTarget)),this,this.getClass().getDeclaredMethod("getNearlyTarget"));
                if(nearlyTarget != null) {
                    if (ignoreList.size() + 1 > (Integer) ConfigInterface.get("CropBot","listSize")) {
                        ignoreList.clear();
                    }
                    ignoreList.add(nearlyTarget);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private BlockPos getTarget(boolean large) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Utils.print("" + ignoreList.size());
            this.player = Minecraft.getMinecraft().thePlayer;
            int bound;
            if(!large) {
                bound = (Integer) ConfigInterface.get("CropBot", "radius");
            } else {
                bound = 40;
            }
            /*this.StartY = (int) (this.player.posY - 5);
            this.StartX = (int) (this.player.posX - bound);
            this.StartZ = (int) (this.player.posZ - bound);
            this.EndX = (int) (this.player.posX + bound);
            this.EndY = (int) (this.player.posY + 5);
            this.EndZ = (int) (this.player.posZ + bound);

            this.NowX = this.StartX;
            this.NowY = this.StartY;
            this.NowZ = this.StartZ;
            while (NowY != EndY) {
                if (this.NowX == this.EndX) {
                    if (this.NowZ == this.EndZ) {

                        this.NowZ = this.StartZ;
                        this.NowX = this.StartX;
                        NowY = NowY + 1;
                    } else {
                        this.NowX = this.StartX;
                        NowZ = NowZ + 1;
                    }
                } else {
                    NowX = NowX + 1;
                }
                BlockPos pos = new BlockPos(NowX, NowY, NowZ);
                if (isValid(pos)) {
                    return pos;
                }
            }
            this.NowX = this.StartX;
            this.NowY = this.StartY;
            this.NowZ = this.StartZ;*/
            BlockPos temp = null;
            for(BlockPos pos : BlockPos.getAllInBox(Minecraft.getMinecraft().thePlayer.getPosition().add(bound,bound,bound),Minecraft.getMinecraft().thePlayer.getPosition().add(-bound,-bound,-bound))){
                if(isValid(pos)){
                    if(temp == null) {
                        temp = pos;
                    }
                } else {
                    if(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockCrops || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.melon_block || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.pumpkin || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockMushroom || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockNetherWart){
                        if (ignoreList.size() + 1 > (Integer) ConfigInterface.get("CropBot","listSize")) {
                            ignoreList.clear();
                        }
                        ignoreList.add(pos);
                    }
                }
            }
            return temp;
        }
        return null;
    }
    private BlockPos getNearlyTarget() {
        if (Minecraft.getMinecraft().theWorld != null) {
            this.player = Minecraft.getMinecraft().thePlayer;
            /*this.nearlyStartY = (int) (this.player.posY - 3.5D);
            this.nearlyStartX = (int) (this.player.posX - 3.5D);
            this.nearlyStartZ = (int) (this.player.posZ - 3.5D);
            this.nearlyEndX = (int) (this.player.posX + 3.5D);
            this.nearlyEndY = (int) (this.player.posY + 3.5D);
            this.nearlyEndZ = (int) (this.player.posZ + 3.5D);

            this.nearlyNowX = this.nearlyStartX;
            this.nearlyNowY = this.nearlyStartY;
            this.nearlyNowZ = this.nearlyStartZ;
            while (nearlyNowY != nearlyEndY) {
                if (this.nearlyNowX == this.nearlyEndX) {
                    if (this.nearlyNowZ == this.nearlyEndZ) {

                        this.nearlyNowZ = this.nearlyStartZ;
                        this.nearlyNowX = this.nearlyStartX;
                        nearlyNowY = nearlyNowY + 1;
                    } else {
                        this.nearlyNowX = this.nearlyStartX;
                        nearlyNowZ = nearlyNowZ + 1;
                    }
                } else {
                    nearlyNowX = nearlyNowX + 1;
                }
                BlockPos pos = new BlockPos(nearlyNowX, nearlyNowY, nearlyNowZ);
                if (isValid(pos)) {
                    return pos;
                }
            }
            this.nearlyNowX = this.nearlyStartX;
            this.nearlyNowY = this.nearlyStartY;
            this.nearlyNowZ = this.nearlyStartZ;*/
            double bound = 3.5D;
            BlockPos temp = null;
            for(BlockPos pos : BlockPos.getAllInBox(Minecraft.getMinecraft().thePlayer.getPosition().add(bound,bound,bound),Minecraft.getMinecraft().thePlayer.getPosition().add(-bound,-bound,-bound))){
                if(isValid(pos)){
                    if(temp == null) {
                        temp = pos;
                    }
                } else {
                    if(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockCrops || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.melon_block || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.pumpkin || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockMushroom || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockNetherWart){
                        if (ignoreList.size() + 1 > (Integer) ConfigInterface.get("CropBot","listSize")) {
                            ignoreList.clear();
                        }
                        ignoreList.add(pos);
                    }
                };
            }
            return temp;
        }
        return null;
    }
    private Boolean isValid(BlockPos block) {
        if(!isIgnored(block)) {
            if (Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() == Blocks.potatoes && (Integer) ConfigInterface.get("CropBot", "crop") == 0) {
                return Minecraft.getMinecraft().theWorld.getBlockState(block).getValue(BlockCrops.AGE) == 7;
            }
            if (Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() == Blocks.carrots && (Integer) ConfigInterface.get("CropBot", "crop") == 1) {
                return Minecraft.getMinecraft().theWorld.getBlockState(block).getValue(BlockCrops.AGE) == 7;
            }
            if ((Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() == Blocks.brown_mushroom || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() == Blocks.red_mushroom) && (Integer) ConfigInterface.get("CropBot", "crop") == 2) {
                return true;
            }
            if (Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() == Blocks.nether_wart && (Integer) ConfigInterface.get("CropBot", "crop") == 3) {
                return Minecraft.getMinecraft().theWorld.getBlockState(block).getValue(BlockNetherWart.AGE) == 3;
            }
            if (Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() == Blocks.wheat && (Integer) ConfigInterface.get("CropBot", "crop") == 4) {
                return Minecraft.getMinecraft().theWorld.getBlockState(block).getValue(BlockCrops.AGE) == 7;
            }
            if ((Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockFlower || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockGrass || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockTallGrass)&& (Integer) ConfigInterface.get("CropBot", "crop") == 5) {
                return true;
            }
            if (Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() == Blocks.melon_block && (Integer) ConfigInterface.get("CropBot", "crop") == 6) {
                return true;
            }
            if (Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() == Blocks.pumpkin && (Integer) ConfigInterface.get("CropBot", "crop") == 7) {
                return true;
            }
        }
        return false;
    }
    private boolean isIgnored(BlockPos pos){
        boolean isIgnored = false;
        for(BlockPos ignored : ignoreList){
            if(pos.getX() == ignored.getX() && pos.getZ() == ignored.getZ() && pos.getY() == ignored.getY()){
                isIgnored = true;
                break;
            }
        }
        return isIgnored;
    }
    @SubscribeEvent
    public void Render(RenderWorldLastEvent event) {
        if(FunctionManager.getStatus("CropBot")) {
            if(nearlyTarget != null){
                Utils.BoxWithESP(nearlyTarget, Chroma.color,false);
                Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPosition(),nearlyTarget,Chroma.color,2);
            }
        }
    }
}
