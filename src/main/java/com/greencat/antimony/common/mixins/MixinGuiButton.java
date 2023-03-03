package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.utils.render.AnimationEngine;
import com.greencat.antimony.utils.sound.SoundPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiButton.class)
public abstract class MixinGuiButton extends Gui{
    @Shadow
    public String displayString;

    public boolean prevSelect = false;
    @Shadow public boolean visible;

    @Shadow public int xPosition;

    @Shadow public int width;

    @Shadow public int yPosition;

    @Shadow public int height;

    @Shadow public boolean enabled;

    @Shadow protected boolean hovered;

    @Shadow(remap = false) public int packedFGColour;

    @Shadow protected abstract int getHoverState(boolean p_getHoverState_1_);

    @Shadow protected abstract void mouseDragged(Minecraft p_mouseDragged_1_, int p_mouseDragged_2_, int p_mouseDragged_3_);

    @Inject(method = "playPressSound",at = @At("HEAD"),cancellable = true)
    public void modifySound(SoundHandler p_playPressSound_1_, CallbackInfo ci){
        SoundPlayer.play(new ResourceLocation(displayString.contains("Done") || displayString.contains("Cancel") || displayString.contains("Save") || displayString.contains("完成") || displayString.contains("取消") || displayString.contains("保存") ? "antimony:button_back" : "antimony:button_press"));
        ci.cancel();
    }
    AnimationEngine animation = new AnimationEngine(0,0);
    @Inject(method = "drawButton",at = @At("HEAD"),cancellable = true)
    public void modifyRender(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_, CallbackInfo ci){
        if(this.visible){
            FontRenderer fontrenderer = p_drawButton_1_.fontRendererObj;
            this.hovered = p_drawButton_2_ >= this.xPosition && p_drawButton_3_ >= this.yPosition && p_drawButton_2_ < this.xPosition + this.width && p_drawButton_3_ < this.yPosition + this.height;
            this.mouseDragged(p_drawButton_1_, p_drawButton_2_, p_drawButton_3_);
            Gui.drawRect(this.xPosition,this.yPosition,this.xPosition + this.width,this.yPosition+ + this.height,this.enabled ? new Color(168,239,255,120).getRGB() : new Color(64,64,64,120).getRGB());
            Gui.drawRect(this.xPosition + this.width / 2,this.yPosition + this.height - 2, (int) (this.xPosition + this.width / 2 + animation.xCoord),this.yPosition + this.height,new Color(175,220,251).getRGB());
            Gui.drawRect((int) (this.xPosition + this.width / 2 - animation.xCoord),this.yPosition + this.height - 2,this.xPosition + this.width / 2,this.yPosition + this.height,new Color(175,220,251).getRGB());
            if(hovered != prevSelect){
                prevSelect = hovered;
                if(hovered){
                    animation.moveTo((int) (this.width / 2.0F),0,0.5);
                } else {
                    animation.moveTo(0,0,0.5);
                }
            }
            int j = 14737632;
            if (this.packedFGColour != 0) {
                j = this.packedFGColour;
            } else if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            ci.cancel();
        }
    }
}
