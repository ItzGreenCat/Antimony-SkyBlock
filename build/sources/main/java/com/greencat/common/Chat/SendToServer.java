package com.greencat.antimony.common.Chat;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.utils.Utils;

import java.io.PrintStream;

public class SendToServer {
    PrintStream ps;
    public void send(String msg) {
        if(FunctionManager.getStatus("AntimonyChannel")) {
            try {
                ps = new PrintStream(AntimonyChannel.socket.getOutputStream(), false, "UTF-8");
                ps.println(msg);
                ps.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
