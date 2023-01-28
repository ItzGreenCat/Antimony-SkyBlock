package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.common.Via;
import com.greencat.antimony.common.function.FrozenScytheAura;
import com.greencat.antimony.common.function.SynthesizerAura;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.common.function.Killaura;
import com.greencat.antimony.common.function.ShortBowAura;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Shadow
    private Entity renderViewEntity;
    @Shadow
    private int leftClickCounter;
    @Shadow
    public MovingObjectPosition objectMouseOver;
    @Shadow
    public WorldClient theWorld;
    @Inject(
            method = "createDisplay",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V",
                    remap = false
            )
    )
    private void inject_createDisplay(CallbackInfo cbi) {
        Display.setTitle("Minecraft 1.8 Running on MS-DOS");
    }
    @Inject(
            method = {"getRenderViewEntity"},
            at = {@At("HEAD")}
    )
    public void getRenderViewEntity(CallbackInfoReturnable<Entity> cir) {
        try {
            if ((FunctionManager.getStatus("Killaura") || FunctionManager.getStatus("ShortBowAura") || FunctionManager.getStatus("SynthesizerAura")) || FunctionManager.getStatus("FrozenScytheAura") && this.renderViewEntity != null && this.renderViewEntity == Minecraft.getMinecraft().thePlayer) {
                if (Killaura.target != null || ShortBowAura.target != null || SynthesizerAura.entityTarget != null || FrozenScytheAura.target != null) {
                    ((EntityLivingBase) this.renderViewEntity).rotationYawHead = ((EntityPlayerSPAccessor) this.renderViewEntity).getLastReportedYaw();
                    ((EntityLivingBase) this.renderViewEntity).renderYawOffset = ((EntityPlayerSPAccessor) this.renderViewEntity).getLastReportedYaw();
                }
            }
        } catch(Exception ignored){

        }
    }
    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectConstructor(GameConfiguration p_i45547_1_, CallbackInfo ci) {
        Via.init();
        try {
            Antimony.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Inject(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovingObjectPosition;getBlockPos()Lnet/minecraft/util/BlockPos;"))
    public void onClickMouse(boolean p_sendClickBlockToController_1_, CallbackInfo ci){
        if (this.leftClickCounter == 0 && theWorld.getBlockState(objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air) {
            CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.ClickBlockEvent(objectMouseOver.getBlockPos(), this.objectMouseOver.sideHit));
        }
    }
    @Inject(method = "shutdownMinecraftApplet",at=@At("HEAD"))
    public void modifyShutdown(CallbackInfo ci){
        Antimony.onMinecraftShutdown();
    }
}