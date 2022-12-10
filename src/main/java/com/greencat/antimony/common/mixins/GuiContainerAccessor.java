package com.greencat.antimony.common.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiContainer.class)
public interface GuiContainerAccessor {
    @Invoker("handleMouseClick")
    void handleMouseClick(Slot slot, int slotNumber, int idk_how_it_work_i_just_copy_it_from_minecraft_vanilla_source_code_1, int idk_how_it_work_i_just_copy_it_from_minecraft_vanilla_source_code_2);
}
