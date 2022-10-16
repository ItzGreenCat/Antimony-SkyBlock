package com.greencat.common.mixins;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({EntityPlayerSP.class})
public interface EntityPlayerSPAccessor {
    @Accessor
    double getLastReportedPosX();

    @Accessor
    void setLastReportedPosX(double var1);

    @Accessor
    double getLastReportedPosY();

    @Accessor
    void setLastReportedPosY(double var1);

    @Accessor
    double getLastReportedPosZ();

    @Accessor
    void setLastReportedPosZ(double var1);

    @Accessor
    float getLastReportedYaw();

    @Accessor
    void setLastReportedYaw(float var1);

    @Accessor
    float getLastReportedPitch();

    @Accessor
    void setLastReportedPitch(float var1);

    @Accessor
    void setServerSprintState(boolean var1);

    @Accessor
    boolean getServerSprintState();

    @Accessor
    void setServerSneakState(boolean var1);

    @Accessor
    boolean getServerSneakState();
}
