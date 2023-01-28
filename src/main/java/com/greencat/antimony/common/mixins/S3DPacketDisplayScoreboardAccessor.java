package com.greencat.antimony.common.mixins;

import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S3DPacketDisplayScoreboard.class)
public interface S3DPacketDisplayScoreboardAccessor {
    @Accessor("scoreName")
    String getScoreName();
}
