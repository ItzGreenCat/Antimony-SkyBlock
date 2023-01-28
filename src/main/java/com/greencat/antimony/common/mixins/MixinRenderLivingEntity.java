package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRenderLivingEntity<T extends EntityLivingBase> extends Render<T> {
    protected MixinRenderLivingEntity(RenderManager p_i46179_1_) {
        super(p_i46179_1_);
    }
    @Inject(method = "doRender*",
    at = @At("HEAD"),cancellable = true)
    public void modifyRender(T p_doRender_1_, double p_doRender_2_, double p_doRender_4_, double p_doRender_6_, float p_doRender_8_, float p_doRender_9_,CallbackInfo ci){
        if(FunctionManager.getStatus("FPS Accelerator") && (Boolean)getConfigByFunctionName.get("FPS Accelerator","entityCulling") && Minecraft.getMinecraft().thePlayer != null && !Minecraft.getMinecraft().thePlayer.canEntityBeSeen(p_doRender_1_)){
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(p_doRender_1_, (RendererLivingEntity<T>)(Object)this, p_doRender_2_, p_doRender_4_, p_doRender_6_));
            super.doRender(p_doRender_1_, p_doRender_2_, p_doRender_4_, p_doRender_6_, p_doRender_8_, p_doRender_9_);
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(p_doRender_1_, (RendererLivingEntity<T>)(Object)this, p_doRender_2_, p_doRender_4_, p_doRender_6_));
            ci.cancel();
        }
    }
}
