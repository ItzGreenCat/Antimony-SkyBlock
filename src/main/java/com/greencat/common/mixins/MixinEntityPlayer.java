package com.greencat.common.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer extends MixinLivingEntityBase{
    @Shadow
    public PlayerCapabilities capabilities;
    @Shadow
    private int itemInUseCount;
    @Shadow
    public InventoryPlayer inventory;
    @Shadow
    protected float speedInAir;
    @Shadow
    public float experience;
    @Shadow
    public int experienceLevel;
    @Shadow
    public int experienceTotal;
}
