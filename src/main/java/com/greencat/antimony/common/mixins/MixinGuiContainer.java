package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.event.CustomEventHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer {
    private final GuiContainer gui = (GuiContainer) (Object) this;

    @Shadow
    public Container inventorySlots;

    @Inject(method = "drawSlot", at = @At("HEAD"), cancellable = true)
    private void onDrawSlot(Slot slot, CallbackInfo ci) {
        CustomEventHandler.GuiContainerEvent.DrawSlotEvent event = new CustomEventHandler.GuiContainerEvent.DrawSlotEvent(inventorySlots, gui, slot);
        CustomEventHandler.EVENT_BUS.post(event);
        if (event.isCanceled())
            ci.cancel();
    }

    @Inject(method = "handleMouseClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;windowClick(IIIILnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"), cancellable = true)
    private void onMouseClick(Slot slot, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        CustomEventHandler.GuiContainerEvent.SlotClickEvent event = new CustomEventHandler.GuiContainerEvent.SlotClickEvent(inventorySlots, gui, slot, slotId);
        CustomEventHandler.EVENT_BUS.post(event);
        if (event.isCanceled())
            ci.cancel();
    }
}
