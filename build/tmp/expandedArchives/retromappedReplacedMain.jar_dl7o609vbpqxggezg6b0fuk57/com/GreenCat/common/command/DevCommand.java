package com.GreenCat.common.command;

import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class DevCommand extends CommandBase {
    Utils utils = new Utils();
    String[] Usage = {"/antimonydev 主命令",
    "/antimonydev SelfName 获取自身名字"
    };
    public int func_82362_a() {
        return 0;
    }

    @Override
    public String func_71517_b() {
        return "antimonydev";
    }

    @Override
    public String func_71518_a(ICommandSender sender) {
        return "";
    }

    @Override
    public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0){
            for (String UsageOneLine : Usage) {
                utils.devLog(UsageOneLine);
            }
        }
        if(args.length == 1){
            if (args[0].equalsIgnoreCase("selfname")) {
                utils.devLog("玩家自身名字(去除特殊符号):" + Minecraft.func_71410_x().field_71439_g.getDisplayNameString());
            }
        }
    }
}
