package com.greencat.antimony.common.function;

import com.greencat.Antimony;
import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.common.mixins.KeyBindingAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.timer.TimerUtils;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.greencat.antimony.core.config.ConfigInterface.get;

public class Killaura extends FunctionStatusTrigger implements ReflectionlessEventHandler {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean isEnable = false;
    private static final String NAME = "Killaura";

    private static final HashSet<Integer> attackedMobs = new HashSet<>();

    public static Entity target;
    public static boolean blocking;
    public static float yaw = 0;
    public static float pitch = 0;
    public static TimerUtils timer = new TimerUtils();
    public static double cps = 10;
    public static double cpsRandom = 2.5;
    public static double range = 4.5;
    public static double findRange = 6;
    public static int mode = 0;
    public static int switchDelay = 350;
    public static Boolean playerOnly = true;
    public static Boolean silentRots = true;
    public static Boolean smoothRots = true;
    public static Boolean keepSprint = true;
    public static Boolean autoBlock = false;
    public static Boolean rots = true;
    public static Boolean is19 = false;
    public static Boolean throughWall = true;
    public static Boolean checkNPC = true;
    public static Boolean checkTeam = true;
    public static Boolean attackPlayer = true;
    public static Boolean swordOnly = false;
    public static int autoBlockMode = 1;
    public static int hypixelTicks = 3;
    public static int rotationMode = 1;
    public static Boolean hvh = false;
    public static int packets = 100;
    public static Boolean noDelay = false;


