package com.greencat.antimony.common.command;

import com.greencat.Antimony;
import com.greencat.antimony.common.function.FakeBan;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.common.function.CustomItemName;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.core.PathfinderProxy;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommandManager extends CommandBase {
    Utils utils = new Utils();
    String[] Usage = {"/antimony 主命令",
            EnumChatFormatting.GOLD + "/amc <消息> 向Antimony聊天频道发送消息",
            "/antimony ss <整数:SCALING> 设置大型截图SCALING(分辨率是窗口分辨率的SCALING倍)",
            "/antimony reloadRepo 重载远程内容",
            "/antimony getUUID 获取手持物品UUID",
            "/antimony goto <整数:X> <整数:Y> <整数:Z> 自动寻找去某个方块的路径",
            "/antimony fakeban <原因> 制造FakeBan"
    };

    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "antimony";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            for (String UsageOneLine : Usage) {
                utils.print(UsageOneLine);
            }
        }
        if (args.length == 1) {
            if (args[0].equals("help")) {
                for (String UsageOneLine : Usage) {
                    utils.print(UsageOneLine);
                }
            }
            if (args[0].equalsIgnoreCase("reloadRepo")) {
                try {
                    String content = "";
                    URL url = new URL("https://itzgreencat.github.io/AntimonyDecorate/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String input;
                    while ((input = reader.readLine()) != null) {
                        content += input;
                    }
                    reader.close();
                    Antimony.GroundDecorateList.clear();
                    for (String str : content.split(";")) {
                        Antimony.GroundDecorateList.put(str.split(",")[0], str.split(",")[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String content = "";
                    URL url = new URL("https://itzgreencat.github.io/AntimonyCustomItemName/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String input;
                    while ((input = reader.readLine()) != null) {
                        content += input;
                    }
                    reader.close();
                    CustomItemName.CustomName.clear();
                    for (String str : content.split(";")) {
                        CustomItemName.CustomName.put(str.split(",")[0], str.split(",")[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (args[0].equalsIgnoreCase("getUUID")) {
                if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
                    String uuid = Utils.getUUIDForItem(Minecraft.getMinecraft().thePlayer.getHeldItem());
                    if(uuid != null){
                        utils.print("UUID: " + uuid + " 已复制到剪贴板");
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        Transferable trans = new StringSelection(uuid);
                        clipboard.setContents(trans, null);
                    } else {
                        utils.print("此物品无UUID");
                    }
                } else {
                    utils.print("此物品无UUID");
                }
            }

        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("ss")) {
                Antimony.ImageScaling = Integer.parseInt(args[1]);
                utils.print("设置完成");
            }
        }
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("fakeban")) {
                String str = "";
                boolean skipped = false;
                for(String string : args) {
                    if(!skipped){
                        skipped = true;
                    } else {
                        str = str + string + " ";
                    }
                }
                FakeBan.active(str);
            }
        }
        if (args.length == 5) {
            if(args[0].equalsIgnoreCase("goto")){
                try {
                    PathfinderProxy.calcPathDistance(new BlockPos(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])), Integer.parseInt(args[4]));
                    if (!PathfinderProxy.running && Pathfinder.hasPath()) {
                        if (!FunctionManager.getStatus("Pathfinding")) {
                            FunctionManager.setStatus("Pathfinding", true);
                        }
                    }
                } catch (Exception e) {
                    Utils.print("寻找路径时出错");
                    e.printStackTrace();
                }

            }
        }
    }
}


