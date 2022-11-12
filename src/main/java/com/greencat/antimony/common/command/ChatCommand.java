package com.greencat.antimony.common.command;

import com.greencat.antimony.common.Chat.SendToServer;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class ChatCommand extends CommandBase {
    Utils utils = new Utils();
    String[] Usage = {"/amc <消息> 向Antimony聊天频道发送消息","/amc toggle 保持/退出Antimony聊天频道"};
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
                utils.print(UsageOneLine);
            }
        } else {
            if(!(args.length == 1 && args[0].equals("toggle"))) {
                String message = "";
                for (String arg : args) {
                    message = message + arg + " ";
                }
                SendToServer sendToServer = new SendToServer();
                sendToServer.send(Minecraft.getMinecraft().thePlayer.getName() + "MSG-!-SPLIT" + message);
            } else {
                ConfigLoader.setChatChannel(!ConfigLoader.getChatChannel());
                utils.print(ConfigLoader.getChatChannel() ? "进入Antimony聊天频道" : "退出Antimony聊天频道");
            }
        }
    }
}
