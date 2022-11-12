package com.greencat.antimony.common.mixins;

import com.greencat.antimony.common.Chat.SendToServer;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MovementInput;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityPlayerSP.class},priority = 1)
public abstract class MixinEntityPlayerSP extends MixinEntityPlayer {
    @Shadow
    @Final
    public NetHandlerPlayClient sendQueue;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private float lastReportedPitch;
    @Shadow
    private double lastReportedPosX;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private int positionUpdateTicks;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    public abstract boolean isSneaking();
    @Shadow
    protected abstract boolean isCurrentViewEntity();
    @Inject(method = "sendChatMessage",cancellable = true,at={@At("HEAD")})
    public void sendChatMessage(String p_sendChatMessage_1_, CallbackInfo cbi) {
        if(ConfigLoader.getChatChannel() && !p_sendChatMessage_1_.startsWith("/")){
            SendToServer sendToServer = new SendToServer();
            sendToServer.send(Minecraft.getMinecraft().thePlayer.getName() + "MSG-!-SPLIT" + p_sendChatMessage_1_);
            cbi.cancel();
        }
    }
    /**
     * @author __GreenCat__
     * @reason killaura need this
     */
    @Overwrite
    public void onUpdateWalkingPlayer() {
        CustomEventHandler.MotionChangeEvent event = new CustomEventHandler.MotionChangeEvent.Pre(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround, this.isSprinting(), this.isSneaking());
        if (!CustomEventHandler.EVENT_BUS.post(event)) {
            boolean flag = event.sprinting;
            if (flag != this.serverSprintState) {
                if (flag) {
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                } else {
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }

                this.serverSprintState = flag;
            }

            boolean flag1 = event.sneaking;
            if (flag1 != this.serverSneakState) {
                if (flag1) {
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                } else {
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                }

                this.serverSneakState = flag1;
            }

            if (this.isCurrentViewEntity()) {
                double d0 = event.x - this.lastReportedPosX;
                double d1 = event.y - this.lastReportedPosY;
                double d2 = event.z - this.lastReportedPosZ;
                double d3 = (double)(event.yaw - this.lastReportedYaw);
                double d4 = (double)(event.pitch - this.lastReportedPitch);
                boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
                boolean flag3 = d3 != 0.0D || d4 != 0.0D;
                if (this.ridingEntity == null) {
                    if (flag2 && flag3) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround));
                    } else if (flag2) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y, event.z, event.onGround));
                    } else if (flag3) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(event.yaw, event.pitch, event.onGround));
                    } else {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer(event.onGround));
                    }
                } else {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, event.yaw, event.pitch, event.onGround));
                    flag2 = false;
                }

                ++this.positionUpdateTicks;
                if (flag2) {
                    this.lastReportedPosX = event.x;
                    this.lastReportedPosY = event.y;
                    this.lastReportedPosZ = event.z;
                    this.positionUpdateTicks = 0;
                }

                Utils.lastReportedPitch = this.lastReportedPitch;
                if (flag3) {
                    this.lastReportedYaw = event.yaw;
                    this.lastReportedPitch = event.pitch;
                }
            }

            CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.MotionChangeEvent.Post(event));
        }
    }
}
