package com.greencat.common.Chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class AntimonyChannel {
    public static Socket socket;
    public AntimonyChannel() {
        try {
            socket = new Socket(InetAddress.getByName("antimonychat.greencatmc.top")/*"127.0.0.1"*/, 11451);
            socket.setOOBInline(true);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
