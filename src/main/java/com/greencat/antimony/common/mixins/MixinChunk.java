package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Chunk.class})
public class MixinChunk {
    @Inject(
            method = {"setBlockState"},
            at = {@At("HEAD")}
    )
    private void onBlockChange(BlockPos pos, IBlockState state, CallbackInfoReturnable<IBlockState> cir) {
        CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.BlockChangeEvent(pos, state));
    }
}
