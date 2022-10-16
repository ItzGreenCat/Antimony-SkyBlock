package com.greencat.common.Chat;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.utils.Utils;

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