    public Killaura() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (FunctionManager.getStatus("Killaura")) {
            isEnable = true;
            cps = (double)get(NAME,"cps");
            cpsRandom = (double)get(NAME,"cps_spike");
            range = (double)get(NAME,"range");
            findRange = (double)get(NAME,"find_range");
            mode = (int)get(NAME,"mode");
            switchDelay = (int)get(NAME,"switchDelay");
            playerOnly = (boolean)get(NAME,"playerOnly");
            silentRots = (boolean)get(NAME,"silent_rotation");
            smoothRots = (boolean)get(NAME,"smooth_rotation");
            keepSprint = (boolean)get(NAME,"keep_sprint");
            autoBlock = (boolean)get(NAME,"auto_block");
            rots = (boolean)get(NAME,"rotation");
            is19 = (boolean)get(NAME,"19_mode");
            autoBlockMode = (int)get(NAME,"auto_block_mode");
            hypixelTicks = (int)get(NAME,"hypixel_ticks");
            rotationMode = (int)get(NAME,"rotation_mode");
            hvh = (boolean)get(NAME,"hvh");
            packets = (int)get(NAME,"packets");
            noDelay = (boolean)get(NAME,"no_delay");
            checkTeam = !(boolean) ConfigInterface.get("Killaura", "isAttackTeamMember");
            checkNPC = !(boolean) ConfigInterface.get("Killaura", "isAttackNPC");
            attackPlayer = (boolean) ConfigInterface.get("Killaura", "isAttackPlayer");
            swordOnly = (boolean) ConfigInterface.get("Killaura", "swordOnly");
        } else {
            isEnable = false;
        }
    }

    @Override
    public String getName() {
        return "Killaura";
    }

    public void init() {
        if (Minecraft.getMinecraft().thePlayer != null) {
            blocking = false; 
            yaw = 0; 
            pitch = 0;
        }
    }

    public void post() {
        if (Minecraft.getMinecraft().thePlayer != null) {
            target = null;
            if(blocking) mc.thePlayer.stopUsingItem(); blocking = false;
            if(autoBlockMode == 5) {
                ((KeyBindingAccessor)mc.gameSettings.keyBindUseItem).setPressed(false);
            }
        }
    }
    @Override
    public void invoke(Event event){
        if (event instanceof CustomEventHandler.MotionChangeEvent) {
            onUpdate((CustomEventHandler.MotionChangeEvent) event);
        }
    }
    public void onUpdate(CustomEventHandler.MotionChangeEvent event) {
        if(!isEnable){
            return;
        }
        if(Minecraft.getMinecraft().thePlayer != null && (Minecraft.getMinecraft().thePlayer.getHeldItem() == null || !(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()  instanceof ItemSword))){
            if(swordOnly){
                return;
            }
        }
        if(!event.isPre())
            return;
        for(Entity e: mc.theWorld.loadedEntityList) {
            if(isValidEntity(e)) {
                if(e != target) {
                    yaw = mc.thePlayer.rotationYaw;
                    pitch = mc.thePlayer.rotationPitch;
                }
                if(!attackedMobs.contains(e.getEntityId()) || mode != 1) {
                    target = e;
                    if(mode == 1){
                        attackedMobs.add(e.getEntityId());
                        (new Thread(() -> {
                            try {
                                Thread.sleep(switchDelay);
                                attackedMobs.remove(e.getEntityId());
                            } catch (Exception var2) {
                                var2.printStackTrace();
                            }
                        })).start();
                        blocking = false;
                        break;
                    }
                }
                blocking = false;
            }
        }
        if(target == null && !attackedMobs.isEmpty()){
            attackedMobs.clear();
        }

        if(target != null) {

            if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target != null && target.getDistanceToEntity(mc.thePlayer) < findRange && !mc.thePlayer.isUsingItem() && autoBlock) {
                switch(autoBlockMode) {
                    case 2: {
                        if(event.isPre())
                            mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 2);
                        break;
                    }
                    case 3: {
                        int t = hypixelTicks;
                        if(mc.thePlayer.ticksExisted % t == 0) {
                            Utils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                            blocking = true;
                        }else if(mc.thePlayer.ticksExisted % t == 1) {
                            Utils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            blocking = false;
                        }
                        break;
                    }
                    case 4: {
                        if(event.isPre()) {
                            if(!mc.thePlayer.isBlocking()) {
                                Utils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                            }
                            blocking = true;
                        }
                        break;
                    }
                    case 5: {
                        if(!mc.gameSettings.keyBindUseItem.isPressed() && target.getDistanceToEntity(mc.thePlayer) < range)
                            ((KeyBindingAccessor)mc.gameSettings.keyBindUseItem).setPressed(true);
                        break;
                    }
                }
            }
            if(target.getDistanceToEntity(mc.thePlayer) > findRange || target.isDead || target.isInvisible()) {
                target = null;
                if(autoBlockMode == 5) {
                    ((KeyBindingAccessor)mc.gameSettings.keyBindUseItem).setPressed(false);
                }
                return;
            }
            if(rots) {
                if(rotationMode != 4) {
                    float[] rotz = new float[2];
                    switch (rotationMode) {
                        case 1: {
                            rotz = MatrixRotations(target);
                            break;
                        }
                        case 2: {
                            rotz = SimpleRotations(target);
                            break;
                        }
                        case 3: {
                            rotz = otherSimpleRotThingy(target);
                            break;
                        }
                    }
                    float speed = mc.gameSettings.mouseSensitivity - (ThreadLocalRandom.current().nextFloat() - 0.5F) * 0.3F;
                    speed = Math.min(0.2F, speed);
                    yaw += (rotz[0] - yaw) * speed;
                    pitch += (rotz[1] - pitch) * speed;
                    if (!smoothRots) {
                        yaw = rotz[0];
                        pitch = rotz[1];
                    }
                    event.yaw = yaw;
                    event.pitch = pitch;
                }
            }
            double cpsRandomized = (cpsRandom >= 0.05 ? (cps + ThreadLocalRandom.current().nextDouble(-Math.abs(cpsRandom - 1), cpsRandom)) : cps);
            if(!silentRots) {
                mc.thePlayer.rotationYaw = event.yaw;
                mc.thePlayer.rotationPitch = event.pitch;
            }
            double time = 1000/cpsRandomized;
            if(noDelay) time = 0;
            if(timer.hasReached(time) && target.getDistanceToEntity(mc.thePlayer) < range+1) {
                if(event.isPre()) {
                    if(autoBlockMode == 5) {
                        ((KeyBindingAccessor)mc.gameSettings.keyBindUseItem).setPressed(false);
                    }
                    mc.thePlayer.stopUsingItem();
                    mc.thePlayer.swingItem();
                    if(blocking && autoBlockMode == 3) {
                        Utils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        blocking = false;
                    }
                    if(target.getDistanceToEntity(mc.thePlayer) <= range) {
                        if(rotationMode == 4) {
                            event.yaw = otherSimpleRotThingy(target)[0];
                            event.pitch = otherSimpleRotThingy(target)[1];
                        }
                        if(hvh){
                            for(int i = 0; i < packets; i++) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                            }
                        }else{
                            if(keepSprint) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                            }else {
                                mc.playerController.attackEntity(mc.thePlayer, target);
                            }
                        }
                    }
                    if (timer.hasReached(time + (50 + ThreadLocalRandom.current().nextInt(50)))) {
                        timer.reset();
                    }
                }
            }
        }
    }
    public static float[] MatrixRotations(Entity entity) {
        boolean random = ThreadLocalRandom.current().nextBoolean();
        double diffX = entity.posX - mc.thePlayer.posX, diffY;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9
                    - (mc.thePlayer.posY
                    + mc.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D
                    - (mc.thePlayer.posY
                    + mc.thePlayer.getEyeHeight());
        }
        double diffZ = entity.posZ - mc.thePlayer.posZ, dist = Math.hypot(diffX, diffZ);
        float sensitivity = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F,
                gcd = sensitivity * sensitivity * sensitivity * 1.2F,
                y = 6.0F*ThreadLocalRandom.current().nextFloat(),
                p = 6.0F*ThreadLocalRandom.current().nextFloat(),
                yawRand = random ? -RandomUtils.nextFloat(0.0F, y) : RandomUtils.nextFloat(0.0F, y),
                pitchRand = random ? -RandomUtils.nextFloat(0.0F, p) : RandomUtils.nextFloat(0.0F, p),
                yaw = ((float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F) + yawRand,
                pitch = MathHelper.clamp_float(((float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI)) + pitchRand + mc.thePlayer.getDistanceToEntity(entity) * 1.25F, -90.0F, 90.0F);
        if (mc.thePlayer.ticksExisted % 2 == 0) {
            pitch = MathHelper.clamp_float(pitch + (random ? RandomUtils.nextFloat(2.0F, 8.0F) : -RandomUtils.nextFloat(2.0F, 8.0F)), -90.0F, 90.0F);
        }
        pitch -= pitch % gcd;
        yaw -= yaw % gcd;
        return new float[]{
                mc.thePlayer.rotationYaw
                        + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
                mc.thePlayer.rotationPitch
                        + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)
        };
    }

    public static float[] SimpleRotations(Entity entity) {
        double diffX = entity.posX - mc.thePlayer.posX, diffY;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9
                    - (mc.thePlayer.posY
                    + mc.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D
                    - (mc.thePlayer.posY
                    + mc.thePlayer.getEyeHeight());
        }
        double diffZ = entity.posZ - mc.thePlayer.posZ, dist = Math.hypot(diffX, diffZ);
        float sensitivity = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F,
                gcd = sensitivity * sensitivity * sensitivity * 1.2F,
                yaw = ((float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F),
                pitch = MathHelper.clamp_float(((float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI)) + mc.thePlayer.getDistanceToEntity(entity) * 1.25F, -90.0F, 90.0F);
        if (mc.thePlayer.ticksExisted % 2 == 0) {
            pitch = MathHelper.clamp_float(pitch, -90.0F, 90.0F);
        }
        pitch -= pitch % gcd;
        yaw -= yaw % gcd;
        return new float[]{
                mc.thePlayer.rotationYaw
                        + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
                mc.thePlayer.rotationPitch
                        + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)
        };
    }

    public static float[] otherSimpleRotThingy(Entity entity) {
        return new float[]{
                (float) Math.toDegrees(Math.atan2(entity.posZ - mc.thePlayer.posZ, entity.posX - mc.thePlayer.posX)) - 90,
                (float) Math.toDegrees(-Math.atan2(entity.posY - mc.thePlayer.posY, Math.sqrt(Math.pow(entity.posX - mc.thePlayer.posX, 2) + Math.pow(entity.posZ - mc.thePlayer.posZ, 2))))
        };
    }
    
    @SubscribeEvent
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if (isEnable) {
            Utils.print("检测到世界服务器改变,自动关闭Killaura");
            FunctionManager.setStatus("Killaura", false);
        }
    }
    @SubscribeEvent
    public void Render(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("Killaura")) {
            if ((boolean) ConfigInterface.get("Killaura", "targetESP")) {
                if (target != null && !target.isDead) {
                    try {
                        Utils.RenderTargetESP((EntityLivingBase) target, Antimony.Color.AntimonyCyan);
                    } catch (Exception ignored) {}
                }
            }
        }
    }
    public boolean isValidEntity(Entity entity) {
        try {
            if((!(entity instanceof EntityPlayer)) && playerOnly){
                return false;
            }
            if ((!(entity == Minecraft.getMinecraft().thePlayer) && !entity.isInvisible()) && !(entity instanceof EntityArmorStand) && !(entity instanceof EntityVillager) && !(entity instanceof EntityPigZombie && ((EntityLivingBase) entity).isChild()) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity) || throughWall) && ((EntityLivingBase) entity).getHealth() > 0.0F && entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= findRange) {
                if (entity instanceof EntityPlayer) {
                    if (Utils.isTeamMember((EntityLivingBase) entity, Minecraft.getMinecraft().thePlayer) && checkTeam) {
                        return false;
                    } else {
                        if (Utils.isNPC(entity) && checkNPC) {
                            return false;
                        } else {
                            return attackPlayer;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch(Exception e){
            return false;
        }
    }
}