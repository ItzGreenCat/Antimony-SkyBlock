package com.greencat.antimony.common.function;

import com.greencat.Antimony;
import com.greencat.antimony.common.mixins.MinecraftAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.nukerCore;
import com.greencat.antimony.core.type.Rotation;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForagingBot {
    static EnumStage stage;
    static nukerCore nuker = new nukerCore();
    static int tick = 0;

    List<BlockPos> dirtList = new ArrayList<BlockPos>();
    public static long latest;

    public ForagingBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event) {
        if (event.function.getName().equals("ForagingBot")) {
            if (!init()) {
                event.setCanceled(true);
                Utils.print("无法找到附近泥土");
            }
        }
    }

    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {
        if (event.function.getName().equals("ForagingBot")) {
            if (event.status) {
                if (!init()) {
                    event.setCanceled(true);
                    Utils.print("无法找到附近泥土");
                }
            } else {
                post();
            }
        }
    }

    @SubscribeEvent
    public void onDisabled(CustomEventHandler.FunctionDisabledEvent event) {
        if (event.function.getName().equals("ForagingBot")) {
            post();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("ForagingBot")) {
                if (event.phase == TickEvent.Phase.START) {
                    if(stage != null){
                        if (stage == EnumStage.EMPTY) {
                            if(!Antimony.NoSaplingBound){
                                Antimony.NoSaplingBound = true;
                                Antimony.NoTreeBound = true;
                            }
                            if(!Antimony.NoTreeBound){
                                Antimony.NoTreeBound = true;
                            }
                            checkSwitch("sapling");
                            List<BlockPos> posList = reList(dirtList);
                                BlockPos pos = null;
                                for(BlockPos currentPos : posList){
                                    if(Minecraft.getMinecraft().theWorld.getBlockState(currentPos.up()).getBlock() == Blocks.air){
                                        pos = currentPos;
                                        break;
                                    }
                                }
                                if(pos != null) {
                                    Rotation rotation = Utils.getRotation(pos);
                                    Minecraft.getMinecraft().thePlayer.rotationYaw = rotation.getYaw();
                                    Minecraft.getMinecraft().thePlayer.rotationPitch = rotation.getPitch();
                                    //KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), true);
                                    //EnumFacing facing = Utils.calculateEnumfacingLook(new Vec3(pos));
                                    if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
                                        if(Minecraft.getMinecraft().theWorld.getBlockState(pos.up()).getBlock() == Blocks.air) {
                                            C08PacketPlayerBlockPlacement c08 = new C08PacketPlayerBlockPlacement(pos, EnumFacing.UP.getIndex(), Minecraft.getMinecraft().thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F);
                                            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(c08);
                                        }
                                    }
                                    //Minecraft.getMinecraft().playerController.onPlayerRightClick(Minecraft.getMinecraft().thePlayer,Minecraft.getMinecraft().theWorld,Minecraft.getMinecraft().thePlayer.getHeldItem(),pos.up(),EnumFacing.fromAngle(Minecraft.getMinecraft().thePlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.0D));
                                }
                        } else {
                            if(Antimony.NoSaplingBound){
                                Antimony.NoSaplingBound = false;
                                Antimony.NoTreeBound = false;
                            }
                            if(Antimony.NoTreeBound){
                                Antimony.NoTreeBound = false;
                            }
                        }
                    }
                    if (tick > 2) {
                        if (stage != null) {
                            if (stage == EnumStage.SAPLING) {
                                if(!Antimony.NoTreeBound){
                                    Antimony.NoTreeBound = true;
                                }
                                checkSwitch("bone meal");
                                List<BlockPos> posList = new ArrayList<BlockPos>();
                                if (Minecraft.getMinecraft().theWorld != null) {
                                    int StartY;
                                    int StartX;
                                    int StartZ;
                                    EntityPlayer player;
                                    int EndX;
                                    int EndY;
                                    int EndZ;
                                    int NowX;
                                    int NowY;
                                    int NowZ;
                                    player = Minecraft.getMinecraft().thePlayer;
                                    int bound;
                                    bound = 4;
                                    StartY = (int) (player.posY - 4);
                                    StartX = (int) (player.posX - bound);
                                    StartZ = (int) (player.posZ - bound);
                                    EndX = (int) (player.posX + bound);
                                    EndY = (int) (player.posY + 4);
                                    EndZ = (int) (player.posZ + bound);

                                    NowX = StartX;
                                    NowY = StartY;
                                    NowZ = StartZ;
                                    while (NowY != EndY) {
                                        if (NowX == EndX) {
                                            if (NowZ == EndZ) {

                                                NowZ = StartZ;
                                                NowX = StartX;
                                                NowY = NowY + 1;
                                            } else {
                                                NowX = StartX;
                                                NowZ = NowZ + 1;
                                            }
                                        } else {
                                            NowX = NowX + 1;
                                        }
                                        BlockPos pos = new BlockPos(NowX, NowY, NowZ);
                                        if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockSapling) {
                                            posList.add(pos);
                                        }
                                    }
                                    NowX = StartX;
                                    NowY = StartY;
                                    NowZ = StartZ;
                                }
                                if (!posList.isEmpty()) {
                                    BlockPos pos = reListNoBack(posList).get(0);
                                    Rotation rotation = Utils.getRotation(pos);
                                    Minecraft.getMinecraft().thePlayer.rotationYaw = rotation.getYaw();
                                    Minecraft.getMinecraft().thePlayer.rotationPitch = rotation.getPitch();
                                    if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
                                        ((MinecraftAccessor)Minecraft.getMinecraft()).rightClickMouse();
                                    }
                                }
                            } else {
                                if(Antimony.NoTreeBound){
                                    Antimony.NoTreeBound = false;
                                }
                            }
                            if (stage == EnumStage.LOG) {
                                checkSwitch("jungle axe");
                                checkSwitch("treecap");
                                BlockPos target = null;
                                if (Minecraft.getMinecraft().theWorld != null) {
                                    int StartY;
                                    int StartX;
                                    int StartZ;
                                    EntityPlayer player;
                                    int EndX;
                                    int EndY;
                                    int EndZ;
                                    int NowX;
                                    int NowY;
                                    int NowZ;
                                    player = Minecraft.getMinecraft().thePlayer;
                                    int bound;
                                    bound = 4;
                                    StartY = (int) (player.posY - 4);
                                    StartX = (int) (player.posX - bound);
                                    StartZ = (int) (player.posZ - bound);
                                    EndX = (int) (player.posX + bound);
                                    EndY = (int) (player.posY + 4);
                                    EndZ = (int) (player.posZ + bound);
                                    NowX = StartX;
                                    NowY = StartY;
                                    NowZ = StartZ;
                                    while (NowY != EndY) {
                                        if (NowX == EndX) {
                                            if (NowZ == EndZ) {

                                                NowZ = StartZ;
                                                NowX = StartX;
                                                NowY = NowY + 1;
                                            } else {
                                                NowX = StartX;
                                                NowZ = NowZ + 1;
                                            }
                                        } else {
                                            NowX = NowX + 1;
                                        }
                                        BlockPos pos = new BlockPos(NowX, NowY, NowZ);
                                        if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockLog) {
                                            target = pos;
                                            break;
                                        }
                                    }
                                    NowX = StartX;
                                    NowY = StartY;
                                    NowZ = StartZ;
                                }
                                if (target != null) {
                                    nuker.nuke(new Vec3(target));
                                }
                            }
                        }
                        tick = 0;
                    } else {
                        tick = tick + 1;
                    }
                }
                if (event.phase == TickEvent.Phase.END) {
                        if (stage != null) {
                            if (stage == EnumStage.EMPTY) {
                                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
                                boolean switchStage = true;
                                for (BlockPos pos : dirtList) {
                                    if (!(Minecraft.getMinecraft().theWorld.getBlockState(pos.up()).getBlock() instanceof BlockSapling)) {
                                        switchStage = false;
                                    }
                                }
                                if (switchStage) {
                                    stage = EnumStage.SAPLING;
                                }
                            }
                            if (stage == EnumStage.SAPLING) {
                                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
                                boolean switchStage = true;
                                for (BlockPos pos : dirtList) {
                                    if (!(Minecraft.getMinecraft().theWorld.getBlockState(pos.up()).getBlock() instanceof BlockLog)) {
                                        switchStage = false;
                                    }
                                }
                                if (switchStage) {
                                    stage = EnumStage.LOG;
                                }
                            }
                            if (stage == EnumStage.LOG) {
                                boolean switchStage = true;
                                for (BlockPos pos : dirtList) {
                                    if (!(Minecraft.getMinecraft().theWorld.getBlockState(pos.up()).getBlock() == Blocks.air)) {
                                        switchStage = false;
                                    }
                                }
                                if (switchStage) {
                                    stage = EnumStage.EMPTY;
                                }
                            }
                        }
                }
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if(FunctionManager.getStatus("ForagingBot")){
            for(BlockPos pos : dirtList){
                Utils.OutlinedBoxWithESP(pos, Chroma.color,false,2);
                Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPosition(),pos,Chroma.color,2);
            }
        }
    }

    public boolean init() {
        List<BlockPos> target = new ArrayList<BlockPos>();
        if (Minecraft.getMinecraft().theWorld != null) {
            int StartY;
            int StartX;
            int StartZ;
            EntityPlayer player;
            int EndX;
            int EndY;
            int EndZ;
            int NowX;
            int NowY;
            int NowZ;
            player = Minecraft.getMinecraft().thePlayer;
            int bound;
            bound = 4;
            StartY = (int) (player.posY - 2);
            StartX = (int) (player.posX - bound);
            StartZ = (int) (player.posZ - bound);
            EndX = (int) (player.posX + bound);
            EndY = (int) (player.posY + 1);
            EndZ = (int) (player.posZ + bound);
            NowX = StartX;
            NowY = StartY;
            NowZ = StartZ;
            while (NowY != EndY) {
                if (NowX == EndX) {
                    if (NowZ == EndZ) {

                        NowZ = StartZ;
                        NowX = StartX;
                        NowY = NowY + 1;
                    } else {
                        NowX = StartX;
                        NowZ = NowZ + 1;
                    }
                } else {
                    NowX = NowX + 1;
                }
                BlockPos pos = new BlockPos(NowX, NowY, NowZ);
                if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.dirt) {
                    target.add(pos);
                }
            }
            NowX = StartX;
            NowY = StartY;
            NowZ = StartZ;
        }
        if (!target.isEmpty()) {
            dirtList = target;

            boolean switchStageEmpty = true;
            for (BlockPos pos : dirtList) {
                if (!(Minecraft.getMinecraft().theWorld.getBlockState(pos.up()).getBlock() instanceof BlockSapling)) {
                    switchStageEmpty = false;
                }
            }
            if (switchStageEmpty) {
                stage = EnumStage.SAPLING;
            }

            boolean switchStageSapling = true;
            for (BlockPos pos : dirtList) {
                if (!(Minecraft.getMinecraft().theWorld.getBlockState(pos.up()).getBlock() instanceof BlockLog)) {
                    switchStageSapling = false;
                }
            }
            if (switchStageSapling) {
                stage = EnumStage.LOG;
            }

            boolean switchLog = true;
            for (BlockPos pos : dirtList) {
                if (!(Minecraft.getMinecraft().theWorld.getBlockState(pos.up()).getBlock() == Blocks.air)) {
                    switchLog = false;
                }
            }
            if (switchLog) {
                stage = EnumStage.EMPTY;
            }

            return true;
        }
        return false;
    }

    public void post() {
        nuker.pos = null;
        tick = 0;
        Antimony.NoSaplingBound = false;
        Antimony.NoTreeBound = false;
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
    }

    public List<BlockPos> reList(List<BlockPos> posList) {
        List<BlockPos> originalList = posList;
        BlockPos[] originalArray = originalList.toArray(new BlockPos[0]);
        for (int i = 0; i < originalArray.length; i++) {
            BlockPos insertValue = originalArray[i];
            int insertIndex = i - 1;
            while (insertIndex >= 0 && new Vec3(insertValue).distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) < new Vec3(originalArray[insertIndex]).distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector())) {
                originalArray[insertIndex + 1] = originalArray[insertIndex];
                insertIndex--;
            }
            originalArray[insertIndex + 1] = insertValue;
        }
        List<BlockPos> newList = new ArrayList<>();
        for (int i = originalArray.length - 1; i >= 0; i--) {
            newList.add(originalArray[i]);
        }
        return newList;
    }

    public List<BlockPos> reListNoBack(List<BlockPos> posList) {
        List<BlockPos> originalList = posList;
        BlockPos[] originalArray = originalList.toArray(new BlockPos[0]);
        for (int i = 0; i < originalArray.length; i++) {
            BlockPos insertValue = originalArray[i];
            int insertIndex = i - 1;
            while (insertIndex >= 0 && new Vec3(insertValue).distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) < new Vec3(originalArray[insertIndex]).distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector())) {
                originalArray[insertIndex + 1] = originalArray[insertIndex];
                insertIndex--;
            }
            originalArray[insertIndex + 1] = insertValue;
        }
        return new ArrayList<>(Arrays.asList(originalArray));
    }

    public void checkSwitch(String name) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    enum EnumStage {
        EMPTY, SAPLING, LOG
    }
}
