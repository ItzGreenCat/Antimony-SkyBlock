package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.greencat.antimony.core.config.ConfigInterface.get;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MacroerDetector extends FunctionStatusTrigger {
    List<EntityPlayer> players = new ArrayList<EntityPlayer>();
    List<EntityPlayer> probablyMacroers = new ArrayList<EntityPlayer>();
    HashMap<EntityPlayer, Vec3> lastRefreshPlayerPos = new HashMap<EntityPlayer, Vec3>();
    Boolean isEnable = false;
    int range = 250;
    int refreshTick = 0;
    int detectorTick = 0;
    public MacroerDetector(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
            if (FunctionManager.getStatus("MacroerDetector")) {
                isEnable = true;
                range = (Integer) get("MacroerDetector", "range");
            } else {
                isEnable = false;
            }
            if (isEnable) {
                if (refreshTick + 1 > 200) {
                    players.clear();
                    probablyMacroers.clear();
                    double x = Minecraft.getMinecraft().thePlayer.posX;
                    double y = Minecraft.getMinecraft().thePlayer.posY;
                    double z = Minecraft.getMinecraft().thePlayer.posZ;
                    List<EntityLivingBase> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - (range * 2 / 2d), y - (range * 2 / 2d), z - (range * 2 / 2d), x + (range * 2 / 2d), y + (range * 2 / 2d), z + (range * 2 / 2d)), null);
                    for (Entity entity : entities) {
                        if (entity instanceof EntityPlayer) {
                            players.add((EntityPlayer) entity);
                        }
                    }
                    refreshTick = 0;
                } else {
                    refreshTick = refreshTick + 1;
                }
                if (detectorTick + 1 > 100) {
                    for (EntityPlayer player : players) {
                        Vec3 playerPos = player.getPositionVector();
                        for(Map.Entry<EntityPlayer,Vec3> entry : lastRefreshPlayerPos.entrySet()){
                            if(entry.getKey() != null && entry.getKey().getEntityId() == player.getEntityId()){
                                playerPos = entry.getValue();
                                break;
                            }
                        }
                        boolean hasLava = false;
                        boolean hasBedrock = false;
                        if ((Math.abs(player.getPositionVector().xCoord - playerPos.xCoord) + Math.abs(player.getPositionVector().zCoord - playerPos.zCoord)) < 0.65) {
                            try {
                                Iterable<BlockPos> blocks = BlockPos.getAllInBox(player.getPosition().add(5, -3, 5), player.getPosition().add(-5, 4, -5));
                                    for (BlockPos pos : blocks) {
                                        if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.lava) {
                                            hasLava = true;
                                        }
                                        if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.bedrock) {
                                            hasBedrock = true;
                                        }
                                        if (hasLava && hasBedrock) {
                                            break;
                                        }

                                    }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                        if (hasLava && player.getHeldItem() != null && player.getHeldItem().getItem() == Items.fishing_rod) {
                            double x = Minecraft.getMinecraft().thePlayer.posX;
                            double y = Minecraft.getMinecraft().thePlayer.posY;
                            double z = Minecraft.getMinecraft().thePlayer.posZ;
                            int range = 5;
                            boolean hasSilverfish = false;
                            List<EntityLivingBase> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - (range * 2 / 2d), y - (range * 2 / 2d), z - (range * 2 / 2d), x + (range * 2 / 2d), y + (range * 2 / 2d), z + (range * 2 / 2d)), null);
                            for (EntityLivingBase entity : entities) {
                                if (entity instanceof EntitySilverfish) {
                                    hasSilverfish = true;
                                    break;
                                }
                            }
                            if (hasSilverfish) {
                                probablyMacroers.add(player);
                            }
                        }
                        if (hasBedrock && player.getHeldItem() != null && player.getHeldItem().hasDisplayName() && (player.getHeldItem().getDisplayName().contains("Pickaxe") || player.getHeldItem().getDisplayName().contains("Drill") || player.getHeldItem().getDisplayName().contains("Gauntlet"))) {
                            probablyMacroers.add(player);
                        }
                        lastRefreshPlayerPos.put(player,player.getPositionVector());
                    }
                    detectorTick = 0;
                } else {
                    detectorTick = detectorTick + 1;
                }
            }
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if(isEnable && probablyMacroers != null){
            for(EntityPlayer player : probablyMacroers){
                Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPositionVector(),player.getPositionVector(), Chroma.color,3.0F);
                Utils.OutlinedBoxWithESP(player.getEntityBoundingBox(),Chroma.color,false,2.5F);
            }
        }
    }
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event){
        if(isEnable){
            lastRefreshPlayerPos.clear();
        }
    }

    @Override
    public String getName() {
        return "MacroerDetector";
    }

    @Override
    public void post() {

    }

    @Override
    public void init(){
        players.clear();
        probablyMacroers.clear();
        lastRefreshPlayerPos.clear();
    }
}
