package com.greencat.common.command;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.ConfigLoader;
import com.greencat.common.storage.SelectGUIStorage;
import com.greencat.common.ui.FunctionNotice;
import com.greencat.type.SelectObject;
import com.greencat.type.SelectTable;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandManager extends CommandBase {
    FunctionNotice functionNotice = new FunctionNotice();
    Utils utils = new Utils();
    String[] Usage = {"/antimony 主命令",
           // "/antimony AutoFishYaw <整数:YAW> 设置钓鱼视角Yaw",
            "/antimony HUDHeight <整数:HEIGHT> 设置SelectGUI距屏幕顶部高度",
            "/antimony FunctionHeight <整数:HEIGHT> 设置FunctionList距屏幕顶部高度",
            "/antimony ss <整数:SCALING> 设置大型截图SCALING(分辨率是窗口分辨率的SCALING倍)",
            "/antimony nw <字符串:玩家名字> <可留空字符串:存档名字>",
            "/antimony petName <字符串:规则> <整数:等级> 设置CustomPetNameTag的自定义规则和自定义等级,规则为\"原始字符=要替换的字符\",(例如Sheep=Golden%SPACESheep),若想要重置规则，则将<字符串:规则填写null," +
                    "如果遇到空格请使用%SPACE代替,如果添加多项规则请使用\",\"分割,(例如Sheep=Golden%SPACESheep,Magma%SPACECube=Slime%SPACECube),宠物等级为整数,若想取消自定义等级请填写\"0\"",
            "/antimony ISwitch <字符串:物品名字(不用全名,空格用_表示)> <字符串:<LEFT,RIGHT>填写LEFT为左键填写RIGHT为右键>",
            "/antimony MouseSwitch <字符串:手中物品名字(不用全名,空格用_表示)> <字符串:<LEFT,RIGHT>填写LEFT为左键触发填写RIGHT为右键触发>",
            "/antimony AutoFishNotice <字符串:ON/OFF> 开启或关闭钓鱼提示"
            //"/antimony AutoFishPresentYaw 将当前Yaw设置为AutoFish使用的视角Yaw",
    //"/antimony SetSwordAndRodIndex [整数:SWORD_INDEX,0-8] [整数:ROD_INDEX,0-8]"
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
        //System.out.println(args.toString());
        //System.out.println(args.length);
        if(args.length == 0){
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
            if(args[0].equals("sw")) {
                ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
                utils.print(String.valueOf(scaledResolution.getScaledWidth()));
            }
            if(args[0].equals("AutoEnderman")) {
                if (FunctionManager.getStatus("AutoEnderman")) {
                    FunctionManager.setStatus("AutoEnderman",false);
                } else {
                    FunctionManager.setStatus("AutoEnderman",true);
                }
            }
            if(args[0].equals("ArmorStandFind")) {
                if (FunctionManager.getStatus("ArmorStandFinder")) {
                    FunctionManager.setStatus("ArmorStandFinder",false);
                } else {
                    FunctionManager.setStatus("ArmorStandFinder",true);
                }
            }
            if(args[0].equals("lg")) {
                for(SelectTable table : SelectGUIStorage.TableStorage){
                    utils.print("列表" + table.getID());
                    for(SelectObject object : table.getList()){
                        utils.print("功能" + object.getName() + "在" + table.getID());
                    }
                }
            }
            if(args[0].equals("ui")) {
            }
            if(args[0].equalsIgnoreCase("AutoFishPresentYaw")) {
                Antimony.AutoFishYaw = (int) Minecraft.getMinecraft().thePlayer.rotationYaw;
                utils.print("Yaw已设置为" + Minecraft.getMinecraft().thePlayer.rotationYaw);
            }



        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("AutoFishYaw")){
                Antimony.AutoFishYaw  = Integer.parseInt(args[1]);
                utils.print("Yaw已设置为" + args[1]);
            }
            if(args[0].equalsIgnoreCase("HUDHeight")){
                ConfigLoader.SetGuiHeight(Integer.parseInt(args[1]));
                utils.print("GUI距离顶部高度已设置为" + args[1]);
            }
            if(args[0].equalsIgnoreCase("FunctionHeight")){
                ConfigLoader.SetFunctionGuiHeight(Integer.parseInt(args[1]));
                utils.print("FunctionList距离顶部高度已设置为" + args[1]);
            }
            if(args[0].equalsIgnoreCase("ss")){
                Antimony.ImageScaling = Integer.parseInt(args[1]);
                utils.print("设置完成");
            }
            if(args[0].equalsIgnoreCase("nw")){
                utils.print("正在查询,可能需要10-20秒");
                utils.getNetworth(args[1]);
            }
            if(args[0].equalsIgnoreCase("autofishnotice")){
                if(args[1].equalsIgnoreCase("on")){
                    ConfigLoader.setAutoFishNotice(true);
                } else if(args[1].equalsIgnoreCase("off")) {
                    ConfigLoader.setAutoFishNotice(false);
                }
                if(ConfigLoader.getAutoFishNotice()) {
                    utils.print("当前钓鱼信息状态: 开启");
                } else {
                    utils.print("当前钓鱼信息状态: 关闭");
                }
            }
        }
        if(args.length == 3){
            if(args[0].equalsIgnoreCase("SetSwordAndRodIndex")){
                Antimony.SwordIndex = Integer.parseInt(args[1]);
                Antimony.RodIndex = Integer.parseInt(args[2]);
                utils.print("设置完成");
            }
            if(args[0].equalsIgnoreCase("nw")){
                utils.print("正在查询,可能需要10-20秒");
                utils.getNetworth(args[1],args[2]);
            }
            if(args[0].equalsIgnoreCase("petName")){
                ConfigLoader.setPetNameRule(args[1].replaceAll("%SPACE"," "));
                ConfigLoader.setCustomPetLevel(Integer.parseInt(args[2]));
                utils.print("设置完成,规则:" + args[1].replaceAll("%SPACE"," ") + " 等级:" + args[2]);
            }
            if(args[0].equalsIgnoreCase("iswitch")){
                ConfigLoader.setISwitch(args[1].replace("_"," "),args[2].toUpperCase());
                utils.print("设置完成,规则:" + args[1].replace("_"," ") + "方式:" + args[2].toUpperCase());
            }
            if(args[0].equalsIgnoreCase("mouseswitch")){
                ConfigLoader.setMSwitch(args[1].replace("_"," "),args[2].toUpperCase());
                utils.print("设置完成,规则:" + args[1].replace("_"," ") + "方式:" + args[2].toUpperCase());
            }

        }
        }
    }


