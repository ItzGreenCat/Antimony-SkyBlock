package com.greencat.extranal;

import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatSplitFrame {
    public JPanel MainPanel;
    private JTextArea ChatArea;
    private JPanel InputPanel;
    private JTextField ChatInput;
    private JButton Send;

    public ChatSplitFrame() {
        Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage(ChatInput.getText());
            }
        });
    }
}
