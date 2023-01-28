package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.S3DPacketDisplayScoreboardAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.notice.Notice;
import com.greencat.antimony.core.notice.NoticeManager;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.timer.SystemTimer2;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.greencat.antimony.utils.Utils.sendPacketNoEvent;

public class Killaura {
    //public static EntityLivingBase entityTarget;
    //public static boolean wasDown;
    public static double maxRotationPitch = 100.0D;
    public static double maxRotationYaw = 120.0D;
    public static double range = 0.0D;
    public static double rotationRange = 6.0D;
    public static double fov = 270.0D;
    public static boolean checkTeam = false;
    public static boolean checkNPC = false;
    public static boolean autoBlock = false;
    public static boolean attackPlayer = false;
    public static boolean onlySword = false;
    public static Double[] currentHeight = {0.0D};
    public static Boolean[] RenderStatus = {true};

    public static String server = "EMPTY";

    public static boolean lagCheck = false;
    public static double wallRange = 3.5D;
    public static double blockRange = 3.5D;
    public static double switchDelay = 400.0D;
    public static int mode = 0;
    public static double cps = 5.0D;
    public static int smoothRotation = 60;

    public static int tickTimer = 0;

    Minecraft mc = Minecraft.getMinecraft();

    public static Entity target;
    private static int switchIndex;
    private static List<Entity> targetList = new CopyOnWriteArrayList<>();
    private static final SystemTimer2 timerAttack = new SystemTimer2(), timerSwitch = new SystemTimer2(), lossTimer = new SystemTimer2();
    private static float yaw, pitch, iyaw, ipitch, alpha;
    private static final Vector<Packet<?>> toDispatch = new Vector<>();
    public static boolean blinking, blocking;

    private boolean isEnable = false;

    public Killaura() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onPacketReceived(CustomEventHandler.PacketReceivedEvent event){
        if(isEnable){
            if (event.packet instanceof S3DPacketDisplayScoreboard) {
                S3DPacketDisplayScoreboard packet = (S3DPacketDisplayScoreboard) event.packet;
                String serverName = ((S3DPacketDisplayScoreboardAccessor)packet).getScoreName();
                if (serverName.equalsIgnoreCase("SForeboard")) {
                    server = "sw";
                } else if (serverName.equalsIgnoreCase("BForeboard")) {
                    server = "bw";
                }
            }
        }
    }
    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (FunctionManager.getStatus("Killaura")) {
            /*if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                wasDown = Mouse.isButtonDown(2) && Minecraft.getMinecraft().currentScreen == null;
            }*/
            try {
                attackPlayer = (boolean) getConfigByFunctionName.get("Killaura", "isAttackPlayer");
                range = (Double) getConfigByFunctionName.get("Reach", "distance");
                maxRotationPitch = (Double) getConfigByFunctionName.get("Killaura", "maxPitch");
                maxRotationYaw = (Double) getConfigByFunctionName.get("Killaura", "maxYaw");
                rotationRange = (Double) getConfigByFunctionName.get("Killaura", "maxRotationRange");
                fov = (Double) getConfigByFunctionName.get("Killaura", "Fov");
                checkTeam = !(boolean) getConfigByFunctionName.get("Killaura", "isAttackTeamMember");
                checkNPC = !(boolean) getConfigByFunctionName.get("Killaura", "isAttackNPC");
                autoBlock = (boolean) getConfigByFunctionName.get("Killaura", "autoBlock");
                onlySword = (boolean) getConfigByFunctionName.get("Killaura", "onlySword");
                lagCheck = (boolean) getConfigByFunctionName.get("Killaura", "lagCheck");
                wallRange = (Double) getConfigByFunctionName.get("Killaura", "wallRange");
                blockRange = (Double) getConfigByFunctionName.get("Killaura", "blockRange");
                mode = (Integer) getConfigByFunctionName.get("Killaura", "mode");
                switchDelay = (Double) getConfigByFunctionName.get("Killaura", "switchDelay");
                cps = (Double) getConfigByFunctionName.get("Killaura", "cps");
                smoothRotation = (Integer) getConfigByFunctionName.get("Killaura", "smooth");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isEnable = true;
        } else {
            isEnable = false;
        }
    }
    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public String mode() {
        if(mode == 0){
            return "Single";
        }
        if(mode == 1){
            return "Switch";
        }
        return "";
    }

