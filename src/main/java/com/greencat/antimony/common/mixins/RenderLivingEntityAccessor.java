package com.greencat.antimony.common.mixins;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RendererLivingEntity.class)
public interface RenderLivingEntityAccessor {
    @Accessor("renderOutlines")
    void setOutline(boolean outline);
}
