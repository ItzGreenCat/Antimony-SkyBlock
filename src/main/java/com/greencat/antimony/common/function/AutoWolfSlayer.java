package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AutoWolfSlayer {
    EntityLivingBase target;
    public static long latest;
    boolean inBoss;
    int disableCount = 0;
    int finderCount = 0;
    BlockPos pointPos;
    Boolean isKilledBoss = false;
    Boolean isSpawnedBoss = false;
    Minecraft mc = Minecraft.getMinecraft();
    Utils utils = new Utils();
    boolean isSpawn = false;

    public AutoWolfSlayer() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("AutoWolfSlayer")){
            FunctionManager.setStatus("Pathfinding",false);
            if((Integer) getConfigByFunctionName.get("AutoWolfSlayer","mode") == 1){
                FunctionManager.setStatus("ShortBowAura", false);
                FunctionManager.setStatus("Killaura", false);
            }
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(!event.status){
            if(event.function.getName().equals("AutoWolfSlayer")){
                FunctionManager.setStatus("Pathfinding",false);
                if((Integer) getConfigByFunctionName.get("AutoWolfSlayer","mode") == 1){
                    FunctionManager.setStatus("ShortBowAura", false);
                    FunctionManager.setStatus("Killaura", false);
                }
            }
        } else {
            isKilledBoss = false;
            isSpawnedBoss = false;
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals("AutoWolfSlayer")){
            isKilledBoss = false;
            isSpawnedBoss = false;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("AutoWolfSlayer")) {
            try {
                if (target == null || target.isDead) {
                    target = getTarget();
                    if (target != null) {
                        BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
                        Pathfinder.setup(new BlockPos(Utils.floorVec(mc.thePlayer.getPositionVector())), pos, 0.0D);
                        pointPos = pos;

                        if (Pathfinder.hasPath()) {
                            FunctionManager.switchStatus("Pathfinding");
                        } else {
                            target = null;
                        }
                    } else {
                        utils.print("附近无法找到狼");
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
                List<EntityWolf> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityWolf.class, new AxisAlignedBB(playerX - (bound / 2d), playerY - (256 / 2d), playerZ - (bound / 2d), playerX + (bound / 2d), playerY + (256 / 2d), playerZ + (bound / 2d)), null);
                if (!entityList.isEmpty()) {
                    if(!isSpawnedBoss){
                        isSpawnedBoss = inBoss();
                    } else {
                        if(!isKilledBoss){
                            isKilledBoss = true;
                        } else {
                            FunctionManager.setStatus("AutoWolfSlayer", false);
                            FunctionManager.setStatus("AutoWolfSlayer", true);
                        }
                    }
                    if(((Integer) getConfigByFunctionName.get("AutoWolfSlayer","mode") == 1) && !inBoss()){
                        if(!FunctionManager.getStatus("ShortBowAura")) {
                            FunctionManager.setStatus("ShortBowAura", true);
                            FunctionManager.setStatus("Killaura", false);
                            checkSwitch((String) getConfigByFunctionName.get("AutoWolfSlayer","bowName"));
                        }
                    }
                    if(((Integer) getConfigByFunctionName.get("AutoWolfSlayer","mode") == 0) || inBoss()) {
                        if (!FunctionManager.getStatus("Killaura")) {
                            FunctionManager.setStatus("Killaura", true);
                            checkSwitch((String) getConfigByFunctionName.get("AutoWolfSlayer","swordName"));
                        }
                    }
                    if(disableCount > 160) {
                        double x = pointPos.getX();
                        double y = pointPos.getY();
                        double z = pointPos.getZ();
                        List<EntityWolf> atPoint = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityWolf.class, new AxisAlignedBB(x - (bound / 2d), y - (256 / 2d), z - (bound / 2d), x + (bound / 2d), y + (256 / 2d), z + (bound / 2d)), null);
                        boolean atPos = false;
                        for(EntityWolf entity : atPoint) {
                            if(entity == target){
                                atPos = true;
                                break;
                            }
                        }
                        if (!atPos) {
                            FunctionManager.setStatus("Pathfinding", false);
                            BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
                            Pathfinder.setup(new BlockPos(Utils.floorVec(mc.thePlayer.getPositionVector())), pos, 0.0D);
                        }
                        disableCount = 0;
                    } else {
                        disableCount = disableCount + 1;
                    }
                } else {
                    if((Integer) getConfigByFunctionName.get("AutoWolfSlayer","mode") == 0) {
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
            int bound = 50;
            List<EntityWolf> entityList = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityWolf.class, new AxisAlignedBB(x - (bound / 2d), y - (256 / 2d), z - (bound / 2d), x + (bound / 2d), y + (256 / 2d), z + (bound / 2d)), null);
            if (!entityList.isEmpty()) {
                EntityWolf[] wolfArray = entityList.toArray(new EntityWolf[0]);
                for (int i = 0; i < wolfArray.length; i++) {
                    EntityWolf insertValue=wolfArray[i];
                    int insertIndex=i-1;
                    while (insertIndex>=0 && insertValue.getPositionVector().distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) < wolfArray[insertIndex].getPositionVector().distanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector())) {
                        wolfArray[insertIndex+1]=wolfArray[insertIndex];
                        insertIndex--;
                    }
                    wolfArray[insertIndex+1]=insertValue;
                }
                return wolfArray[0];
            } else {
                return null;
            }
    }
    private boolean inBoss() {
        inBoss = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        final String combatZoneName = "slay the boss";
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), combatZoneName) && !scoreBoardLines.get(size - i).toLowerCase().contains("to")) {
                inBoss = true;
            }
        }
        return inBoss;
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
