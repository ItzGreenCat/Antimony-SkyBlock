package com.greencat.antimony.develop;

import com.greencat.antimony.core.InventoryClicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Console {
    public static JFrame frame = new JFrame("Console");
    public static JPanel textPanel = new JPanel();
    public static JPanel sendPanel = new JPanel();
    public static JTextArea textArea = new JTextArea();
    public static JTextField inputArea = new JTextField();
    public static JScrollPane scrollPane = new JScrollPane(textArea);

    public static void init() {
        //inti console ui
        frame.setLayout(new BorderLayout());
        scrollPane.setBounds(0, 0, 384, 200);
        scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(scrollPane, BorderLayout.CENTER);
        inputArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    addMessage(inputArea.getText());
                    handleInput(inputArea.getText());
                }
            }
        });
        sendPanel.add(inputArea);
        frame.setSize(384, 216);
        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(inputArea, BorderLayout.SOUTH);
        frame.setAlwaysOnTop(true);
    }

    public static void addMessage(String str) {
        textArea.append(str + "\n");
    }

    public static void startMessage(String str) {
        textArea.append("start running " + str + "\n");
    }

    public static void errorMessage(String str) {
        textArea.append("an error happened on running " + str + "\n");
    }

    public static void handleInput(String str) {
        String[] string = str.split(" ");
        //a flag - use for skip command string
        boolean skipped = false;
        String command = string[0];
        List<String> args = new ArrayList<String>();
        //skip command string and add args to a list
        for (String arg : string) {
            if (skipped) {
                args.add(arg);
            } else {
                skipped = true;
            }
        }
        startMessage(command);
        try {
            if (command.equals("help")) {
                addMessage("clickslot [Name] - ClickInventory Slot by item name.");
            }
            if (command.equals("clickslot")) {
                if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
                    Container container = ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots;
                    if(container instanceof ContainerChest) {
                        List<Slot> slots = container.inventorySlots;
                        for (Slot slot : slots) {
                            if (slot != null && slot.getStack() != null && slot.getStack().hasDisplayName() && StringUtils.stripControlCodes(slot.getStack().getDisplayName()).toLowerCase().contains(args.get(0).toLowerCase())) {
                                //Test the InventoryClicker
                                InventoryClicker.ClickSlot(slot);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            errorMessage(command);
            addMessage(e.toString());
            e.printStackTrace();
        }
    }
}
