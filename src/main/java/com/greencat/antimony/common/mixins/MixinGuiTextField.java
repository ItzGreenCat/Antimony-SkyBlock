package com.greencat.antimony.common.mixins;

import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiTextField.class)
public interface MixinGuiTextField {
    @Accessor("visible")
    boolean getVisible();
    @Accessor("visible")
    void setVisible(boolean visible);
    @Accessor("lineScrollOffset")
    int getLineScrollOffset();
}
