package com.greencat.antimony.common.mixins;

import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetworkDispatcher.class)
public class MixinNetworkDispatcher {
    @Redirect(method = "handleVanilla",at = @At(value = "INVOKE",target = "Lnet/minecraftforge/fml/common/FMLLog;info(Ljava/lang/String;[Ljava/lang/Object;)V"),remap = false)
    public void modifyLog(String format, Object[] data){
    }
}
