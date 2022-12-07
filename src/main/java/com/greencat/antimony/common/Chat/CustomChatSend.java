package com.greencat.antimony.common.Chat;

import com.greencat.Antimony;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

public class CustomChatSend {
    public static void send(String str){
        try {
            DataOutputStream out = new DataOutputStream(AntimonyChannel.socket.getOutputStream());
            out.writeUTF(str);
            out.flush();
        } catch(Exception e){
            Antimony.getjLogger().log(Level.WARNING,"Cannot send message");
        }
    }
}
