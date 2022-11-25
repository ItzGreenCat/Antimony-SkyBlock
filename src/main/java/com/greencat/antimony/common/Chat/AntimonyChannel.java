package com.greencat.antimony.common.Chat;

import java.net.InetAddress;
import java.net.Socket;

public class AntimonyChannel {
    public static Socket socket;
    public AntimonyChannel() {
        try {
            socket = new Socket(InetAddress.getByName("amc.pysio.online")/*"127.0.0.1"*/, 11451);
            socket.setOOBInline(true);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
