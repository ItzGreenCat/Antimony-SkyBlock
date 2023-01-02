package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinEntityRendererDispatcher {
    @Inject(
            method = "renderTileEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render(TileEntity entity, float p_renderTileEntity_2_, int p_renderTileEntity_3_, CallbackInfo ci){
        CustomEventHandler.RenderTileEntityPreEvent event = new CustomEventHandler.RenderTileEntityPreEvent(entity);
        CustomEventHandler.EVENT_BUS.post(event);
        if(event.isCanceled()){
            ci.cancel();
        }
    }
}
