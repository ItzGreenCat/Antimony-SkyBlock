package com.greencat;

import com.greencat.antimony.common.Chat.AntimonyChannel;
import com.greencat.antimony.common.Chat.CheckConnect;
import com.greencat.antimony.common.Chat.ReadFromServer;
import com.greencat.antimony.common.EventLoader;
import com.greencat.antimony.common.MainMenu.GuiMainMenuModify;
import com.greencat.antimony.common.Via;
import com.greencat.antimony.common.command.ChatCommand;
import com.greencat.antimony.common.command.CommandManager;
import com.greencat.antimony.common.command.DevCommand;
import com.greencat.antimony.common.function.*;
import com.greencat.antimony.common.function.rank.CustomRank;
import com.greencat.antimony.common.function.rank.RankList;
import com.greencat.antimony.common.function.title.TitleManager;
import com.greencat.antimony.common.key.KeyLoader;
import com.greencat.antimony.core.*;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.blacklist.BlackList;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.settings.*;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.utils.Blur;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import io.netty.channel.EventLoop;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

@Mod(modid = Antimony.MODID, name = Antimony.NAME, version = Antimony.VERSION, acceptedMinecraftVersions = "1.8.9", clientSideOnly = true)
public class Antimony {
    public static final String MODID = "antimony";
    public static final String NAME = "Antimony-Client";
    public static final String VERSION = "3.3.1";
    private static final String Sb = "Sb";

    public static float strafe;
    public static float forward;
    public static float friction;

    Utils utils = new Utils();

    public static boolean AutoFishYawState = false;
    public static int ImageScaling = 1;
    public static boolean shouldRenderBossBar = true;
    public static int Color = 16542622;
    public static File AntimonyDirectory = new File(System.getProperty("user.dir") + "\\Antimony\\");
    public static String PresentGUI = "root";
    public static String PresentFunction = "";
    public static HashMap<String, String> GroundDecorateList = new HashMap<String, String>();
    public static int versionIndex = 0;
    public static Boolean LabymodInstallCheck;
    public static Boolean NoSaplingBound = false;
    public static Boolean NoTreeBound = false;

    public static HashMap<AntimonyFunction,Integer> KeyBinding = new HashMap<AntimonyFunction, Integer>();


    @Instance(Antimony.MODID)
    public static Antimony instance;

    public static void start() {
        Via.getInstance().start();
    }
    public static Logger getjLogger() {
        return Via.getInstance().getjLogger();
    }

    public static CompletableFuture<Void> getInitFuture() {
        return Via.getInstance().getInitFuture();
    }

    public static ExecutorService getAsyncExecutor() {
        return Via.getInstance().getAsyncExecutor();
    }

    public static EventLoop getEventLoop() {
        return Via.getInstance().getEventLoop();
    }

    public static File getFile() {
        return Via.getInstance().getFile();
    }

    public static String getLastServer() {
        return Via.getInstance().getLastServer();
    }

    public static int getVersion() {
        return Via.getInstance().getVersion();
    }

    public static void setVersion(int version) {
        Via.getInstance().setVersion(version);
    }

    public static void setFile(File file) {
        Via.getInstance().setFile(file);
    }

