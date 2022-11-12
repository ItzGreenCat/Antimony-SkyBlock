package com.greencat.antimony.common.mixins;

import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiTextField.class)
public interface GuiTextFieldAccessor {
    @Accessor("id")
    void setID(int id);
}
