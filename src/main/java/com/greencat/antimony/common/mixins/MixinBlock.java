package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock {
    @Inject(method = "isCollidable", at = @At("HEAD"), cancellable = true)
    private void isCollidable(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if(Antimony.NoSaplingBound && (Block.getIdFromBlock((Block) (Object) this) == Block.getIdFromBlock(Blocks.sapling))){
            callbackInfoReturnable.setReturnValue(false);
        }
        if(Antimony.NoTreeBound && ((Block.getIdFromBlock((Block) (Object) this) == Block.getIdFromBlock(Blocks.leaves)) || (Block.getIdFromBlock((Block) (Object) this) == Block.getIdFromBlock(Blocks.leaves2)) || (Block.getIdFromBlock((Block) (Object) this) == Block.getIdFromBlock(Blocks.log)) || (Block.getIdFromBlock((Block) (Object) this) == Block.getIdFromBlock(Blocks.log2)))){
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}
