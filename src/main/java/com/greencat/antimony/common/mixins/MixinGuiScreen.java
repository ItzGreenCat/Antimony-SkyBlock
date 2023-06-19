package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.inputfix.GuiScreenFix;
import com.greencat.antimony.utils.Blur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
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
    @Shadow
    public Minecraft mc;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public abstract void drawWorldBackground(int p_drawBackground_1_);
    //AnimationEngine animation = new AnimationEngine(0,0);
    @Inject(
            method="drawDefaultBackground",
            at=@At(
                    value = "HEAD"
            ),
            cancellable = true)
    public void defaultBackground(CallbackInfo cib){
        try {
            if (this.mc.theWorld != null) {
                if((Integer) ConfigInterface.get("Interface","bgstyle") == 0) {
                    this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
                }
                if((Integer) ConfigInterface.get("Interface","bgstyle") == 1) {
                    Blur.renderBlur(0, 0, this.width, this.height, 20);
                }
                GlStateManager.pushMatrix();
                GlStateManager.enableAlpha();
                int height = (int) (this.height / 12.0D);
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/CustomUI/title.png"));
                Gui.drawModalRectWithCustomSizedTexture(0,this.height - height,0,0,height * 5,height,height * 5,height);
                GlStateManager.popMatrix();
            } else {
                drawWorldBackground(0);
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
    @Inject(method = "drawScreen",at=@At("HEAD"))
    public void getMouseLocation(int x, int y, float delta, CallbackInfo ci){
        Antimony.mouseX = x;
        Antimony.mouseY = y;
    }
    @Inject(method = "drawBackground",at=@At("HEAD"), cancellable = true)
    public void modifyBackground(int p_drawBackground_1_, CallbackInfo ci){
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/CustomUI/defaultBackground.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, (int) (this.width * 0.5625F), this.width, this.width * 0.5625F);
        ci.cancel();
    }
}
