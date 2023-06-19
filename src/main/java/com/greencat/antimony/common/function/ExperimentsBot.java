package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

import static com.greencat.antimony.core.config.ConfigInterface.get;

public class ExperimentsBot {
    Minecraft mc = Minecraft.getMinecraft();
    private ExperimentType currentExperiment = ExperimentType.NONE;
    private boolean hasAdded = false;
    private int clicks = 0;
    private long lastClickTime = 0L;
    private final List<Map.Entry<Integer, String>> chronomatronOrder = new ArrayList<>(28);
    private int lastAdded = 0;
    private final HashMap<Integer,Integer> ultrasequencerOrder = new HashMap<>();
    public ExperimentsBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
            currentExperiment = ExperimentType.NONE;
            hasAdded = false;
            chronomatronOrder.clear();
            lastAdded = 0;
            ultrasequencerOrder.clear();
            if (!Utils.isOnSkyBlock() || !(event.gui instanceof GuiChest)) return;
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (chestName.contains("Chronomatron") && !chestName.contains("Sta")) {
                    currentExperiment = ExperimentType.CHRONOMATRON;
                } else if (chestName.contains("Ultrasequencer") && !chestName.contains("Sta")) {
                    currentExperiment = ExperimentType.ULTRASEQUENCER;
                } else if (chestName.startsWith("Superpairs (")) {
                    currentExperiment = ExperimentType.SUPERPAIRS;
                }
            }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
            if (!Utils.isOnSkyBlock() || !(event.gui instanceof GuiChest)) return;
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                List<Slot> invSlots = container.inventorySlots;
                switch (currentExperiment) {
                    case CHRONOMATRON:
                        if (invSlots.get(49).getStack() != null && invSlots.get(49).getStack().getItem() == Item.getItemFromBlock(Blocks.glowstone) && invSlots.get(lastAdded) != null &&
                                    !invSlots.get(lastAdded).getStack().isItemEnchanted()

                        ) {
                            hasAdded = false;
                            if (chronomatronOrder.size() > 11 && (Boolean) get("ExperimentsBot", "autoExit")) {
                                mc.thePlayer.closeScreen();
                            }
                        }
                        if (!hasAdded && invSlots.get(49).getStack() != null && invSlots.get(49).getStack().getItem() == Items.clock) {
                            Optional<Slot> optional = invSlots.stream().filter(it -> it.slotNumber >= 10 && it.slotNumber <= 43).filter(item -> item.getStack().isItemEnchanted()).findFirst();
                            if (optional.isPresent()) {
                                Slot slot = optional.get();
                                chronomatronOrder.add(new AbstractMap.SimpleEntry<>(slot.slotNumber, slot.getStack().getDisplayName()));
                                lastAdded = slot.slotNumber;
                                hasAdded = true;
                                clicks = 0;
                            }
                        }
                        if (hasAdded && invSlots.get(49) != null && invSlots.get(49).getStack().getItem() == Items.clock && chronomatronOrder.size() > clicks &&
                                FunctionManager.getStatus("ExperimentsBot") && System.currentTimeMillis() - lastClickTime > (Integer) get("ExperimentsBot", "delay")
                        ) {
                            mc.playerController.windowClick(
                                    mc.thePlayer.openContainer.windowId,
                                    chronomatronOrder.get(clicks).getKey(),
                                    0,
                                    0,
                                    mc.thePlayer
                            );
                            lastClickTime = System.currentTimeMillis();
                            clicks++;
                        }
                        break;
                    case ULTRASEQUENCER:
                        if (invSlots.get(49) != null && invSlots.get(49).getStack().getItem() == Items.clock) {
                            hasAdded = false;
                        }
                        if (!hasAdded && invSlots.get(49) != null && invSlots.get(49).getStack().getItem() == Item.getItemFromBlock(Blocks.glowstone)) {
                            if (!invSlots.get(44).getHasStack()) return;
                            ultrasequencerOrder.clear();
                            invSlots.stream().filter(it -> it.slotNumber >= 9 && it.slotNumber <= 44).forEach(this::setUltraSequencerOrder);
                            hasAdded = true;
                            clicks = 0;
                            if (ultrasequencerOrder.size() > 9 && (Boolean) get("ExperimentsBot", "autoExit")) {
                                mc.thePlayer.closeScreen();
                            }
                        }
                        if (invSlots.get(49).getStack() != null && invSlots.get(49).getStack().getItem() == Items.clock && ultrasequencerOrder.containsKey(clicks) &&
                                FunctionManager.getStatus("ExperimentsBot") && System.currentTimeMillis() - lastClickTime > (Integer) get("ExperimentsBot", "delay")
                        ) {
                                Integer slot = ultrasequencerOrder.get(clicks);
                                if (slot != null) {
                                    mc.playerController.windowClick(
                                            mc.thePlayer.openContainer.windowId,
                                            slot, 0, 0, mc.thePlayer
                                    );
                                }
                                lastClickTime = System.currentTimeMillis();
                                clicks++;
                        }
                        break;
                    default:
                        break;

                }
            }
    }

    public void setUltraSequencerOrder(Slot slot) {
        if (slot.getStack() != null && slot.getStack().getItem() == Items.dye) {
            ultrasequencerOrder.put(slot.getStack().stackSize - 1,slot.slotNumber);
        }
    }
    enum ExperimentType {
        CHRONOMATRON,
        ULTRASEQUENCER,
        SUPERPAIRS,
        NONE
    }
}
