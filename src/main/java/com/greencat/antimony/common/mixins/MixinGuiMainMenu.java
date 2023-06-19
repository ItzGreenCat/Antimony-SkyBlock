package com.greencat.antimony.common.mixins;

import com.greencat.antimony.utils.render.AnimationEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Random;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    int background = 1;
    private static long lastSwitch = 0L;
    Random random = new Random();
    @Inject(
            method = "initGui",
            at = @At(
                    value = "RETURN"
            )
    )
    public void modifyInit(CallbackInfo ci){
        if(System.currentTimeMillis() - lastSwitch > 600000) {
            background = random.nextInt(3) + 1;
            lastSwitch = System.currentTimeMillis();
        }
    }
    @Inject(
            method = "drawScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V"
            ),
            cancellable = true)
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci){
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/CustomUI/MainMenuBackground/bg" + background + ".png"));
        Gui.drawModalRectWithCustomSizedTexture(0,0,0,0,scaledResolution.getScaledWidth(),scaledResolution.getScaledHeight(),scaledResolution.getScaledWidth(),scaledResolution.getScaledHeight());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int ScreenWidth = scaledResolution.getScaledWidth();
        int ScreenHeight = scaledResolution.getScaledHeight();
        int yCoord = (int) (ScreenHeight / 20 * 4.5D);
        int xCoord = ScreenWidth / 12;
        int height = (int) (ScreenHeight / 9.0D);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/CustomUI/title.png"));
        Gui.drawModalRectWithCustomSizedTexture(xCoord,yCoord,0,0,height * 5,height,height * 5,height);

        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        ci.cancel();
    }
}
