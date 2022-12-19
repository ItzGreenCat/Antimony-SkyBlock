package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.InventoryClicker;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.develop.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.stream.Collectors;


public class AutoTerminal {
    protected static TerminalType terminal = TerminalType.NONE;
    protected static List<Slot> clickQueue = new ArrayList<>();
    @Deprecated
    protected static List<Slot> rightClickQueue = new ArrayList<>();
    static int cooldown = 0;
    static int threadTimer = -1;
    static int lastWindowID;
    static int targetColorIndex = -1;
    @Deprecated
    static long lastClickTime = 0;
    static boolean threadFlag = false;
    static Thread solverThread;
    static final ArrayList<Integer> colorOrder = new ArrayList<>(Arrays.asList(14, 1, 4, 13, 11));

    public AutoTerminal() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiBackgroundDrawn(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (FunctionManager.getStatus("AutoTerminal")) {
            if (terminal == TerminalType.NONE) {
                getTerminal();
            }
            if (terminal != TerminalType.NONE && AutoTerminal.clickQueue.isEmpty()) {
                GuiScreen screen = Minecraft.getMinecraft().currentScreen;
                if (screen instanceof GuiChest) {
                    Container chestContainer = ((GuiChest) screen).inventorySlots;
                    if (chestContainer.windowId != lastWindowID) {
                        lastWindowID = chestContainer.windowId;
                        Solver solver = new Solver();
                        threadFlag = false;
                        solverThread = new Thread(solver);
                        solverThread.start();
                        threadTimer = 0;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("AutoTerminal")) {
            if (event.phase == TickEvent.Phase.END) {
                if (threadTimer + 1 > 40) {
                    threadTimer = -1;
                    threadFlag = true;
                } else {
                    threadTimer = threadTimer + 1;
                }
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
                    AutoTerminal.clickQueue.clear();
                    AutoTerminal.rightClickQueue.clear();
                    terminal = TerminalType.NONE;
                    threadFlag = true;
                    threadTimer = -1;
                    cooldown = 0;
                } else if (terminal != TerminalType.NONE && !AutoTerminal.clickQueue.isEmpty()) {
                    if (cooldown + 1 > (Integer) getConfigByFunctionName.get("AutoTerminal","cooldown")) {
                        //if(rightClickQueue.isEmpty()) {
                            InventoryClicker.ClickSlot(AutoTerminal.clickQueue.get(0), InventoryClicker.Type.LEFT/*type*/);
                            AutoTerminal.clickQueue.remove(0);
                        /*} else {
                            InventoryClicker.ClickSlot(AutoTerminal.clickQueue.get(0), InventoryClicker.Type.RIGHT);
                            AutoTerminal.rightClickQueue.remove(0);
                        }*/
                        cooldown = 0;
                    } else {
                        cooldown = cooldown + 1;
                    }
                }
            }
        }
    }

    public void getTerminal() {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof GuiChest) {
            Container chestContainer = ((GuiChest) screen).inventorySlots;
            if (chestContainer instanceof ContainerChest) {
                ContainerChest container = (ContainerChest) chestContainer;
                String GuiName = container.getLowerChestInventory().getDisplayName().getUnformattedText();
                if (GuiName.contains("Navigate the maze!")) {
                    terminal = TerminalType.MAZE;
                } else if (GuiName.contains("Click in order!")) {
                    terminal = TerminalType.CLICK_IN_ORDER;
                } else if (GuiName.contains("Correct all the panes!")) {
                    terminal = TerminalType.CORRECT_ALL_PANEL;
                } else if (GuiName.contains("What starts with:")) {
                    terminal = TerminalType.LETTER;
                } else if (GuiName.contains("Select all the")) {
                    terminal = TerminalType.COLOR;
                } else if (GuiName.contains("Change all to same color!")) {
                    terminal = TerminalType.CHANGE_ALL_TO_SAME_COLOR;
                } else {
                    terminal = TerminalType.NONE;
                }
            }
        }
    }

    public enum TerminalType {
        MAZE, CLICK_IN_ORDER, CORRECT_ALL_PANEL, LETTER, COLOR, CHANGE_ALL_TO_SAME_COLOR, NONE
    }
}

class Solver implements Runnable {
    @Override
    public void run() {
        List<Slot> currentSlots;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof GuiChest) {
            Container chestContainer = ((GuiChest) screen).inventorySlots;
            currentSlots = chestContainer.inventorySlots;
            String chestName = ((ContainerChest)chestContainer).getLowerChestInventory().getDisplayName().getUnformattedText();
            switch (AutoTerminal.terminal) {
                case MAZE:
                    Console.addMessage("Auto Terminal Type MAZE");
                    int[] mazeDirection = new int[]{-9, -1, 1, 9};
                    boolean[] isStartSlot = new boolean[54];
                    int endSlot = -1;
                    for (Slot slot : currentSlots) {
                        if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                        ItemStack itemStack = slot.getStack();
                        if (itemStack == null) continue;
                        if (itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                            if (itemStack.getItemDamage() == 5) {
                                isStartSlot[slot.slotNumber] = true;
                            } else if (itemStack.getItemDamage() == 14) {
                                endSlot = slot.slotNumber;
                            }
                        }
                    }
                    for (int slot = 0; slot < 54; slot++) {
                        if (isStartSlot[slot]) {
                            boolean[] mazeVisited = new boolean[54];
                            int startSlot = slot;
                            while (startSlot != endSlot) {
                                boolean newSlotChosen = false;
                                for (int i : mazeDirection) {
                                    int nextSlot = startSlot + i;
                                    if (nextSlot < 0 || nextSlot > 53 || i == -1 && startSlot % 9 == 0 || i == 1 && startSlot % 9 == 8)
                                        continue;
                                    if (nextSlot == endSlot) return;
                                    if (mazeVisited[nextSlot]) continue;
                                    ItemStack itemStack = currentSlots.get(nextSlot).getStack();
                                    if (itemStack == null) continue;
                                    if (itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.getItemDamage() == 0) {
                                        AutoTerminal.clickQueue.add(currentSlots.get(nextSlot));
                                        startSlot = nextSlot;
                                        mazeVisited[nextSlot] = true;
                                        newSlotChosen = true;
                                        break;
                                    }
                                }
                                if (!newSlotChosen) {
                                    Console.addMessage("an error happened while solving maze");
                                    return;
                                }
                            }
                        }
                    }
                case CLICK_IN_ORDER:
                    Console.addMessage("Auto Terminal Type CLICK_IN_ORDER");
                    int min = 0;
                    Slot[] temp = new Slot[14];
                    for (int i = 10; i <= 25; i++) {
                        if (i == 17 || i == 18) continue;
                        ItemStack itemStack = currentSlots.get(i).getStack();
                        if (itemStack == null) continue;
                        if (itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.stackSize < 15) {
                            if (itemStack.getItemDamage() == 14) {
                                temp[itemStack.stackSize - 1] = currentSlots.get(i);
                            } else if (itemStack.getItemDamage() == 5) {
                                if (min < itemStack.stackSize) {
                                    min = itemStack.stackSize;
                                }
                            }
                        }
                    }
                    AutoTerminal.clickQueue.addAll(Arrays.stream(temp).filter(Objects::nonNull).collect(Collectors.toList()));
                    if (AutoTerminal.clickQueue.size() != 14 - min) return;
                    break;

                case CORRECT_ALL_PANEL:
                    Console.addMessage("Auto Terminal Type CORRECT_ALL_PANEL");
                    for (Slot slot : currentSlots) {
                        if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                        if (slot.slotNumber < 9 || slot.slotNumber > 35 || slot.slotNumber % 9 <= 1 || slot.slotNumber % 9 >= 7)
                            continue;
                        ItemStack itemStack = slot.getStack();
                        if (itemStack == null) return;
                        if (itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.getItemDamage() == 14) {
                            AutoTerminal.clickQueue.add(slot);
                        }
                    }
                    break;

                case LETTER:
                    Console.addMessage("Auto Terminal Type LETTER");
                    if (chestName.length() > chestName.indexOf("'") + 1) {
                        char letterNeeded = chestName.charAt(chestName.indexOf("'") + 1);
                        for (Slot slot : currentSlots) {
                            if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                            if (slot.slotNumber < 9 || slot.slotNumber > 44 || slot.slotNumber % 9 == 0 || slot.slotNumber % 9 == 8)
                                continue;
                            ItemStack itemStack = slot.getStack();
                            if (itemStack == null) return;
                            if (itemStack.isItemEnchanted()) continue;
                            if (StringUtils.stripControlCodes(itemStack.getDisplayName()).charAt(0) == letterNeeded) {
                                AutoTerminal.clickQueue.add(slot);
                            }
                        }
                    }
                    break;

                case COLOR:
                    Console.addMessage("Auto Terminal Type COLOR");
                    String colorNeeded = null;
                    for (EnumDyeColor color : EnumDyeColor.values()) {
                        String colorName = color.getName().replaceAll("_", " ").toUpperCase();
                        if (chestName.contains(colorName)) {
                            colorNeeded = color.getUnlocalizedName();
                            break;
                        }
                    }

                    if (colorNeeded != null) {
                        for (Slot slot : currentSlots) {
                            if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                            if (slot.slotNumber < 9 || slot.slotNumber > 44 || slot.slotNumber % 9 == 0 || slot.slotNumber % 9 == 8)
                                continue;
                            ItemStack itemStack = slot.getStack();
                            if (itemStack == null) return;
                            if (itemStack.isItemEnchanted()) continue;
                            if (itemStack.getUnlocalizedName().contains(colorNeeded)) {
                                AutoTerminal.clickQueue.add(slot);
                            }
                        }
                    }
                    break;

                case CHANGE_ALL_TO_SAME_COLOR:
                    Console.addMessage("Auto Terminal Type CHANGE_ALL_TO_SAME_COLOR");
                    for (Slot slot : currentSlots) {
                        if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                        if (AutoTerminal.targetColorIndex == -1)
                            AutoTerminal.targetColorIndex = getTargetColorIndex(Minecraft.getMinecraft().thePlayer.openContainer.inventorySlots);
                        ItemStack itemStack = slot.getStack();
                        if (itemStack == null) continue;
                        if (!AutoTerminal.colorOrder.contains(itemStack.getItemDamage())) continue;

                        //boolean leftClick = (AutoTerminal.colorOrder.indexOf(AutoTerminal.targetColorIndex) - AutoTerminal.colorOrder.indexOf(itemStack.getItemDamage()) + AutoTerminal.colorOrder.size()) % AutoTerminal.colorOrder.size() < Math.round(AutoTerminal.colorOrder.size() / 2f);
                        AutoTerminal.clickQueue.add(slot);
                    }
                    break;
            }
        }
    }
    private static int getTargetColorIndex(List<Slot> slots) {
        if(slots.isEmpty()) return 15;

        float sum = 0;
        for(Slot slot : slots) {
            if(slot == null) continue;
            ItemStack stack = slot.getStack();
            if(stack == null) continue;
            sum += AutoTerminal.colorOrder.indexOf(stack.getItemDamage());
        }

        int index = Math.round(sum / slots.size());
        return AutoTerminal.colorOrder.size() > index ? index : -1;
    }
}
