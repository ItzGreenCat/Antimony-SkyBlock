package com.greencat.antimony.common.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface PlayerControllerAccessor {
    @Accessor("currentPlayerItem")
    void setSlot(int i);
    @Accessor("currentPlayerItem")
    int getSlot();
}
