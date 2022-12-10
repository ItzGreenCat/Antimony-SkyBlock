package com.greencat.antimony.core;

import com.greencat.antimony.common.mixins.GuiContainerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public class InventoryClicker {
    public static void ClickSlot(Slot slot){
        if(Minecraft.getMinecraft().currentScreen instanceof GuiContainer){
            GuiContainer container = (GuiContainer)Minecraft.getMinecraft().currentScreen;
            ((GuiContainerAccessor)container).handleMouseClick(slot, slot.slotNumber,0, 4);
        }
    }
}
