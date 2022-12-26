package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class FrozenTreasureESP {
    public FrozenTreasureESP(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event){
        if (FunctionManager.getStatus("FrozenTreasureESP")) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    double x = Minecraft.getMinecraft().thePlayer.posX;
                    double y = Minecraft.getMinecraft().thePlayer.posY;
                    double z = Minecraft.getMinecraft().thePlayer.posZ;
                    List<EntityArmorStand> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - (200 / 2d), y - (256 / 2d), z - (200 / 2d), x + (200 / 2d), y + (256 / 2d), z + (200 / 2d)), null);
                    for(EntityArmorStand entity : entityList) {
                        if (entity != null) {
                            if(entity.getEquipmentInSlot(4) != null && (Minecraft.getMinecraft().theWorld.getBlockState(entity.getPosition().up()).getBlock() == Blocks.ice || Minecraft.getMinecraft().theWorld.getBlockState(entity.getPosition().up()).getBlock() == Blocks.packed_ice) && isInCave()){
                                if(entity.getEquipmentInSlot(4).getDisplayName() != null && entity.getEquipmentInSlot(4).hasDisplayName()) {
                                    String itemName = entity.getEquipmentInSlot(4).getDisplayName();
                                    if(itemName.contains("Ice Bait") || itemName.contains("Enchanted Ice") || itemName.contains("Glacial Fragment") || (itemName.contains("Packed Ice") && !itemName.contains("Enchanted"))) {
                                        Utils.OutlinedBoxWithESP(entity.getPosition().up(),Color.GRAY, false, 3);
                                    }
                                    if(itemName.contains("White Gift")){
                                        Utils.OutlinedBoxWithESP(entity.getPosition().up(),Color.WHITE, false, 3);
                                    }
                                    if(itemName.contains("Green Gift")){
                                        Utils.OutlinedBoxWithESP(entity.getPosition().up(),Color.GREEN, false, 3);
                                    }
                                    if(itemName.contains("Glacial Talisman")){
                                        Utils.OutlinedBoxWithESP(entity.getPosition().up(),Color.BLUE, false, 3);
                                    }
                                    if(itemName.contains("Red Gift")){
                                        Utils.OutlinedBoxWithESP(entity.getPosition().up(),Color.RED, false, 3);
                                        Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPositionVector(),new Vec3(entity.getPosition().up()).addVector(0.5,0.5,0.5),Color.RED,2.5F);
                                    }
                                    if(itemName.contains("Enchanted Packed Ice")){
                                        Utils.OutlinedBoxWithESP(entity.getPosition().up(),Color.CYAN, false, 3);
                                        Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPositionVector(),new Vec3(entity.getPosition().up()).addVector(0.5,0.5,0.5),Color.CYAN,2.5F);
                                    }
                                    Utils.renderText(itemName, entity.getPosition().up(), event.partialTicks);
                                }
                            }
                        }
                    }
                }
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
}
