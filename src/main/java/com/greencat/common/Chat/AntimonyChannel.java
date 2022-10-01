package com.greencat.common.Chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class AntimonyChannel {
    public static Socket socket;
    public AntimonyChannel() {
        try {
            socket = new Socket(InetAddress.getByName("antimonychat.greencatmc.top"), 11451);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
