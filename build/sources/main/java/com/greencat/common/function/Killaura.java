package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.common.mixins.EntityPlayerSPAccessor;
import com.greencat.antimony.core.settings.SettingBoolean;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Mouse;

import java.util.Iterator;
import java.util.List;

public class Killaura {
    public static EntityLivingBase entityTarget;
    public boolean wasDown;
    public double maxRotationPitch = 100.0D;
    public double maxRotationYaw = 120.0D;
    public double range = 0.0D;
    public double rotationRange = 6.0D;
    public double fov = 270.0D;
    public boolean checkTeam = false;
    public boolean checkNPC = false;
    public static boolean attackPlayer = false;

    public Killaura() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if(FunctionManager.getStatus("Killaura")) {
            if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                this.wasDown = Mouse.isButtonDown(2) && Minecraft.getMinecraft().currentScreen == null;
            }
        }
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("Killaura")) {
            entityTarget = null;
        }
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionSwitchEvent event) {
        if(event.function.getName().equals("Killaura") && !event.status) {
            entityTarget = null;
        }
    }

    @SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    public void onMovePre(CustomEventHandler.MotionChangeEvent.Pre event) {
        if (FunctionManager.getStatus("Killaura")) {
            try {
                attackPlayer = (boolean) getConfigByFunctionName.get("Killaura", "isAttackPlayer");
                range = (Double)getConfigByFunctionName.get("Reach","distance");
                maxRotationPitch = (Double)getConfigByFunctionName.get("Killaura","maxPitch");
                maxRotationYaw = (Double)getConfigByFunctionName.get("Killaura","maxYaw");
                rotationRange = maxRotationPitch = (Double)getConfigByFunctionName.get("Killaura","maxRotationRange");
                fov = (Double)getConfigByFunctionName.get("Killaura","Fov");
                checkTeam = !(boolean) getConfigByFunctionName.get("Killaura", "isAttackTeamMember");
                checkNPC = !(boolean) getConfigByFunctionName.get("Killaura", "isAttackNPC");
            } catch(Exception e){
                e.printStackTrace();
            }
            entityTarget = this.getTarget();
            if (entityTarget != null) {
                float[] angles = Utils.getServerAngles(entityTarget);
                event.yaw = ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - MathHelper.wrapAngleTo180_float((float) Math.max(-this.maxRotationYaw, Math.min((double) (((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - angles[0]), this.maxRotationYaw)));
                event.pitch = ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - MathHelper.wrapAngleTo180_float((float) Math.max(-this.maxRotationPitch, Math.min((double) (((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - angles[1]), this.maxRotationPitch)));
            } else {
                entityTarget = null;
            }
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public void onMovePost(CustomEventHandler.MotionChangeEvent.Post event) {
        if(FunctionManager.getStatus("Killaura")) {
            if (entityTarget != null && Minecraft.getMinecraft().thePlayer.ticksExisted % 2 == 0) {
                Utils.updateItemNoEvent();
                if ((double) Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entityTarget) < this.range) {
                    if (Minecraft.getMinecraft().thePlayer.isUsingItem()) {
                        Minecraft.getMinecraft().playerController.onStoppedUsingItem(Minecraft.getMinecraft().thePlayer);
                    }

                    Minecraft.getMinecraft().thePlayer.swingItem();
                    float[] angles = Utils.getServerAngles(entityTarget);
                    if (Math.abs(((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - angles[1]) < 25.0F && Math.abs(((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - angles[0]) < 15.0F) {
                        Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, entityTarget);
                    }

                    if (!Minecraft.getMinecraft().thePlayer.isUsingItem() && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemSword && /*blockfalse*/ false) {
                        Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                    }
                }
            }
        }

    }

    private EntityLivingBase getTarget() {
        if ((!(Minecraft.getMinecraft().currentScreen instanceof GuiContainer) && Minecraft.getMinecraft().theWorld != null)) {
            Double x = Minecraft.getMinecraft().thePlayer.posX;
            Double y = Minecraft.getMinecraft().thePlayer.posY;
            Double z = Minecraft.getMinecraft().thePlayer.posZ;
            List<EntityLivingBase> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - (range*2 / 2d), y - (range*2 / 2d), z - (range*2 / 2d), x + (range*2 / 2d), y + (range*2 / 2d), z + (range*2 / 2d)), null);
            for(EntityLivingBase entity : entities){
                if(isValid(entity)){
                    return entity;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    private boolean isValid(EntityLivingBase entity) {
        if ((!(entity == Minecraft.getMinecraft().thePlayer) && !entity.isInvisible()) && !(entity instanceof EntityArmorStand) && !(entity instanceof EntityVillager) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity)) && entity.getHealth() > 0.0F && entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= (entityTarget != null && entityTarget != entity ? this.range : Math.max(this.rotationRange, this.range)) && Utils.isWithinFOV(entity, this.fov + 5.0D) && Utils.isWithinPitch(entity, this.fov + 5.0D)) {
            if((entity instanceof EntityPlayer) && (Utils.isTeam(entity, Minecraft.getMinecraft().thePlayer) && checkTeam || Utils.isNPC(entity) && checkNPC)) {
                return attackPlayer;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


}

