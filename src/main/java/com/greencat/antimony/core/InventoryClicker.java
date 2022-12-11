package com.greencat.antimony.core;

import com.greencat.antimony.common.mixins.GuiContainerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public class InventoryClicker {
    public static void ClickSlot(Slot slot,Type type){
        if(Minecraft.getMinecraft().currentScreen instanceof GuiContainer){
            GuiContainer container = (GuiContainer)Minecraft.getMinecraft().currentScreen;
            if(type == Type.DROP) {
                ((GuiContainerAccessor) container).handleMouseClick(slot, slot.slotNumber, 0, 4);
            }
            if(type == Type.LEFT){
                ((GuiContainerAccessor) container).handleMouseClick(slot,slot.slotNumber,0,0);
            }
            if(type == Type.RIGHT){
                ((GuiContainerAccessor) container).handleMouseClick(slot,slot.slotNumber,1,0);
            }
        }
    }
    public static void ClickSlot(Slot slot,int button,int type){
        if(Minecraft.getMinecraft().currentScreen instanceof GuiContainer){
            GuiContainer container = (GuiContainer)Minecraft.getMinecraft().currentScreen;
            ((GuiContainerAccessor) container).handleMouseClick(slot, slot.slotNumber, button, type);
        }
    }
    public enum Type{
        DROP,LEFT,RIGHT
    }
}
