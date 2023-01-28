package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.type.Rotation;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntityVillager;
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

public class FrozenScytheAura {
    public static EntityLivingBase target;
    private static boolean attack;
    private static ArrayList<EntityLivingBase> attackedMobs = new ArrayList();
    public FrozenScytheAura() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("FrozenScytheAura")) {
            target = null;
            attack = false;
            attackedMobs.clear();
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {
        if(event.function.getName().equals("FrozenScytheAura") && !event.status) {
            target = null;
            attack = false;
            attackedMobs.clear();
        }
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public void onUpdate(CustomEventHandler.MotionChangeEvent.Pre event) {
        if (Killaura.target == null && FunctionManager.getStatus("FrozenScytheAura") && (double)Minecraft.getMinecraft().thePlayer.ticksExisted % 1 == 0.0D) {
            boolean hasScythe = Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().hasDisplayName() && Minecraft.getMinecraft().thePlayer.getHeldItem().getDisplayName().contains("Scythe");
            if (hasScythe) {
                target = this.getTarget(target);
                if (target != null) {
                    attack = true;
                    Rotation angles = Utils.getRotation(target.getPositionVector().addVector(0.0D,((double) target.getEyeHeight() - (double) target.height / 1.5D) + 0.5D,0.0D));
                    event.yaw = angles.getYaw();
                    event.pitch = angles.getPitch();
                }

            }
        }
    }

    @SubscribeEvent
    public void onUpdatePost(CustomEventHandler.MotionChangeEvent.Post event) {
        if (attack) {
            try {
                if ((double)Minecraft.getMinecraft().thePlayer.ticksExisted % (Double) getConfigByFunctionName.get("FrozenScytheAura","delay") == 0.0D) {
                    Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    attack = false;
                }
            } catch(Exception ignored){
            }
        }
    }

    private EntityLivingBase getTarget(EntityLivingBase lastTarget) {
        Stream<Entity> var10000 = Minecraft.getMinecraft().theWorld.getLoadedEntityList().stream().filter((entityx) -> entityx instanceof EntityLivingBase).filter((entityx) -> this.isValid((EntityLivingBase)entityx));
        EntityPlayerSP var10001 = Minecraft.getMinecraft().thePlayer;
        var10001.getClass();
        List<Entity> validTargets = var10000.sorted(Comparator.comparingDouble(var10001::getDistanceToEntity)).sorted(Comparator.comparing((entityx) -> Utils.getYawDifference(lastTarget != null ? lastTarget : (EntityLivingBase)entityx, (EntityLivingBase)entityx)).reversed()).collect(Collectors.toList());
        Iterator<Entity> var3 = validTargets.iterator();
            if (var3.hasNext()) {
                Entity entity = var3.next();
                    attackedMobs.add((EntityLivingBase) entity);
                    (new Thread(() -> {
                        try {
                            Thread.sleep(350L);
                        } catch (InterruptedException var2) {
                            var2.printStackTrace();
                        }
                        attackedMobs.remove(entity);
                    })).start();
                    return (EntityLivingBase) entity;
            } else {
                return null;
            }
    }

    private boolean isValid(EntityLivingBase entity) {
        if ((!(entity == Minecraft.getMinecraft().thePlayer) && !entity.isInvisible()) && !(entity instanceof EntityArmorStand) &&  !(entity instanceof EntityBlaze) && !(entity instanceof EntityVillager) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity)) && entity.getHealth() > 0.0F && !entity.getName().equals("Dummy") && !entity.getName().startsWith("Decoy")
        && !((double)entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) > (Double) getConfigByFunctionName.get("FrozenScytheAura","range"))
        ) {
            if(entity instanceof EntityPlayer){
                if(Utils.isTeamMember(entity, Minecraft.getMinecraft().thePlayer) && !(Boolean) getConfigByFunctionName.get("FrozenScytheAura","attackTeam")) {
                    return false;
                } else {
                    return !Utils.isNPC(entity);
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
