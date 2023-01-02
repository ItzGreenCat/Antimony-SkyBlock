package com.greencat.antimony.common.mixins;

import com.greencat.antimony.common.function.FPSAccelerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLeavesBase.class)
public abstract class MixinLeavesBlock extends MixinBlock{
    @Inject(
            method = "shouldSideBeRendered",
            at = @At("HEAD"),
            cancellable = true
    )
    public void culling(IBlockAccess p_shouldSideBeRendered_1_, BlockPos p_shouldSideBeRendered_2_, EnumFacing p_shouldSideBeRendered_3_, CallbackInfoReturnable<Boolean> cir){
        if(FPSAccelerator.leaveCulling){
            cir.setReturnValue(false);
        }
    }
}
