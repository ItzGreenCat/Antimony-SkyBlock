package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.type.Rotation;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrozenScytheAura extends FunctionStatusTrigger implements ReflectionlessEventHandler {
    public static EntityLivingBase target;
    private static boolean attack;
    private static ArrayList<EntityLivingBase> attackedMobs = new ArrayList();
    public FrozenScytheAura() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @Override
    public String getName() {
        return "FrozenScytheAura";
    }

    @Override
    public void post() {
        target = null;
        attack = false;
        attackedMobs.clear();
    }

    @Override
    public void init() {

    }
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
    public void onUpdatePost(CustomEventHandler.MotionChangeEvent.Post event) {
        if (attack) {
            try {
                if ((double)Minecraft.getMinecraft().thePlayer.ticksExisted % (Double) ConfigInterface.get("FrozenScytheAura","delay") == 0.0D) {
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
        && !((double)entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) > (Double) ConfigInterface.get("FrozenScytheAura","range"))
        ) {
            if(entity instanceof EntityPlayer){
                if(Utils.isTeamMember(entity, Minecraft.getMinecraft().thePlayer) && !(Boolean) ConfigInterface.get("FrozenScytheAura","attackTeam")) {
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
    @Override
    public void invoke(Event event){
        if(event instanceof CustomEventHandler.MotionChangeEvent.Pre){
            onUpdate((CustomEventHandler.MotionChangeEvent.Pre) event);
            return;
        }
        if(event instanceof CustomEventHandler.MotionChangeEvent.Post){
            onUpdatePost((CustomEventHandler.MotionChangeEvent.Post) event);
        }
    }
}
