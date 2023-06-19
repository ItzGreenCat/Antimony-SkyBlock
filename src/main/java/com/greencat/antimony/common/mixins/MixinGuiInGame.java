package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Blur;
import com.greencat.antimony.utils.render.AnimationEngine;
import com.greencat.antimony.utils.render.HotBarShadow;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.greencat.antimony.core.config.ConfigInterface;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiIngame.class)
public class MixinGuiInGame extends Gui {
    private int prevLocation = 0;
    private final AnimationEngine animation = new AnimationEngine(0,0);
    @Redirect(method = "renderTooltip",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/GuiIngame;drawTexturedModalRect(IIIIII)V",ordinal = 0))
    public void modifyRenderBorder(GuiIngame instance, int x, int y, int u, int v, int w, int h){
        int mode = (Integer) ConfigInterface.get("Interface","hotbar");
        if(mode == 0){
            this.drawTexturedModalRect(x,y, u, v, w,h);
        }
        if(mode == 1){
            Blur.renderBlur(x,y,w,h,10);
            /*HotBarShadow.x = x;
            HotBarShadow.y = y;
            HotBarShadow.w = w;
            HotBarShadow.h = h;*/
        }
        if(mode == 2){
            Gui.drawRect(x,y,x + w,y + h, Color.BLACK.getRGB());
        }
    }
    @Redirect(method = "renderTooltip",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/GuiIngame;drawTexturedModalRect(IIIIII)V",ordinal = 1))
    public void modifyRenderSelected(GuiIngame instance, int x, int y, int u, int v, int w, int h){
        if(prevLocation != x){
            if((Boolean) ConfigInterface.get("Interface","HotBarAnimation")) {
                animation.moveTo(x, 0, 0.25);
            } else {
                animation.xCoord = x;
                animation.targetX = x;
            }
            prevLocation = x;
        }
        int mode = (Integer) ConfigInterface.get("Interface","hotbar");
        if(mode == 0){
            this.drawTexturedModalRect((float) animation.xCoord,y, u, v, w,h);
        }
        if(mode == 1){
            Gui.drawRect((int) animation.xCoord + 1,y + 1, (int) ((float) animation.xCoord + w) - 1,y + h,new Color(154, 154, 154, 143).getRGB());
        }
        if(mode == 2){
            Gui.drawRect((int) animation.xCoord + 1,y + 1, (int) ((float) animation.xCoord + w) - 1,y + h,new Color(154, 154, 154).getRGB());
        }
    }
    @Inject(method = "renderTooltip", at = @At(value = "RETURN"))
    public void onRenderTooltip(ScaledResolution p_renderTooltip_1_, float partialTicks, CallbackInfo ci){
        CustomEventHandler.EVENT_BUS.post(new CustomEventHandler.ScreenRender2DEvent(partialTicks));
    }
}
