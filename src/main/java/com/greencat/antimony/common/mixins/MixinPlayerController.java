package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerController {
    @Shadow
    private ItemStack currentItemHittingBlock;
    @Shadow
    private BlockPos currentBlock;
    @Inject(method = { "attackEntity" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;addToSendQueue(Lnet/minecraft/network/Packet;)V") }, cancellable = true)
    public void attackEntity(EntityPlayer playerIn, Entity targetEntity, CallbackInfo ci) {
        CustomEventHandler.PreAttackEvent event = new CustomEventHandler.PreAttackEvent(targetEntity);
        CustomEventHandler.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
    @Inject(method = "isHittingPosition",at=@At("HEAD"),cancellable = true)
    public void updateNBTWithMining(BlockPos p_isHittingPosition_1_, CallbackInfoReturnable<Boolean> cir){
        if(FunctionManager.getStatus("NBTCanceller")){
            boolean flag;
            ItemStack currentItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
            flag = currentItemHittingBlock == null && currentItem == null;
            if (currentItemHittingBlock != null && currentItem != null) {
                if (currentItem.getTagCompound() != null) {
                        cir.setReturnValue(p_isHittingPosition_1_.equals(currentBlock) &&
                                currentItem.getItem() == currentItemHittingBlock.getItem());
                }
                flag = currentItem.getItem() == currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(currentItem, currentItemHittingBlock) && (currentItem.isItemStackDamageable() || currentItem.getMetadata() == currentItemHittingBlock.getMetadata());
            }
            cir.setReturnValue(p_isHittingPosition_1_.equals(currentBlock) && flag);
        }
    }
}
