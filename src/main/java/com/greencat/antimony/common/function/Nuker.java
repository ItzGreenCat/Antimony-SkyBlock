package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.nukerCore2;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
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

public class Nuker {
    nukerCore2 nuker = new nukerCore2();
    BlockPos pos;
    public Nuker(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("Nuker")){
            nuker.post();
            nuker.enable = false;
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("Nuker")) {
            if (!event.status) {
                nuker.post();
                nuker.enable = false;
            } else {
                nuker.init();
                nuker.enable = true;
            }
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals("Nuker")){
            nuker.init();
            nuker.enable = true;
        }
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("Nuker")) {
            if((Integer)getConfigByFunctionName.get("Nuker","rotation") == 0){
                nuker.rotation = nukerCore2.RotationType.SERVER_ROTATION;
            } else if((Integer)getConfigByFunctionName.get("Nuker","rotation") == 1){
                nuker.rotation = nukerCore2.RotationType.ROTATION;
            }
            pos = getBlock();
            if(nuker.requestBlock){
                nuker.putBlock(pos);
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
        BlockPos pos = null;
        int nukerType = (Integer)getConfigByFunctionName.get("Nuker","type");
        if(nukerType == 0){
            pos = nuker.closestMineableBlock(Blocks.stained_glass);
        }
        if(nukerType == 1){
            BlockPos pos1 = nuker.closestMineableBlock(Blocks.stained_glass);
            BlockPos pos2 = nuker.closestMineableBlock(Blocks.stained_glass_pane);
            pos = nuker.BlockPosMin(pos1,pos2);
        }
        if(nukerType == 2){
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
        if(nukerType == 3){
            pos = nuker.closestMineableBlock(Blocks.stone);
        }
        if(nukerType == 4){
            pos = nuker.closestMineableBlock(Blocks.netherrack);
        }
        if(nukerType == 5){
            pos = nuker.closestMineableBlock(Blocks.sand);
        }
        if(nukerType == 6){
            pos = nuker.closestMineableBlock(Blocks.gold_block);
        }
        if(nukerType == 7){
            List<Block> oreList = new ArrayList<Block>();
            oreList.add(Blocks.prismarine);
            oreList.add(Blocks.wool);
            pos = nuker.closestMineableBlock(oreList);
        }
        if(nukerType == 8){
            if (Minecraft.getMinecraft().theWorld != null) {
                double x = Minecraft.getMinecraft().thePlayer.posX;
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ;
                List<EntityArmorStand> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - (200 / 2d), y - (256 / 2d), z - (200 / 2d), x + (200 / 2d), y + (256 / 2d), z + (200 / 2d)), null);
                for(EntityArmorStand entity : entityList) {
                    if (entity != null) {
                        if(entity.getEquipmentInSlot(4) != null && Minecraft.getMinecraft().theWorld.getBlockState(entity.getPosition().up()).getBlock() == Blocks.ice && isInCave()){
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
        return pos;
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