    public double range() {
        return range;
    }

    public double blockRange() {
        return blockRange;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if (event.function.getName().equals("Killaura")) {
            post();
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event) {
        if (event.function.getName().equals("Killaura")) {
            init();
        }
    }

    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {
        if (event.function.getName().equals("Killaura")) {
            if (!event.status) {
                post();
            } else {
                init();
            }
        }
    }
    private void init(){
        if(Minecraft.getMinecraft().thePlayer != null) {
            alpha = 0;
            iyaw = mc.thePlayer.rotationYaw;
            ipitch = mc.thePlayer.rotationPitch;
            lossTimer.reset();
            tickTimer = 0;

            blinking = false;
        }
    }
    private void post(){
        //entityTarget = null;
        if(Minecraft.getMinecraft().thePlayer != null) {
            yaw = iyaw = mc.thePlayer.rotationYaw;
            pitch = ipitch = mc.thePlayer.rotationPitch;
            target = null;
            targetList.clear();
            tickTimer = 0;
            dispatchPackets();
        }
    }

    private boolean heldSword() {
        if(mc.thePlayer != null) {
            return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
        } else {
            return false;
        }
    }

    private boolean isInFOV() {
        if (target != null) {
            float[] rotationPosition = Utils.getAngles(target);
            int pitchByPos = (int) rotationPosition[1], yawByPos = (int) rotationPosition[0], yaw = (int) mc.thePlayer.rotationYaw,
                    pitch = (int) mc.thePlayer.rotationPitch, differenceYaw = Math.abs(yaw - yawByPos), differencePitch = Math.abs(pitch - pitchByPos);

            return differenceYaw <= maxRotationYaw && differencePitch <= maxRotationPitch;
        }

        return false;
    }

    public boolean serverLag() {
        return lagCheck && lossTimer.getLastDelay() >= 100;
    }

    public boolean shouldAttack() {
        return target != null && target.isEntityAlive() && mc.thePlayer.getDistanceToEntity(target) <=
                (mc.thePlayer.canEntityBeSeen(target) ? range : wallRange) && isInFOV();
    }

    public boolean shouldRotate() {
        return shouldAttack() && !serverLag() && !isBreakingBlock();
    }

    public boolean shouldBlock() {
        return autoBlock && target != null && heldSword() && target.isEntityAlive() && isEnable && !isBreakingBlock() &&
                mc.thePlayer.getDistanceToEntity(target) <= (mc.thePlayer.canEntityBeSeen(target) ? blockRange : wallRange);
    }

    private boolean isAutismShopKeeperCheck(Entity target) {
        IChatComponent component = target.getDisplayName();
        String formatted = component.getFormattedText();
        boolean first = !formatted.substring(0, formatted.length() - 2).contains("\u00A7");
        boolean second = formatted.substring(formatted.length() - 2).contains("\u00A7");
        return server.equals("bw") && first && second;
    }
    public boolean isBreakingBlock() {
        return mc.gameSettings.keyBindAttack.isKeyDown() && mc.thePlayer.capabilities.allowEdit &&
                mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK);
    }

    private boolean goBack;

