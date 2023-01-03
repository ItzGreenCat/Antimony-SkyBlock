package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.S08PacketPlayerPosLookAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.timer.SystemTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import static com.greencat.antimony.core.config.getConfigByFunctionName.get;

public class Disabler {
    private final LinkedBlockingQueue<Packet<INetHandlerPlayServer>> packets = new LinkedBlockingQueue<Packet<INetHandlerPlayServer>>();
    Minecraft mc = Minecraft.getMinecraft();
    private int counter = 0;
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private final SystemTimer timerCancelDelay = new SystemTimer();
    private final SystemTimer timerCancelTimer = new SystemTimer();
    private boolean timerShouldCancel = true;
    private boolean inCage = true;
    private boolean isEnable = false;
    private boolean noC03 = false;
    private boolean antiDogBan = false;
    private boolean C00Disabler = false;
    private boolean C0BDisabler = false;
    private boolean banWarning = false;
    private boolean timerDisabler = false;
    private int strafePackets = 70;
    private boolean strafeDisabler = false;

    public Disabler() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event) {
        if (event.function.getName().equals("Disabler")) {
            init();
        }
    }

    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event) {
        if (event.function.getName().equals("Disabler") && event.status) {
            init();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("Disabler")) {
            isEnable = true;
            banWarning = (Boolean) get("Disabler", "banWarning");
            timerDisabler = (Boolean) get("Disabler", "timer");
            strafePackets = (Integer) get("Disabler", "strafePackets");
            noC03 = (Boolean) get("Disabler", "noC03");
            strafeDisabler = (Boolean) get("Disabler", "strafeDisabler");
            antiDogBan = (Boolean) get("Disabler", "antiWatchdog");
            C00Disabler = (Boolean) get("Disabler", "C00Disabler");
            C0BDisabler = (Boolean) get("Disabler", "C0BDisabler");
        } else {
            isEnable = false;
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        if (isEnable) {
            counter = 0;
            inCage = true;
        }
    }

    @SubscribeEvent
    public void onPacket(CustomEventHandler.PacketEvent event) throws InterruptedException {
        if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            if (isEnable) {
                Packet<?> packet = event.packet;

                if (banWarning && packet instanceof S02PacketChat && ((S02PacketChat) packet).getChatComponent().getUnformattedText().toLowerCase().contains(("Cages opened!".toLowerCase()))) {
                    Utils.print("[Disabler] 请暂停使用功能,已被标记");
                    inCage = false;
                }

                if (mc.thePlayer.ticksExisted > 200f)
                    inCage = false;

                if (timerDisabler && !inCage) {
                    if (packet instanceof C02PacketUseEntity || packet instanceof C03PacketPlayer || packet instanceof C07PacketPlayerDigging || packet instanceof C08PacketPlayerBlockPlacement ||
                            packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction && mc.thePlayer.ticksExisted > strafePackets) {
                        if (timerShouldCancel) {
                            if (!timerCancelTimer.hasTimePassed(350L)) {
                                Packet temp = packet;
                                packets.add(temp);
                                event.setCanceled(true);
                            } else {
                                timerShouldCancel = false;
                                while (!packets.isEmpty()) {
                                    Utils.sendPacketNoEvent(packets.take());
                                }
                            }
                            if (mc.thePlayer.isUsingItem() || (FunctionManager.getStatus("Killaura") && Killaura.autoBlock && Minecraft.getMinecraft().thePlayer.isBlocking()) && mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                                timerShouldCancel = false;
                                while (!packets.isEmpty()) {
                                    Utils.sendPacketNoEvent(packets.take());
                                }
                            }
                        }
                    }
                }

                if (packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && noC03) {
                    event.setCanceled(true);
                }

                if (strafeDisabler && (mc.thePlayer.ticksExisted < strafePackets) && packet instanceof C03PacketPlayer && (mc.thePlayer.ticksExisted % 15 != 0)) {
                    event.setCanceled(true);
                }
                if (antiDogBan || (strafeDisabler && (mc.thePlayer.ticksExisted < strafePackets))) {
                    if (event.packet instanceof C03PacketPlayer.C06PacketPlayerPosLook && mc.thePlayer.onGround && mc.thePlayer.fallDistance > 10) {
                        if (counter > 0) {
                            if (((C03PacketPlayer.C06PacketPlayerPosLook) event.packet).getPositionX() == x && ((C03PacketPlayer.C06PacketPlayerPosLook) event.packet).getPositionY() == y && ((C03PacketPlayer.C06PacketPlayerPosLook) event.packet).getPositionZ() == z) {
                                mc.getNetHandler().getNetworkManager().sendPacket(
                                        new C03PacketPlayer.C04PacketPlayerPosition(
                                                ((C03PacketPlayer.C06PacketPlayerPosLook) event.packet).getPositionX(),
                                                ((C03PacketPlayer.C06PacketPlayerPosLook) event.packet).getPositionY(),
                                                ((C03PacketPlayer.C06PacketPlayerPosLook) event.packet).getPositionZ(),
                                                ((C03PacketPlayer.C06PacketPlayerPosLook) event.packet).isOnGround()
                                        )
                                );
                                event.setCanceled(true);
                            }
                        }
                        counter += 1;

                        if (event.packet instanceof C03PacketPlayer.C05PacketPlayerLook && mc.thePlayer.isRiding()) {
                            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                        } else if (event.packet instanceof C0CPacketInput && mc.thePlayer.isRiding()) {
                            mc.getNetHandler().getNetworkManager().sendPacket(event.packet);
                            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                            event.setCanceled(true);
                        }
                    }

                    if (event.packet instanceof S08PacketPlayerPosLook) {
                        Packet<?> s08 = event.packet;
                        x = ((S08PacketPlayerPosLookAccessor) s08).getX();
                        y = ((S08PacketPlayerPosLookAccessor) s08).getY();
                        z = ((S08PacketPlayerPosLookAccessor) s08).getZ();
                    }

                    if (event.packet instanceof S07PacketRespawn) {
                        counter = 0;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(CustomEventHandler.PlayerUpdateEvent event) {
        if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            if (isEnable) {
                if (timerDisabler) {
                    if (timerCancelDelay.hasTimePassed(5000L)) {
                        timerShouldCancel = true;
                        timerCancelTimer.reset();
                        timerCancelDelay.reset();
                    }
                }
                boolean isBlockUnder;
                isBlockUnder = isBlockUnder();
                if (C00Disabler) {
                    if (mc.thePlayer.onGround && isBlockUnder && mc.thePlayer.fallDistance > 10) {
                        mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive(new Random().nextInt(1001) - 1));
                    }
                }
                if (C0BDisabler) {
                    if (mc.thePlayer.ticksExisted % 180 == 90) {
                        if (mc.thePlayer.onGround && mc.thePlayer.fallDistance > 10) {
                            mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive(new Random().nextInt(1001) - 1));
                            mc.timer.timerSpeed = 0.8f;
                        } else {
                            if (mc.thePlayer.fallDistance < 10) {
                                if (mc.thePlayer.posY == ((double) mc.thePlayer.fallDistance)) {
                                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(false));
                                    if (mc.thePlayer.onGround) mc.timer.timerSpeed = 0.4f;
                                    if (mc.thePlayer.fallDistance == 0f)
                                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void init() {
        counter = 0;
        inCage = true;
        x = 0.0;
        y = 0.0;
        z = 0.0;
        timerCancelDelay.reset();
        timerCancelTimer.reset();
    }

    private boolean isBlockUnder() {
        if (mc.thePlayer.posY < 0) {
            return false;
        }
        int off = 0;
        while (off < ((int) mc.thePlayer.posY) + 2) {
            AxisAlignedBB aabb = mc.thePlayer.getEntityBoundingBox()
                    .offset(0.0, -off, 0.0);
            if (!(mc.theWorld.getCollidingBoundingBoxes(
                    mc.thePlayer,
                    aabb
            ).isEmpty())
            ) {
                return true;
            }
            off += 2;
        }
        return false;
    }
}
