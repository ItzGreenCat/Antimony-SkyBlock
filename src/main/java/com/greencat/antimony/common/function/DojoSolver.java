package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.type.Rotation;
import com.greencat.antimony.utils.Utils;
import me.greencat.lwebus.core.reflectionless.ReflectionlessEventHandler;
import me.greencat.lwebus.core.type.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import java.util.function.Predicate;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import java.util.Iterator;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.item.ItemBow;
import java.util.stream.Collectors;
import java.util.Comparator;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.item.EntityArmorStand;

import java.util.regex.Pattern;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.entity.Entity;
import java.util.HashMap;

import static com.greencat.antimony.core.config.ConfigInterface.get;

public class DojoSolver implements ReflectionlessEventHandler {
    private int jumpStage;
    private int ticks;
    private static boolean inTenacity;
    private static boolean inMastery;
    private static final HashMap<Entity, Long> shot;
    public DojoSolver() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    public void onPlayerUpdate(CustomEventHandler.MotionChangeEvent event) {
        if (FunctionManager.getStatus("DojoSolver")) {
            if ((Boolean) get("DojoSolver","mastery") && DojoSolver.inMastery && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() == Items.bow) {
                if (!Minecraft.getMinecraft().thePlayer.isUsingItem()) {
                    Minecraft.getMinecraft().playerController.sendUseItem((EntityPlayer)Minecraft.getMinecraft().thePlayer, (World)Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                }
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), true);
                Pattern pattern = Pattern.compile("\\d:\\d\\d\\d");
                Entity target = null;
                double time = 100.0;
                DojoSolver.shot.entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue() > 5000L);
                /*Pattern pattern2;*/
                final Iterator<Entity> iterator = Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(e -> e instanceof EntityArmorStand && getColor(e.getName()) && !DojoSolver.shot.containsKey(e) && pattern.matcher(e.getName()).find()).sorted(Comparator.comparingDouble(entity -> this.getPriority(ChatFormatting.stripFormatting(entity.getName())))).collect(Collectors.toList()).iterator();
                if (iterator.hasNext()) {
                    final Entity entity2 = iterator.next();
                    final Rotation rotation = Utils.getRotation(entity2.getPositionVector().addVector(0.0, 4.0, 0.0));
                    target = entity2;
                    time = this.getPriority(ChatFormatting.stripFormatting(entity2.getName()));
                    event.pitch = rotation.getPitch();
                    event.yaw = rotation.getYaw();
                }
                if (Minecraft.getMinecraft().thePlayer.isUsingItem() && Minecraft.getMinecraft().thePlayer.getItemInUse().getItem() == Items.bow && target != null && !event.isPre()) {
                    final ItemBow bow = (ItemBow)Minecraft.getMinecraft().thePlayer.getItemInUse().getItem();
                    final int i = bow.getMaxItemUseDuration(Minecraft.getMinecraft().thePlayer.getItemInUse()) - Minecraft.getMinecraft().thePlayer.getItemInUseCount();
                    float f = i / 20.0f;
                    f = (f * f + f * 2.0f) / 3.0f;
                    if (f >= (double) get("DojoSolver","bowChargeTime") && time <= (double) get("DojoSolver","time")) {
                        Minecraft.getMinecraft().playerController.onStoppedUsingItem((EntityPlayer)Minecraft.getMinecraft().thePlayer);
                        DojoSolver.shot.put(target, System.currentTimeMillis());
                    }
                }
            }
            if (event.isPre()) {
                if ((Boolean) get("DojoSolver","tenacity") && DojoSolver.inTenacity) {
                    if (this.jumpStage == 0) {
                        event.pitch = 90.0F;
                        if (Utils.isLiquid(0.01f) && Minecraft.getMinecraft().thePlayer.onGround) {
                            final MovingObjectPosition rayrace = Utils.rayTrace(0.0f, 90.0f, 4.5f);
                            if (rayrace != null) {
                                final int held = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                                Utils.swapToSlot(8);
                                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.getHeldItem()));
                                final Vec3 hitVec = rayrace.hitVec;
                                final BlockPos hitPos = rayrace.getBlockPos();
                                final float f2 = (float)(hitVec.xCoord - hitPos.getX());
                                final float f3 = (float)(hitVec.yCoord - hitPos.getY());
                                final float f4 = (float)(hitVec.zCoord - hitPos.getZ());
                                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(rayrace.getBlockPos(), rayrace.sideHit.getIndex(), Minecraft.getMinecraft().thePlayer.getHeldItem(), f2, f3, f4));
                                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket((Packet)new C0APacketAnimation());
                                Utils.swapToSlot(held);
                            }
                            Minecraft.getMinecraft().thePlayer.jump();
                            this.jumpStage = 1;
                        }
                    }
                    else if (this.jumpStage == 1) {
                        if (Utils.isLiquid(0.5f) && Minecraft.getMinecraft().thePlayer.motionY < 0.0) {
                            this.jumpStage = 2;
                        }
                    }
                    else if (this.jumpStage == 2) {
                        this.ticks %= 40;
                        ++this.ticks;
                        if (this.ticks == 40) {
                            double temp = event.y - 0.20000000298023224;
                            event.y = temp;
                        }
                        else if (this.ticks == 39) {
                            double temp = event.y - 0.10000000149011612;
                            event.y = temp;
                        }
                        else if (this.ticks == 38) {
                            double temp1 = event.y - 0.07999999821186066;
                            double temp2 = event.x + 0.20000000298023224;
                            double temp3 = event.z + 0.20000000298023224;
                            event.y = temp1;
                            event.x = temp2;
                            event.z  = temp3;
                        }
                    }
                }
                else {
                    final int n = 0;
                    this.jumpStage = n;
                    this.ticks = n;
                }
            }
        }
    }

    private double getPriority(String name) {
        double timeLeft = 100000.0;
        name = name.replaceAll(":", ".");
        timeLeft = Double.parseDouble(name);
        return timeLeft;
    }
    public void onMove(CustomEventHandler.CurrentPlayerMoveEvent event) {
        if (FunctionManager.getStatus("DojoSolver") && (Boolean) get("DojoSolver","tenacity") && DojoSolver.inTenacity && this.jumpStage == 2) {
            event.stop();
            Minecraft.getMinecraft().thePlayer.setVelocity(0.0, 0.0, 0.0);
        }
    }

    private static boolean getColor(final String name) {
        /*return name.startsWith("§c§l");*/
        /*return name.startsWith("§a§l");*/
        return name.startsWith("§e§l");
    }
    public void onBlockBounds(CustomEventHandler.BlockBoundsEvent event) {
        if (event.block == Blocks.lava && DojoSolver.inTenacity && FunctionManager.getStatus("DojoSolver") && (Boolean) get("DojoSolver","tenacity")) {
            event.aabb = new AxisAlignedBB((double)event.pos.getX(), (double)event.pos.getY(), (double)event.pos.getZ(), (double)(event.pos.getX() + 1), (double)(event.pos.getY() + 1), (double)(event.pos.getZ() + 1));
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("DojoSolver") && Minecraft.getMinecraft().theWorld != null) {
            DojoSolver.inTenacity = Utils.hasLine("Challenge: Tenacity");
            DojoSolver.inMastery = Utils.hasLine("Challenge: Mastery");
        }
    }
    public void onLeftClick(CustomEventHandler.PreAttackEvent event) {
        if (FunctionManager.getStatus("DojoSolver") && (Boolean) get("DojoSolver","swordSwap")) {
            this.left(event.entity);
        }
    }

    public void left(final Entity target) {
        if (Utils.hasLine("Challenge: Discipline") && target instanceof EntityZombie && ((EntityZombie)target).getCurrentArmor(3) != null) {
            final Item item = ((EntityZombie)target).getCurrentArmor(3).getItem();
            if (Items.leather_helmet.equals(item)) {
                this.pickItem(stack -> stack.getItem() == Items.wooden_sword);
            }
            else if (Items.golden_helmet.equals(item)) {
                this.pickItem(stack -> stack.getItem() == Items.golden_sword);
            }
            else if (Items.diamond_helmet.equals(item)) {
                this.pickItem(stack -> stack.getItem() == Items.diamond_sword);
            }
            else if (Items.iron_helmet.equals(item)) {
                this.pickItem(stack -> stack.getItem() == Items.iron_sword);
            }
        }
    }

    private void pickItem(final Predicate<ItemStack> predicate) {
        final int slot = Utils.getHotbar(predicate);
        if (slot != -1) {
            Utils.swapToSlot(slot);
        }
    }

    static {
        shot = new HashMap<Entity, Long>();
    }

    @Override
    public void invoke(Event event) {
        if(event instanceof CustomEventHandler.PreAttackEvent){
            onLeftClick((CustomEventHandler.PreAttackEvent) event);
            return;
        }
        if(event instanceof CustomEventHandler.CurrentPlayerMoveEvent){
            onMove((CustomEventHandler.CurrentPlayerMoveEvent) event);
            return;
        }
        if(event instanceof CustomEventHandler.BlockBoundsEvent){
            onBlockBounds((CustomEventHandler.BlockBoundsEvent) event);
            return;
        }
        if(event instanceof CustomEventHandler.MotionChangeEvent){
            onPlayerUpdate((CustomEventHandler.MotionChangeEvent) event);
        }
    }
}