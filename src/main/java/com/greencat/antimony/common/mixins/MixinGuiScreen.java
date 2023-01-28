package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.inputfix.GuiScreenFix;
import com.greencat.antimony.utils.Blur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui {
    Blur blur = new Blur();
    @Shadow
    public Minecraft mc;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Inject(
            method="drawDefaultBackground",
            at=@At(
                    value = "HEAD"
            ),
            cancellable = true)
    public void defaultBackground(CallbackInfo cib){
        try {
            if (this.mc.theWorld != null) {
                if((Integer) getConfigByFunctionName.get("Interface","bgstyle") == 0) {
                    this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
                }
                if((Integer) getConfigByFunctionName.get("Interface","bgstyle") == 1) {
                    Blur.renderBlur(0, 0, this.width, this.height, 20);
                }
            } else {
                ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/CustomUI/defaultBackground.png"));
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            }
            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.BackgroundDrawnEvent((GuiScreen) (Object) this));
        } catch (Exception e){
            e.printStackTrace();
        }
        cib.cancel();
    }
    @Inject(method = "handleKeyboardInput",at=@At("HEAD"),cancellable = true)
    public void inputFixMixin(CallbackInfo ci){
        if(FunctionManager.getStatus("InputFix")){
            GuiScreenFix.handleKeyboardInput((GuiScreen)(Object)this);
            ci.cancel();
        }
    }
}
