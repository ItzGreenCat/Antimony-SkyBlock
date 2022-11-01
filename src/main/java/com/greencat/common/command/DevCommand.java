package com.greencat.common.command;

import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;

public class DevCommand extends CommandBase {
    public static List<BlockPos> posList;
    public static boolean renderNode = false;
    Utils utils = new Utils();
    String[] Usage = {"/antimonydev 主命令",
    "SelfName"
    };
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "antimonydev";
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
        }
        if(args.length == 1){
            if (args[0].equalsIgnoreCase("selfname")) {
                utils.devLog("玩家自身名字(去除特殊符号):" + Minecraft.getMinecraft().thePlayer.getDisplayNameString());
            }
        }
    }
}
