package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import com.greencat.antimony.core.via.protocol.ProtocolCollection;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    public void injectInitGui(CallbackInfo ci) {
        buttonList.add(new GuiButton(114514, 5, 38, 98, 20,
                ProtocolCollection.getProtocolById(Antimony.getVersion()).getName()));
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void injectActionPerformed(GuiButton p_actionPerformed_1_, CallbackInfo ci) {
        if (p_actionPerformed_1_.id == 114514){
            if(Antimony.versionIndex + 1 > ProtocolCollection.values().length - 1){
                Antimony.versionIndex = 0;
            } else {
                Antimony.versionIndex = Antimony.versionIndex + 1;
            }
            Antimony.setVersion(ProtocolCollection.values()[Antimony.versionIndex].getVersion().getVersion());
        }
    }
    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void injectDrawScreenHead(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci) {
        for(GuiButton button : this.buttonList){
            if(button.id == 114514){
                button.displayString = ProtocolCollection.getProtocolById(Antimony.getVersion()).getName();
            }
        }
    }
    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void injectDrawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci) {
        mc.fontRendererObj.drawStringWithShadow("<-- 当前版本",
                104, 44, -1);
    }
}