    public static void setLastServer(String lastServer) {
        Via.getInstance().setLastServer(lastServer);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        // TODO
        new com.greencat.antimony.core.config.ConfigLoader(event);
        new getConfigByFunctionName();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws AWTException {
        // TODO
        new EventLoader();
        new KeyLoader();

        if (!AntimonyDirectory.exists()) {
            AntimonyDirectory.mkdir();
        }
        ClientCommandHandler.instance.registerCommand(new CommandManager());
        ClientCommandHandler.instance.registerCommand(new DevCommand());
        ClientCommandHandler.instance.registerCommand(new ChatCommand());
        LabymodInstallCheck = utils.ModLoadCheck("labymod");

        if (Minecraft.getMinecraft().gameSettings.gammaSetting > 1) {
            Minecraft.getMinecraft().gameSettings.gammaSetting = 0;
        }

        //init GroundDecorate
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
            GroundDecorateList.clear();
            for (String str : content.split(";")) {
                GroundDecorateList.put(str.split(",")[0], str.split(",")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //init CustomItemName
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

        CustomEventHandler.EVENT_BUS.register(new Utils());


        //Misc
        new Chroma();
        new GuiMainMenuModify();
        new CustomEventHandler.ClientTickEndEvent();
        new HUDManager();
        new Pathfinding();
        new nukerCore();
        new BlackList();
        new nukerWrapper();

        //Dev
        //Function
        new AutoFish();
        new AutoKillWorm();
        new WormLavaESP();
        new SilverfishESP();
        new GolemESP();
        new GuardianESP();
        new GemstoneHidePane();
        new FullBright();
        new StarredMobESP();
        new DungeonKeyESP();
        new CustomPetName();
        new CustomItemSound();
        new LanternESP();
        new SkeletonAim();
        new TitleManager();
        new AntiAFKJump();
        new Sprint();
        new Eagle();
        new Velocity();
        new ItemTranslate();
        new GhostBlock();
        new InstantSwitch();
        new AutoClicker();
        new TerminalESP();
        new HideDungeonMobNameTag();
        new PlayerFinder();
        new SecretBot();
        new LividESP();
        new AutoCannon();
        new DroppedItemESP();
        new MouseISwitch();
        new Rat();
        new Killaura();
        new AutoUse();
        new HollowAutoPurchase();
        new WTap();
        new FreeCamera();
        new TargetESP();
        new Cartoon();
        new Interface();
        new ShortBowAura();
        new HideFallingBlock();
        new AutoWolfSlayer();
        new ChestFinder();
        new AutoLeave();
        new CropBot();
        new MarketingGenerator();
        new PeltESP();
        new ForagingBot();
        new JasperESP();
        new SynthesizerAura();
        new Nuker();
        new FrozenTreasureESP();


        Blur.register();

        new com.greencat.antimony.common.decorate.Events();

        new AntimonyChannel();
        new ReadFromServer();
        new CheckConnect();

        new RankList();
        new CustomRank();

        AntimonyRegister register = new AntimonyRegister();
        register.RegisterFunction(new AntimonyFunction("AutoClicker"));
        register.RegisterFunction(new AntimonyFunction("Killaura"));
        register.RegisterFunction(new AntimonyFunction("Reach"));
        register.RegisterFunction(new AntimonyFunction("WTap"));
        register.RegisterFunction(new AntimonyFunction("FreeCamera"));
        register.RegisterFunction(new AntimonyFunction("TargetESP"));
        register.RegisterFunction(new AntimonyFunction("WormLavaESP"));
        register.RegisterFunction(new AntimonyFunction("LanternESP"));
        register.RegisterFunction(new AntimonyFunction("SilverfishESP"));
        register.RegisterFunction(new AntimonyFunction("GolemESP"));
        register.RegisterFunction(new AntimonyFunction("GuardianESP"));
        register.RegisterFunction(new AntimonyFunction("AutoFish"));
        register.RegisterFunction(new AntimonyFunction("AutoKillWorm"));
        register.RegisterFunction(new AntimonyFunction("GemstoneHidePane"));
        register.RegisterFunction(new AntimonyFunction("FullBright"));
        register.RegisterFunction(new AntimonyFunction("StarredMobESP"));
        register.RegisterFunction(new AntimonyFunction("DungeonKeyESP"));
        register.RegisterFunction(new AntimonyFunction("CustomPetNameTag"));
        register.RegisterFunction(new AntimonyFunction("CustomItemSound"));
        register.RegisterFunction(new AntimonyFunction("SkeletonAim"));
        register.RegisterFunction(new AntimonyFunction("AntiAFKJump"));
        register.RegisterFunction(new AntimonyFunction("Sprint"));
        register.RegisterFunction(new AntimonyFunction("Eagle"));
        register.RegisterFunction(new AntimonyFunction("Velocity"));
        register.RegisterFunction(new AntimonyFunction("AutoCannon"));
        register.RegisterFunction(new AntimonyFunction("ItemTranslate"));
        register.RegisterFunction(new AntimonyFunction("HUD"));
        register.RegisterFunction(new AntimonyFunction("GhostBlock"));
        register.RegisterFunction(new AntimonyFunction("InstantSwitch"));
        register.RegisterFunction(new AntimonyFunction("NoHurtCam"));
        register.RegisterFunction(new AntimonyFunction("NoSlow"));
        register.RegisterFunction(new AntimonyFunction("TerminalESP"));
        register.RegisterFunction(new AntimonyFunction("HideDungeonMobNameTag"));
        register.RegisterFunction(new AntimonyFunction("LividESP"));
        register.RegisterFunction(new AntimonyFunction("PlayerFinder"));
        register.RegisterFunction(new AntimonyFunction("SecretBot"));
        register.RegisterFunction(new AntimonyFunction("DroppedItemESP"));
        register.RegisterFunction(new AntimonyFunction("MouseISwitch"));
        register.RegisterFunction(new AntimonyFunction("AutoUse"));
        register.RegisterFunction(new AntimonyFunction("Cartoon"));
        register.RegisterFunction(new AntimonyFunction("HollowAutoPurchase"));
        register.RegisterFunction(new AntimonyFunction("AntimonyChannel"));
        register.RegisterFunction(new AntimonyFunction("Rat"));
        register.RegisterFunction(new AntimonyFunction("Interface"));
        register.RegisterFunction(new AntimonyFunction("ShortBowAura"));
        register.RegisterFunction(new AntimonyFunction("HideFallingBlock"));
        register.RegisterFunction(new AntimonyFunction("Pathfinding"));
        register.RegisterFunction(new AntimonyFunction("AutoWolfSlayer"));
        register.RegisterFunction(new AntimonyFunction("ChestFinder"));
        register.RegisterFunction(new AntimonyFunction("AutoLeave"));
        register.RegisterFunction(new AntimonyFunction("CropBot"));
        register.RegisterFunction(new AntimonyFunction("MarketingGenerator"));
        register.RegisterFunction(new AntimonyFunction("PeltESP"));
        register.RegisterFunction(new AntimonyFunction("BlackList"));
        register.RegisterFunction(new AntimonyFunction("ForagingBot"));
        register.RegisterFunction(new AntimonyFunction("JasperESP"));
        register.RegisterFunction(new AntimonyFunction("SynthesizerAura"));
        register.RegisterFunction(new AntimonyFunction("Nuker"));
        register.RegisterFunction(new AntimonyFunction("NukerWrapper"));
        register.RegisterFunction(new AntimonyFunction("FrozenTreasureESP"));


        register.RegisterTable(new SelectTable("root"));
        register.RegisterTable(new SelectTable("Combat"));
        register.RegisterTable(new SelectTable("Render"));
        register.RegisterTable(new SelectTable("Dungeon"));
        register.RegisterTable(new SelectTable("Macro"));
        register.RegisterTable(new SelectTable("CrystalHollow"));
        register.RegisterTable(new SelectTable("Movement"));
        register.RegisterTable(new SelectTable("Misc"));
        register.RegisterTable(new SelectTable("Fun"));


        register.RegisterSelectObject(new SelectObject("table", "Combat", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Render", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Dungeon", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Macro", "root"));
        register.RegisterSelectObject(new SelectObject("table", "CrystalHollow", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Movement", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Misc", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Fun", "root"));

        register.RegisterSelectObject(new SelectObject("function", "AutoClicker", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "AutoCannon", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "TargetESP", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "NoSlow", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "Killaura", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "ShortBowAura", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "Reach", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "WTap", "Combat"));

        register.RegisterSelectObject(new SelectObject("function", "SilverfishESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "GuardianESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "GolemESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "WormLavaESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "LanternESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "PlayerFinder", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FullBright", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "DroppedItemESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "PeltESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "NoHurtCam", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "HideFallingBlock", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "JasperESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FrozenTreasureESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "ChestFinder", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FreeCamera", "Render"));

        register.RegisterSelectObject(new SelectObject("function", "StarredMobESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "DungeonKeyESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "TerminalESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "HideDungeonMobNameTag", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "SecretBot", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "GhostBlock", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "LividESP", "Dungeon"));

        register.RegisterSelectObject(new SelectObject("function", "AutoFish", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoKillWorm", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoLeave", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoWolfSlayer", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "CropBot", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "ForagingBot", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "SynthesizerAura", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "Nuker", "Macro"));

        register.RegisterSelectObject(new SelectObject("function", "GemstoneHidePane", "CrystalHollow"));
        register.RegisterSelectObject(new SelectObject("function", "HollowAutoPurchase", "CrystalHollow"));

        register.RegisterSelectObject(new SelectObject("function", "AntiAFKJump", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Sprint", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Eagle", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Velocity", "Movement"));


        register.RegisterSelectObject(new SelectObject("function", "SkeletonAim", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "AntimonyChannel", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "InstantSwitch", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "MouseISwitch", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "AutoUse", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "BlackList", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "Interface", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "HUD", "Misc"));

        register.RegisterSelectObject(new SelectObject("function", "CustomPetNameTag", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "CustomItemSound", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "Cartoon", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "MarketingGenerator", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "Rat", "Fun"));


        register.RegisterSelectObject(new SelectObject("function", "ItemTranslate", "root"));

        AntimonyRegister.ReList();

        ConfigLoader.applyFunctionState();

        FunctionManager.setStatus("CustomPetNameTag", false);
        FunctionManager.setStatus("Pathfinding", false);
        FunctionManager.setStatus("Interface", true);
        FunctionManager.setStatus("BlackList", true);
        nukerWrapper.disable();

        FunctionManager.bindFunction("Killaura");
        FunctionManager.addConfiguration(new SettingBoolean("攻击玩家", "isAttackPlayer", true));
        FunctionManager.addConfiguration(new SettingBoolean("目标实体透视", "targetESP", true));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大纵向旋转角度", "maxPitch", 90.0D,90.0D,-90.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大横向旋转角度", "maxYaw", 120.0D,180.0D,-180.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大旋转距离", "maxRotationRange", 6.0D,12.0D,2.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("在此项值内视场角生物为可攻击生物", "Fov", 270.0D,360.0D,90.0D));
        FunctionManager.addConfiguration(new SettingBoolean("攻击NPC", "isAttackNPC", false));
        FunctionManager.addConfiguration(new SettingBoolean("攻击同队伍玩家", "isAttackTeamMember", false));

        FunctionManager.bindFunction("InstantSwitch");
        FunctionManager.addConfiguration(new SettingString("物品名称", "itemName", "of the End"));
        FunctionManager.addConfiguration(new SettingBoolean("左键模式", "leftClick", false));

        FunctionManager.bindFunction("AutoUse");
        FunctionManager.addConfiguration(new SettingLimitInt("间隔时间","cooldown",10,Integer.MAX_VALUE,0));
        FunctionManager.addConfiguration(new SettingString("物品名称(部分匹配,无需写全名)","itemName","of the End"));
        FunctionManager.addConfiguration(new SettingInt("计时器位置(X)", "timerX", 200));
        FunctionManager.addConfiguration(new SettingInt("计时器位置(Y)", "timerY", 130));

        FunctionManager.bindFunction("Reach");
        FunctionManager.addConfiguration(new SettingLimitDouble("距离","distance",3.0D,4.0D,3.0D));

        FunctionManager.bindFunction("AutoFish");
        FunctionManager.addConfiguration(new SettingBoolean("SlugFish模式", "slug", false));
        FunctionManager.addConfiguration(new SettingBoolean("状态提示", "message", true));
        FunctionManager.addConfiguration(new SettingBoolean("显示抛竿计时器", "timer", true));
        FunctionManager.addConfiguration(new SettingBoolean("强制潜行", "sneak", false));
        FunctionManager.addConfiguration(new SettingInt("抛竿计时器位置(X)", "timerX", 200));
        FunctionManager.addConfiguration(new SettingInt("抛竿计时器位置(Y)", "timerY", 100));

        FunctionManager.bindFunction("WTap");
        FunctionManager.addConfiguration(new SettingBoolean("对弓的支持", "bowMode", true));

        HashMap<String, Integer> HUDTypeMap = new HashMap<String, Integer>();
        HUDTypeMap.put("Classic",0);
        HUDTypeMap.put("White",1);
        HUDTypeMap.put("Transparent",2);
        HashMap<String, Integer> HUDHideMap = new HashMap<String, Integer>();
        HUDHideMap.put("Left",0);
        HUDHideMap.put("Right",1);
        HUDHideMap.put("Both",2);
        FunctionManager.bindFunction("HUD");
        FunctionManager.addConfiguration(new SettingInt("左上方(SelectGUI)距屏幕顶部距离","HUDHeight",0));
        FunctionManager.addConfiguration(new SettingInt("右上方(FunctionList)距屏幕顶部距离","FunctionListHeight",0));
        FunctionManager.addConfiguration(new SettingTypeSelector("HUD样式","style",2,HUDTypeMap));
        FunctionManager.addConfiguration(new SettingTypeSelector("HUD关闭时隐藏部分","hide",2,HUDHideMap));

        FunctionManager.bindFunction("CustomPetNameTag");
        FunctionManager.addConfiguration(new SettingString("规则为\"原始字符=要替换的字符\",如果添加多项规则请使用\",\"分割,如果想清空自定义名称规则请填写null", "petName", "null"));
        FunctionManager.addConfiguration(new SettingInt("宠物等级,如果想取消自定义等级请填写0","petLevel",0));

        FunctionManager.bindFunction("MouseISwitch");
        FunctionManager.addConfiguration(new SettingString("物品名称", "itemName", "Shadow Fu"));
        FunctionManager.addConfiguration(new SettingBoolean("左键触发", "leftTrigger", true));

        FunctionManager.bindFunction("AutoKillWorm");
        FunctionManager.addConfiguration(new SettingLimitInt("间隔时间","cooldown",300,Integer.MAX_VALUE,0));
        FunctionManager.addConfiguration(new SettingBoolean("瞄准Worm", "aim", false));
        FunctionManager.addConfiguration(new SettingString("右键物品名称", "itemName", "staff of the vol"));
        FunctionManager.addConfiguration(new SettingInt("右键次数", "rcCount", 1));
        FunctionManager.addConfiguration(new SettingInt("右键延迟(游戏刻)", "rcCooldown", 10));
        FunctionManager.addConfiguration(new SettingInt("计时器位置(X)", "timerX", 200));
        FunctionManager.addConfiguration(new SettingInt("计时器位置(Y)", "timerY", 115));

        FunctionManager.bindFunction("AntimonyChannel");
        FunctionManager.addConfiguration(new SettingBoolean("重连提示", "notice", true));

        FunctionManager.bindFunction("NoSlow");
        FunctionManager.addConfiguration(new SettingLimitDouble("剑格挡减速效果","sword",0.5D,1.0D,0.2D));
        FunctionManager.addConfiguration(new SettingLimitDouble("拉弓减速效果","bow",1.0D,1.0D,0.2D));
        FunctionManager.addConfiguration(new SettingLimitDouble("进食减速效果","eat",1.0D,1.0D,0.2D));

        HashMap<String, Integer> BackgroundStyleMap = new HashMap<String, Integer>();
        BackgroundStyleMap.put("Vanilla",0);
        BackgroundStyleMap.put("Blur",1);
        FunctionManager.bindFunction("Interface");
        FunctionManager.addConfiguration(new SettingBoolean("FPS显示", "fps", false));
        FunctionManager.addConfiguration(new SettingInt("FPS位置(X)", "fpsX", 200));
        FunctionManager.addConfiguration(new SettingInt("FPS位置(Y)", "fpsY", 130));
        FunctionManager.addConfiguration(new SettingBoolean("坐标显示", "location", false));
        FunctionManager.addConfiguration(new SettingInt("坐标位置(X)", "locationX", 200));
        FunctionManager.addConfiguration(new SettingInt("坐标位置(Y)", "locationY", 145));
        FunctionManager.addConfiguration(new SettingBoolean("服务器天数显示", "day", false));
        FunctionManager.addConfiguration(new SettingInt("天数位置(X)", "dayX", 200));
        FunctionManager.addConfiguration(new SettingInt("天数位置(Y)", "dayY", 190));
        FunctionManager.addConfiguration(new SettingTypeSelector("GUI背景样式","bgstyle",1,BackgroundStyleMap));

        FunctionManager.bindFunction("ShortBowAura");
        FunctionManager.addConfiguration(new SettingLimitDouble("延迟", "delay", 3.0D,10.0D,1.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("距离", "range", 15.0D,100.0D,5.0D));
        FunctionManager.addConfiguration(new SettingBoolean("右键模式", "right", false));
        FunctionManager.addConfiguration(new SettingBoolean("攻击同队成员", "attackTeam", false));

        FunctionManager.bindFunction("AutoWolfSlayer");
        HashMap<String, Integer> SlayMode = new HashMap<String, Integer>();
        SlayMode.put("Killaura",0);
        SlayMode.put("ShortBowAura",1);
        FunctionManager.addConfiguration(new SettingTypeSelector("击杀Wolf模式","mode",0,SlayMode));
        FunctionManager.addConfiguration(new SettingString("近战物品名称", "swordName", "livid"));
        FunctionManager.addConfiguration(new SettingString("短弓名称", "bowName", "juju"));

        FunctionManager.bindFunction("ChestFinder");
        HashMap<String, Integer> renderMode = new HashMap<String, Integer>();
        renderMode.put("Outline",0);
        renderMode.put("Full",1);
        renderMode.put("Tracer",2);
        FunctionManager.addConfiguration(new SettingTypeSelector("模式","mode",0,renderMode));

        FunctionManager.bindFunction("AutoLeave");
        FunctionManager.addConfiguration(new SettingInt("检测半径", "radius",30));
        FunctionManager.addConfiguration(new SettingLimitInt("附近最大可存在玩家数量", "limit",0,100,0));
        FunctionManager.addConfiguration(new SettingLimitInt("附近玩家大于可存在玩家数量后执行命令前冷却(游戏Tick)", "tickLimit",200,Integer.MAX_VALUE,0));
        FunctionManager.addConfiguration(new SettingString("需执行的命令", "command", "/warp home"));
        FunctionManager.addConfiguration(new SettingBoolean("只提示声音不发送命令", "soundOnly", false));

        FunctionManager.bindFunction("CropBot");
        HashMap<String, Integer> crops = new HashMap<String, Integer>();
        crops.put("Potato",0);
        crops.put("Carrot",1);
        crops.put("Mushroom",2);
        crops.put("Nether Wart",3);
        FunctionManager.addConfiguration(new SettingTypeSelector("作物种类","crop",0,crops));
        FunctionManager.addConfiguration(new SettingInt("检测半径(设置太高小心卡死)", "radius",8));

        FunctionManager.bindFunction("MarketingGenerator");
        FunctionManager.addConfiguration(new SettingString("名称", "name", "Necron"));
        FunctionManager.addConfiguration(new SettingString("事件", "event","不能与Kuudra一起吃"));
        FunctionManager.addConfiguration(new SettingString("另一种解释", "explain", "容易Yikes"));

        FunctionManager.bindFunction("BlackList");
        HashMap<String, Integer> apiSource = new HashMap<String, Integer>();
        apiSource.put("API Source 1",0);
        apiSource.put("API Source 2",1);
        FunctionManager.addConfiguration(new SettingTypeSelector("API","api",0,apiSource));

        FunctionManager.bindFunction("PlayerFinder");
        FunctionManager.addConfiguration(new SettingBoolean("不显示头顶假人与NPC", "showNpc", true));
        HashMap<String, Integer> playerFinderMode = new HashMap<String, Integer>();
        playerFinderMode.put("3D Box",0);
        playerFinderMode.put("XRay",1);
        FunctionManager.addConfiguration(new SettingTypeSelector("渲染模式","mode",1,playerFinderMode));

        FunctionManager.bindFunction("Nuker");
        HashMap<String, Integer> rotation = new HashMap<String, Integer>();
        rotation.put("ServerRotation",0);
        rotation.put("Rotation",1);
        FunctionManager.addConfiguration(new SettingTypeSelector("旋转模式","rotation",0,rotation));
        HashMap<String, Integer> nukerType = new HashMap<String, Integer>();
        nukerType.put("Gemstone(No Panel)",0);
        nukerType.put("Gemstone",1);
        nukerType.put("Ore",2);
        nukerType.put("Stone",3);
        nukerType.put("Netherrack",4);
        nukerType.put("Sand",5);
        nukerType.put("Gold Block",6);
        nukerType.put("Mithril",7);
        nukerType.put("Frozen Treasure",8);
        FunctionManager.addConfiguration(new SettingTypeSelector("模式","type",0,nukerType));

        NewUserFunction();

        reloadKeyMapping();

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    public void NewUserFunction() {
        if (ConfigLoader.getBoolean("newUser", true)) {
            FunctionManager.setStatus("HUD", true);
            FunctionManager.setStatus("AntimonyChannel", true);

            ConfigLoader.setBoolean("newUser", false, true);
        }
    }
    public static void reloadKeyMapping(){
        KeyBinding.clear();
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
            int keyCode = ConfigLoader.getInt(function.getName() + "_KeyBindValue",-114514);
            if(keyCode != -114514){
                KeyBinding.put(function,keyCode);
            }
        }
    }
}
