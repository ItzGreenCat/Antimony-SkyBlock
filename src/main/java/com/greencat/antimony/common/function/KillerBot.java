package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class KillerBot {
    EntityLivingBase target;
    public static long latest;
    int disableCount = 0;
    int finderCount = 0;
    BlockPos pointPos;
    Minecraft mc = Minecraft.getMinecraft();

    public KillerBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("KillerBot")){
            FunctionManager.setStatus("Pathfinding",false);
            FunctionManager.setStatus("ShortBowAura", false);
            FunctionManager.setStatus("Killaura", false);
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(!event.status){
            if(event.function.getName().equals("KillerBot")){
                FunctionManager.setStatus("Pathfinding",false);
                FunctionManager.setStatus("ShortBowAura", false);
                FunctionManager.setStatus("Killaura", false);
            }
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals("KillerBot")){
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("KillerBot")) {
            try {
                if (target == null || target.isDead) {
                    target = getTarget();
                    if (target != null) {
                        BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
                        Pathfinder.setup(new BlockPos(Utils.floorVec(mc.thePlayer.getPositionVector())), pos, 0.0D);
                        pointPos = pos;

                        if (Pathfinder.hasPath()) {
                            FunctionManager.setStatus("Pathfinding",true);
                        } else {
                            target = null;
                        }
                    }
                }
                if(!FunctionManager.getStatus("Pathfinding")) {
                    if (finderCount > 20) {
                        target = null;
                        finderCount = 0;
                    } else {
                        finderCount = finderCount + 1;
                    }
                }
                double playerX = Minecraft.getMinecraft().thePlayer.posX;
                double playerY = Minecraft.getMinecraft().thePlayer.posY;
                double playerZ = Minecraft.getMinecraft().thePlayer.posZ;
                int bound = 3;
                List<EntityLivingBase> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(playerX - (bound / 2d), playerY - (256 / 2d), playerZ - (bound / 2d), playerX + (bound / 2d), playerY + (256 / 2d), playerZ + (bound / 2d)), null);
                if (!entityList.isEmpty()) {
                    if(((Integer) getConfigByFunctionName.get("KillerBot","mode") == 1)){
                        if(!FunctionManager.getStatus("ShortBowAura")) {
                            FunctionManager.setStatus("ShortBowAura", true);
                            FunctionManager.setStatus("Killaura", false);
                            checkSwitch((String) getConfigByFunctionName.get("KillerBot","bowName"));
                        }
                    }
                    if(((Integer) getConfigByFunctionName.get("KillerBot","mode") == 0)) {
                        if (!FunctionManager.getStatus("Killaura")) {
                            FunctionManager.setStatus("Killaura", true);
                            checkSwitch((String) getConfigByFunctionName.get("KillerBot","swordName"));
                        }
                    }
                    if(disableCount > 160) {
                        double x = pointPos.getX();
                        double y = pointPos.getY();
                        double z = pointPos.getZ();
                        List<EntityLivingBase> atPoint = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - (bound / 2d), y - (256 / 2d), z - (bound / 2d), x + (bound / 2d), y + (256 / 2d), z + (bound / 2d)), null);
                        boolean atPos = false;
                        for(EntityLivingBase entity : atPoint) {
                            if(entity == target){
                                atPos = true;
                                break;
                            }
                        }
                        if (!atPos && !(target == null || target.isDead)) {
                            FunctionManager.setStatus("Pathfinding", false);
                            BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
                            Pathfinder.setup(new BlockPos(Utils.floorVec(mc.thePlayer.getPositionVector())), pos, 0.0D);
                        }
                        disableCount = 0;
                    } else {
                        disableCount = disableCount + 1;
                    }
                } else {
                    if((Integer) getConfigByFunctionName.get("KillerBot","mode") == 0) {
                        if (FunctionManager.getStatus("Killaura")) {
                            FunctionManager.setStatus("Killaura", false);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public EntityLivingBase getTarget() {
        double x = Minecraft.getMinecraft().thePlayer.posX;
        double y = Minecraft.getMinecraft().thePlayer.posY;
        double z = Minecraft.getMinecraft().thePlayer.posZ;
        int bound = 500;
        List<EntityLivingBase> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - (bound / 2d), y - (256 / 2d), z - (bound / 2d), x + (bound / 2d), y + (256 / 2d), z + (bound / 2d)), null);
        if (!entityList.isEmpty()) {
            EntityLivingBase[] Array = entityList.toArray(new EntityLivingBase[0]);
            for (int i = 0; i < Array.length; i++) {
                EntityLivingBase insertValue=Array[i];
                int insertIndex=i-1;
                while (insertIndex>=0 && insertValue.getPositionVector().distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) < Array[insertIndex].getPositionVector().distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector())) {
                    Array[insertIndex+1]=Array[insertIndex];
                    insertIndex--;
                }
                Array[insertIndex+1]=insertValue;
            }
            for(EntityLivingBase entity : Array){
                if(isValid(entity)){
                    return entity;
                }
            }
        }
        return null;
    }
    public boolean isValid(EntityLivingBase entity){
        int type = (Integer) getConfigByFunctionName.get("KillerBot","type");
        if(entity != null) {
            if (type == 0 && entity instanceof EntityZombie && entity.posY >= 70) {
                return true;
            }
            if (type == 1 && entity instanceof EntityZombie && entity.posY < 70) {
                return true;
            }
            if (type == 2 && entity instanceof EntityPlayer && entity.getName().toLowerCase().contains("crystal sentry")) {
                return true;
            }
            return false;
        }
        return false;
    }
    public void checkSwitch(String name){
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
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
