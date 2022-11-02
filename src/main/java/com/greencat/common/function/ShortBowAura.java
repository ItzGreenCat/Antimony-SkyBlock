package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShortBowAura {
    public static EntityLivingBase target;
    private static boolean attack;
    public boolean isInTheCatacombs = false;
    private static ArrayList<EntityLivingBase> attackedMobs = new ArrayList();
    public ShortBowAura() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public void onUpdate(CustomEventHandler.MotionChangeEvent.Pre event) {
        if (Killaura.entityTarget == null && FunctionManager.getStatus("ShortBowAura") && (double)Minecraft.getMinecraft().thePlayer.ticksExisted % (Double) getConfigByFunctionName.get("ShortBowAura","delay") == 0.0D && (isInDungeon() || (Boolean) getConfigByFunctionName.get("ShortBowAura","onlyDungeon"))) {
            boolean hasShortBow = Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() == Items.bow;
            if (hasShortBow) {
                target = this.getTarget(target);
                if (target != null) {
                    attack = true;
                    float[] angles = Utils.getBowAngles(target);
                    event.yaw = angles[0];
                    event.pitch = angles[1];
                }

            }
        }
    }

    @SubscribeEvent
    public void onUpdatePost(CustomEventHandler.MotionChangeEvent.Post event) {
        if (attack) {
            int held = Minecraft.getMinecraft().thePlayer.inventory.currentItem;

            Utils.updateItemNoEvent();
            this.click();
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = held;
            Utils.updateItemNoEvent();
            attack = false;
        }
    }

    private EntityLivingBase getTarget(EntityLivingBase lastTarget) {
            Stream Stream1145141919810 = Minecraft.getMinecraft().theWorld.getLoadedEntityList().stream().filter((entityx) -> {
                return entityx instanceof EntityLivingBase;
            }).filter((entityx) -> {
                return this.isValid((EntityLivingBase)entityx);
            });
            EntityPlayerSP var10001 = Minecraft.getMinecraft().thePlayer;
            var10001.getClass();
            List<Entity> validTargets = (List)Stream1145141919810.sorted(Comparator.comparingDouble(var10001::getDistanceToEntity)).sorted(Comparator.comparing((entityx) -> {
                return Utils.getYawDifference(lastTarget != null ? lastTarget : (EntityLivingBase)entityx, (EntityLivingBase)entityx);
            }).reversed()).collect(Collectors.toList());
            Iterator var3 = validTargets.iterator();
            if (var3.hasNext()) {
                Entity entity = (Entity)var3.next();
                attackedMobs.add((EntityLivingBase)entity);
                (new Thread(() -> {
                    try {
                        Thread.sleep(350L);
                    } catch (InterruptedException var2) {
                        var2.printStackTrace();
                    }

                    attackedMobs.remove(entity);
                })).start();
                return (EntityLivingBase)entity;
            } else {
                return null;
            }
    }

    private void click() {
            if(!(Boolean) getConfigByFunctionName.get("ShortBowAura","right")) {
                Minecraft.getMinecraft().thePlayer.swingItem();

            } else {
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.getHeldItem()));
            }

    }

    private boolean isValid(EntityLivingBase entity) {
        if (entity != Minecraft.getMinecraft().thePlayer && !(entity instanceof EntityArmorStand) && Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity) && !(entity.getHealth() <= 0.0F) && !((double)entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) > (Double) getConfigByFunctionName.get("ShortBowAura","range")) && (!(entity instanceof EntityPlayer) && !(entity instanceof EntityBat) && !(entity instanceof EntityZombie) && !(entity instanceof EntityGiantZombie) || !entity.isInvisible()) && !entity.getName().equals("Dummy") && !entity.getName().startsWith("Decoy")) {
            return !attackedMobs.contains(entity) && !(entity instanceof EntityBlaze) && (!Utils.isTeamMember(Minecraft.getMinecraft().thePlayer, entity) || (Boolean) getConfigByFunctionName.get("ShortBowAura","attackTeam"));
        } else {
            return false;
        }
    }
    private boolean isInDungeon() {
        isInTheCatacombs = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        final String combatZoneName = "the catacombs";
        final String clearedName = "dungeon cleared";
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), clearedName)) {
                isInTheCatacombs = true;
            }
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), combatZoneName) && !scoreBoardLines.get(size - i).toLowerCase().contains("to")) {
                isInTheCatacombs = true;
            }
        }
        return isInTheCatacombs;
    }
}
