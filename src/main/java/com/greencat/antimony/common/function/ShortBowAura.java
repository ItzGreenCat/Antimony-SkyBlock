package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
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

public class ShortBowAura {
    public static EntityLivingBase target;
    private static boolean attack;
    private static ArrayList<EntityLivingBase> attackedMobs = new ArrayList();
    public ShortBowAura() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("ShortBowAura")) {
            target = null;
            attack = false;
            attackedMobs.clear();
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {
        if(event.function.getName().equals("ShortBowAura") && !event.status) {
            target = null;
            attack = false;
            attackedMobs.clear();
        }
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public void onUpdate(CustomEventHandler.MotionChangeEvent.Pre event) {
        if (Killaura.target == null && FunctionManager.getStatus("ShortBowAura") && (double)Minecraft.getMinecraft().thePlayer.ticksExisted % (Double) getConfigByFunctionName.get("ShortBowAura","delay") == 0.0D) {
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

    private void click() {
            if(!(Boolean) getConfigByFunctionName.get("ShortBowAura","right")) {
                Minecraft.getMinecraft().thePlayer.swingItem();

            } else {
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.getHeldItem()));
            }

    }

    private boolean isValid(EntityLivingBase entity) {
        if ((!(entity == Minecraft.getMinecraft().thePlayer) && !entity.isInvisible()) && !(entity instanceof EntityArmorStand) &&  !(entity instanceof EntityBlaze) && !(entity instanceof EntityVillager) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity)) && entity.getHealth() > 0.0F && !entity.getName().equals("Dummy") && !entity.getName().startsWith("Decoy")
        && !((double)entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) > (Double) getConfigByFunctionName.get("ShortBowAura","range"))
        ) {
            if(entity instanceof EntityPlayer){
                if(Utils.isTeamMember(entity, Minecraft.getMinecraft().thePlayer) && !(Boolean) getConfigByFunctionName.get("ShortBowAura","attackTeam")) {
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
