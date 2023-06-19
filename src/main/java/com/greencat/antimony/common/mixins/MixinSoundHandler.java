package com.greencat.antimony.common.mixins;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SoundHandler.class)
public class MixinSoundHandler {
    @Redirect(method = "update",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/audio/SoundManager;updateAllSounds()V"))
    public void onUpdate(SoundManager instance){
        try{
            instance.updateAllSounds();
        } catch(Exception ignored){}
    }
}
