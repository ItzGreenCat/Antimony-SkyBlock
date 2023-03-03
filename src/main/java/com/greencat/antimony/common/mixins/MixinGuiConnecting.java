package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.utils.render.AnimationEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public abstract class MixinGuiConnecting extends GuiScreen {

    @Shadow
    private NetworkManager networkManager;


    @Inject(method = "connect", at = @At("HEAD"))
    public void injectConnect(String ip, int port, CallbackInfo ci) {
        Antimony.setLastServer(ip + ":" + port);
    }
    @Inject(method = "drawScreen", at = @At("HEAD"),cancellable = true)
    public void modifyRender(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci){
        this.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F,1.0F,1.0F);
        GlStateManager.enableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Antimony.MODID,"logo.png"));
        Gui.drawModalRectWithCustomSizedTexture((this.width / 2) - ((this.width / 12) / 2),(this.height / 4) + 120 + 12 - 20 - (this.width / 12),0,0,this.width / 12,this.width / 12,this.width / 12.0F,this.width / 12.0F);
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        for(GuiButton button : this.buttonList) {
            if(button.id == 0) {
                if (this.networkManager == null) {
                    button.displayString = I18n.format("connect.connecting") + " | " + I18n.format("gui.cancel");
                } else {
                    button.displayString = I18n.format("connect.authorizing") + " | " + I18n.format("gui.cancel");
                }
            }
        }
        super.drawScreen(p_drawScreen_1_,p_drawScreen_2_,p_drawScreen_3_);
        ci.cancel();
    }
}
