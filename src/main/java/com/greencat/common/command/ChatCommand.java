package com.greencat.common.command;

import com.greencat.common.Chat.SendToServer;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class ChatCommand extends CommandBase {
    Utils utils = new Utils();
    String[] Usage = {"/amc <消息> 向Antimony聊天频道发送消息"};
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "amc";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0){
            for (String UsageOneLine : Usage) {
                utils.devLog(UsageOneLine);
            }
        } else {
            String message = "";
            for(String arg : args){
                message = message + arg + " ";
            }
            SendToServer sendToServer = new SendToServer();
            sendToServer.send(Minecraft.getMinecraft().thePlayer.getName() + "MSG-!-SPLIT" + message);
        }
    }
}
