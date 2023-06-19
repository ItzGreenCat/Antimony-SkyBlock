package com.greencat.antimony.common.mixins;

import com.greencat.antimony.common.Chat.CustomChatSend;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.ServerRotation;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.EnumAction;
import net.minecraft.util.MovementInput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    protected int sprintToggleTimer;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    public MovementInput movementInput;
    @Shadow
    public abstract boolean isSneaking();
    @Shadow
    protected abstract boolean isCurrentViewEntity();
    @Inject(method = "sendChatMessage",cancellable = true,at={@At("HEAD")})
    public void sendChatMessage(String p_sendChatMessage_1_, CallbackInfo cbi) {
        if(ConfigLoader.getChatChannel() && !p_sendChatMessage_1_.startsWith("/")){
            CustomChatSend.send("0|" + Minecraft.getMinecraft().thePlayer.getName() + "|" + p_sendChatMessage_1_);
            cbi.cancel();
        }
    }
    @ModifyVariable(method = "sendChatMessage",at = @At("HEAD"),ordinal = 0,argsOnly = true)
    public String sendChatMessage(@NotNull String value){
        String message = value;
        message = message.replace("who asked","Hold on i gotta get more Magic Find So i can find who asked.");
        if(FunctionManager.getStatus("ChatSuffix") && !message.startsWith("/")){
            message = message + ConfigInterface.get("ChatSuffix","content");
        }
        return message;
    }
    //private static PlayerState stateCache;
    private static CustomEventHandler.MotionChangeEvent eventCache;
    @Inject(
            method = "onUpdateWalkingPlayer",
            at=@At("HEAD"),
            cancellable = true)
    public void onUpdateHead(CallbackInfo ci){
        CustomEventHandler.MotionChangeEvent event = new CustomEventHandler.MotionChangeEvent.Pre(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround, this.isSprinting(), this.isSneaking(),true);
        CustomEventHandler.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
        eventCache = event;
        //stateCache = new PlayerState(this.posX,this.posY,this.posZ,this.rotationYaw,this.rotationPitch,this.isSneaking(),this.isSprinting());
        this.posX = event.x;
        this.posY = event.y;
        this.posZ = event.z;

        /*this.rotationYaw = event.yaw;
        this.rotationPitch = event.pitch;*/
        ServerRotation.usingServerRotation = true;
        ServerRotation.yaw = event.yaw;
        ServerRotation.pitch = event.pitch;

        this.movementInput.sneak = event.sneaking;
        this.setFlag(3,event.sprinting);

    }
    @Inject(
            method = "onUpdateWalkingPlayer",
            at=@At("RETURN")
    )
    public void onUpdatePost(CallbackInfo ci) {
        ServerRotation.usingServerRotation = false;
        this.lastReportedPosX = eventCache.x;
        this.lastReportedPosY = eventCache.y;
        this.lastReportedPosZ = eventCache.z;

        this.lastReportedYaw = eventCache.yaw;
        this.lastReportedPitch = eventCache.pitch;
        CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.MotionChangeEvent.Post(eventCache));
    }
    /**
     * @author __GreenCat__
     * @reason killaura dont need this
     */
    /*
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
     */
    @Redirect(
            method = {"onLivingUpdate"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"
            )
    )
    public boolean isUsingItem(EntityPlayerSP instance) {
        return (!FunctionManager.getStatus("NoSlow") || !instance.isUsingItem()) && instance.isUsingItem();
    }
    @Inject(
            method = {"onLivingUpdate"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/AbstractClientPlayer;onLivingUpdate()V"
            )}
    )
    public void onLivingUpdate(CallbackInfo ci) {
        try {
            if (FunctionManager.getStatus("NoSlow") && this.isUsingItem()) {
                MovementInput movement;
                if (this.getItemInUse().getItem().getItemUseAction(this.getItemInUse()) == EnumAction.BLOCK) {
                    movement = this.movementInput;
                    movement.moveForward = (float) ((double) movement.moveForward * (Double) ConfigInterface.get("NoSlow","sword"));
                    movement = this.movementInput;
                    movement.moveStrafe = (float) ((double) movement.moveStrafe * (Double) ConfigInterface.get("NoSlow","sword"));
                } else if (this.getItemInUse().getItem().getItemUseAction(this.getItemInUse()) == EnumAction.BOW) {
                    movement = this.movementInput;
                    movement.moveForward = (float) ((double) movement.moveForward * (Double) ConfigInterface.get("NoSlow","bow"));
                    movement = this.movementInput;
                    movement.moveStrafe = (float) ((double) movement.moveStrafe * (Double) ConfigInterface.get("NoSlow","bow"));
                } else if (this.getItemInUse().getItem().getItemUseAction(this.getItemInUse()) != EnumAction.NONE) {
                    movement = this.movementInput;
                    movement.moveForward = (float) ((double) movement.moveForward * (Double) ConfigInterface.get("NoSlow","eat"));
                    movement = this.movementInput;
                    movement.moveStrafe = (float) ((double) movement.moveStrafe * (Double) ConfigInterface.get("NoSlow","eat"));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    @Inject(
            method = {"onLivingUpdate"},
            at = {@At(
                    value = "HEAD"
            )}
    )
    public void callEvent(CallbackInfo ci){
        CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.PlayerUpdateEvent());
    }
    @Inject(
            method = {"onLivingUpdate"},
            at = {@At(
                    value = "RETURN"
            )}
    )
    public void callEvent2(CallbackInfo ci){
        CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.PlayerUpdateEvent.Post());
    }
}