    @SubscribeEvent
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if (isEnable) {
            if (goBack) {
                sendPacketNoEvent(new C01PacketChatMessage("/back"));
                goBack = false;
            }
            Utils.print("检测到世界服务器改变,自动关闭Killaura");
            FunctionManager.setStatus("Killaura", false);
        }
    }
    @SubscribeEvent
    public void onMotionUpdatePre(CustomEventHandler.MotionChangeEvent.Pre event){
        if(isEnable){
            if(Minecraft.getDebugFPS() == 0){
                return;
            }
            double clamp = MathHelper.clamp_double(mc.getDebugFPS() / 30, 1, 9999);

            if (alpha < 1) {
                alpha = (float) (alpha + (1 - alpha) * (0.99F / clamp));
            }

            alpha = MathHelper.clamp_float(alpha, 0, 1);

            if (mc.thePlayer.inventory.getStackInSlot(0) != null) {
                if (mc.thePlayer.inventory.getStackInSlot(0).getItem() == Items.compass) {
                    if (mc.thePlayer.inventory.getStackInSlot(0).getDisplayName().contains("Teleporter")) {
                        target = null;
                    }
                }
            }

            if (targetsInRange() > 0) {
                targetList = getTargetsFromRange();
            } else {
                targetList = getEntityList();
            }

            if (target != null && !target.isEntityAlive()) {
                target = null;
            }

            if (targetList != null) {
                if (timerSwitch.delay(switchDelay)) {
                    switchAndReset();
                }
                try {
                    if (target != null) {
                        if (mc.thePlayer.getDistanceToEntity(target) > range() && targetsInRange() > 0) {
                            target = getTargetFromRange();
                        } else if (mc.thePlayer.getDistanceToEntity(target) > blockRange()) {
                            target = null;
                            switchAndReset();
                        } else if (!target.isEntityAlive() && ((EntityLivingBase) target).getHealth() <= 0) {
                            target = null;
                            switchAndReset();
                        } else if (mode().equalsIgnoreCase("Switch")) {
                            if (!targetList.isEmpty() && targetList.size() - 1 <= switchIndex) {
                                target = targetList.get(switchIndex);
                            }
                        }

                    } else {
                        if (!targetList.isEmpty() && (targetList.size() - 1 <= switchIndex && mode().equalsIgnoreCase("Switch"))) {
                            target = targetList.get(mode().equalsIgnoreCase("Switch") ? switchIndex : 0);
                        } else if(!targetList.isEmpty()){
                            target = targetList.get(0);
                        }
                    }
                } catch(Exception ignored){

                }

            }
        }
    }
    @SubscribeEvent
    public void onMotionUpdatePre2(CustomEventHandler.MotionChangeEvent.Pre event) {
        if (isEnable) {
            if (shouldBlock() || blocking) {
                sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                blocking = false;
            }

        }
    }
    @SubscribeEvent
    public void onMotionUpdatePre3(CustomEventHandler.MotionChangeEvent.Pre event) {
        if (isEnable) {
            if (shouldRotate()) {
                float frac = MathHelper.clamp_float(1f - smoothRotation / 100f, 0.1f, 1f);
                float[] rotations = Utils.getAngles(target);
                iyaw = iyaw + (rotations[0] - iyaw) * frac;
                ipitch = ipitch + (rotations[1] - ipitch) * frac;
                event.yaw = yaw = iyaw;
                event.pitch = pitch = ipitch;

                } else {
                    iyaw = mc.thePlayer.rotationYaw;
                    ipitch = mc.thePlayer.rotationPitch;
                }
        }
    }
    @SubscribeEvent
    public void onMotionUpdatePost(CustomEventHandler.MotionChangeEvent.Post event){
        if (isEnable) {
            if (shouldAttack() && !serverLag()) {
                attackEntity(target);
            }
            if (shouldBlock()) {
                interactAutoBlock();
                mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                blocking = true;
            }
        }
    }
    @SubscribeEvent
    public void onTick2(ClientTickEvent event){
        if(isEnable && mc.thePlayer != null){
            if(tickTimer + 1 >= 10){
                if(autoBlock) {
                    C08PacketPlayerBlockPlacement c08 = new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem());
                    Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(c08);
                }
                dispatchPackets();
                if(autoBlock) {
                    C07PacketPlayerDigging c07 = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) ? new BlockPos(-1, -1, -1) : BlockPos.ORIGIN, EnumFacing.DOWN);
                    Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(c07);
                }
                tickTimer = 0;
            } else {
                tickTimer = tickTimer + 1;
            }
        }
    }
    @SubscribeEvent
    public void onPacketSent(CustomEventHandler.PacketSentEvent event){
        if (isEnable){
            if (blinking) {
                if (event.packet instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer) event.packet;

                    if (packet.isMoving()) {
                        toDispatch.add(event.packet);
                        event.setCanceled(true);
                    }
                }

                if (event.packet instanceof C02PacketUseEntity) {
                    C02PacketUseEntity packet = (C02PacketUseEntity) event.packet;

                    if (packet.getAction().equals(C02PacketUseEntity.Action.ATTACK) && packet.getEntityFromWorld(mc.theWorld).equals(target)) {
                        toDispatch.add(event.packet);
                        event.setCanceled(true);
                        //dispatchPackets();
                        blinking = false;
                    }
                }

                if (toDispatch.size() > 114514) {
                    dispatchPackets();
                    NoticeManager.add(new Notice("Killaura :","PacketBuffer","Packet Stack Overflowed"));
                    blinking = false;
                }
            }

            if (shouldBlock() || blocking) {
                if (event.packet instanceof C07PacketPlayerDigging) {
                    C07PacketPlayerDigging packet = (C07PacketPlayerDigging) event.packet;

                    if (packet.getStatus().equals(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
                        event.setCanceled(true);
                    }
                }

                if (event.packet instanceof C08PacketPlayerBlockPlacement) {
                    C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.packet;

                    if (packet.getPlacedBlockDirection() == 255) {
                        event.setCanceled(true);
                    }
                }
            } else {
                lossTimer.reset();
            }
        }
    }
    @SubscribeEvent
    public void Render(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("Killaura")) {
            if ((boolean) getConfigByFunctionName.get("Killaura", "targetESP")) {
                if (target != null && !target.isDead && shouldAttack()) {
                    try {
                        Utils.RenderTargetESP((EntityLivingBase) target, new Color(251, 255, 25), 2.5F, (float) ((target.getEntityBoundingBox().maxY - target.getEntityBoundingBox().minY) / 4.5), currentHeight, RenderStatus);
                    } catch(Exception ignored){

                    }
                }
            }
        }
    }
    private void switchAndReset() {
        if(!targetList.isEmpty()) {
            switchIndex = (switchIndex + 1) % targetList.size();
        }
        timerSwitch.reset();
    }

    private void attackEntity(Entity target) {
        if (timerAttack.delay(1000L / cps)) {
            CustomEventHandler.AttackEvent event = new CustomEventHandler.AttackEvent();
            CustomEventHandler.EVENT_BUS.post(event);


            if (shouldBlock()) {
                sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                blocking = false;
            }

            mc.thePlayer.swingItem();
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
            mc.thePlayer.onEnchantmentCritical(target);

            timerAttack.reset();
        }
    }
    public float getIYaw() {
        return iyaw;
    }

    public float getIPitch() {
        return ipitch;
    }
    private boolean interactable(Block block) {
        return block == Blocks.chest || block == Blocks.trapped_chest || block == Blocks.crafting_table
                || block == Blocks.furnace || block == Blocks.ender_chest || block == Blocks.enchanting_table;
    }

    private void interactAutoBlock() {
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if (mc.objectMouseOver.entityHit != null) {
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.INTERACT));

            } else if (interactable(mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock())) {
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
                        mc.objectMouseOver.getBlockPos(), getFacingDirection(mc.objectMouseOver.getBlockPos()), mc.objectMouseOver.hitVec);
            }
        }
    }

    public boolean isValidEntity(Entity entity) {
        try {
            if(onlySword && (Minecraft.getMinecraft().thePlayer.getHeldItem() == null || !(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemSword))){
                return false;
            }
            if(isAutismShopKeeperCheck(entity)){
                return false;
            }
            if ((!(entity == Minecraft.getMinecraft().thePlayer) && !entity.isInvisible()) && !(entity instanceof EntityArmorStand) && !(entity instanceof EntityVillager) && !(entity instanceof EntityPigZombie && ((EntityLivingBase) entity).isChild()) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity)) && ((EntityLivingBase) entity).getHealth() > 0.0F && entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= (target != null && target != entity ? range : Math.max(rotationRange, range))) {
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

    private List<Entity> getEntityList() {
        List<Entity> list = mc.theWorld.getLoadedEntityList().stream().filter(this::isValidEntity).collect(Collectors.toList());
        list = list.stream().sorted(Comparator.comparing(o -> o.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toCollection(ObjectArrayList::new));
        return list;
    }

    private List<Entity> getTargetsFromRange() {
        return getEntityList().stream().filter(e -> mc.thePlayer.getDistanceToEntity(e) <= range()).collect(Collectors.toList());
    }

    private int targetsInRange() {
        int i = 0;

        if (getEntityList().isEmpty()) {
            return 0;

        } else {
            for (Entity entity : getEntityList()) {
                if (mc.thePlayer.getDistanceToEntity(entity) <= range()) {
                    i++;
                }
            }

            return i;
        }
    }

    private Entity getTargetFromRange() {
        for (Entity entity : getEntityList()) {
            if (mc.thePlayer.getDistanceToEntity(entity) <= range()) {
                return entity;
            }
        }

        return null;
    }
    private void dispatchPackets() {
        blinking = false;
        toDispatch.forEach(Utils::sendPacketNoEvent);
        toDispatch.clear();
    }

    public MovingObjectPosition getPosition() {
        float borderSize = target.getCollisionBorderSize();
        Vec3 positionEyes = Utils.getPositionEyes();
        Vec3 startVec = Utils.getVectorForRotation(getIPitch(), getIYaw());
        Vec3 endVec = positionEyes.addVector(startVec.xCoord * range(), startVec.yCoord * range(), startVec.zCoord * range());
        return target.getEntityBoundingBox().expand(borderSize, borderSize, borderSize).calculateIntercept(positionEyes, endVec);
    }

    private boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }
    public EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.UP;
        }

        MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                new Vec3((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D));
        return rayResult != null ? rayResult.sideHit : direction;
    }


    /*@SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    public void onMovePre(CustomEventHandler.MotionChangeEvent.Pre event) {
        if (FunctionManager.getStatus("Killaura")) {

            entityTarget = this.getTarget();
            if (entityTarget != null) {
                float[] angles = Utils.getServerAngles(entityTarget);
                event.yaw = ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - MathHelper.wrapAngleTo180_float((float) Math.max(-maxRotationYaw, Math.min(((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - angles[0], maxRotationYaw)));
                event.pitch = ((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - MathHelper.wrapAngleTo180_float((float) Math.max(-maxRotationPitch, Math.min(((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - angles[1], maxRotationPitch)));
            } else {
                entityTarget = null;
            }
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public void onMovePost(CustomEventHandler.MotionChangeEvent.Post event) {
        if (FunctionManager.getStatus("Killaura")) {
            if(onlySword && (Minecraft.getMinecraft().thePlayer.getHeldItem() == null || !(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemSword))){
                return;
            }
            if (entityTarget != null && Minecraft.getMinecraft().thePlayer.ticksExisted % 2 == 0) {
                Utils.updateItemNoEvent();
                if ((double) Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entityTarget) < range) {
                    if (Minecraft.getMinecraft().thePlayer.isUsingItem()) {
                        Minecraft.getMinecraft().playerController.onStoppedUsingItem(Minecraft.getMinecraft().thePlayer);
                    }

                    Minecraft.getMinecraft().thePlayer.swingItem();
                    float[] angles = Utils.getServerAngles(entityTarget);
                    if (Math.abs(((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedPitch() - angles[1]) < 25.0F && Math.abs(((EntityPlayerSPAccessor) Minecraft.getMinecraft().thePlayer).getLastReportedYaw() - angles[0]) < 15.0F) {
                        Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, entityTarget);
                    }

                    if (!Minecraft.getMinecraft().thePlayer.isUsingItem() && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock) {
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
            List<EntityLivingBase> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - (range * 2 / 2d), y - (range * 2 / 2d), z - (range * 2 / 2d), x + (range * 2 / 2d), y + (range * 2 / 2d), z + (range * 2 / 2d)), null);
            for (EntityLivingBase entity : entities) {
                if (isValid(entity)) {
                    return entity;
                }
            }
        }
        return null;
    }

    private boolean isValid(EntityLivingBase entity) {
        if ((!(entity == Minecraft.getMinecraft().thePlayer) && !entity.isInvisible()) && !(entity instanceof EntityArmorStand) && !(entity instanceof EntityVillager) && !(entity instanceof EntityPigZombie && entity.isChild()) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity)) && entity.getHealth() > 0.0F && entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= (entityTarget != null && entityTarget != entity ? range : Math.max(rotationRange, range)) && Utils.isWithinFOV(entity, fov + 5.0D) && Utils.isWithinPitch(entity, fov + 5.0D)) {
            if (entity instanceof EntityPlayer) {
                if (Utils.isTeamMember(entity, Minecraft.getMinecraft().thePlayer) && checkTeam) {
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
    }

    */
}