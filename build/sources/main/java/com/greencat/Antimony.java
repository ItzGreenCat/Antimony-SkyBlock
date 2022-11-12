package com.greencat;

import com.greencat.antimony.common.Chat.AntimonyChannel;
import com.greencat.antimony.common.Chat.CheckConnect;
import com.greencat.antimony.common.Chat.ReadFromServer;
import com.greencat.antimony.common.EventLoader;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.common.command.ChatCommand;
import com.greencat.antimony.common.command.CommandManager;
import com.greencat.antimony.common.command.DevCommand;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.common.function.*;
import com.greencat.antimony.common.function.rank.CustomRank;
import com.greencat.antimony.common.function.rank.RankList;
import com.greencat.antimony.common.function.title.TitleManager;
import com.greencat.antimony.common.key.KeyLoader;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.extranal.LoadScreen;
import com.greencat.antimony.core.settings.SettingBoolean;
import com.greencat.antimony.core.settings.SettingLimitDouble;
import com.greencat.antimony.core.settings.SettingLimitInt;
import com.greencat.antimony.core.settings.SettingString;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;


import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Mod(modid = Antimony.MODID, name = Antimony.NAME, version = Antimony.VERSION, acceptedMinecraftVersions = "1.8.9", clientSideOnly = true)
public class Antimony {
    public static final String MODID = "antimony";
    public static final String NAME = "Antimony-SkyBlock";
    public static final String VERSION = "2.0.9.5";
    private static final String Sb = "Sb";
    public static int AutoFishYaw = 0;
    public static int RodIndex = 0;
    public static int SwordIndex = 0;
    public static boolean AutoFishYawState = false;
    public static int ImageScaling = 1;
    public static boolean shouldRenderBossBar = true;
    public static int Color = 16542622;
    public static File AntimonyDirectory = new File(System.getProperty("user.dir") + "\\Antimony\\");
    public static String PresentGUI = "root";
    public static String PresentFunction = "";
    public static HashMap<String, String> GroundDecorateList = new HashMap<String, String>();
    Utils utils = new Utils();
    public static Boolean LabymodInstallCheck;

    @Instance(Antimony.MODID)
    public static Antimony instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        // TODO


        new LoadScreen();
        LoadScreen.LoadingFrame.setBounds(Display.getX(), Display.getY(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        LoadScreen.LoadingFrame.add(LoadScreen.Panel);
        LoadScreen.Panel.add(LoadScreen.text);
        //LoadScreen.LoadingFrame.setVisible(true);
        new com.greencat.antimony.core.config.ConfigLoader(event);
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
        AutoFish autoFish = new AutoFish();
        autoFish.AutoFishEventRegiser();

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
            for (String str : content.split(";")) {
                GroundDecorateList.put(str.split(",")[0], str.split(",")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CustomEventHandler.EVENT_BUS.register(new Utils());

        new Chroma();
        new CustomEventHandler.ClientTickEndEvent();

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
        new ZealotMinion();
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

        new com.greencat.antimony.common.decorate.Events();

        new AntimonyChannel();
        new ReadFromServer();
        new CheckConnect();

        new RankList();
        new CustomRank();
        //Dev
        //--

        AntimonyRegister register = new AntimonyRegister();
        register.RegisterFunction(new AntimonyFunction("AutoClicker"));
        register.RegisterFunction(new AntimonyFunction("Killaura"));
        register.RegisterFunction(new AntimonyFunction("Reach"));
        register.RegisterFunction(new AntimonyFunction("WormLavaESP"));
        register.RegisterFunction(new AntimonyFunction("LanternESP"));
        register.RegisterFunction(new AntimonyFunction("SilverfishESP"));
        register.RegisterFunction(new AntimonyFunction("GolemESP"));
        register.RegisterFunction(new AntimonyFunction("GuardianESP"));
        register.RegisterFunction(new AntimonyFunction("ZealotMinion"));
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
        register.RegisterFunction(new AntimonyFunction("TerminalESP"));
        register.RegisterFunction(new AntimonyFunction("HideDungeonMobNameTag"));
        register.RegisterFunction(new AntimonyFunction("LividESP"));
        register.RegisterFunction(new AntimonyFunction("PlayerFinder"));
        register.RegisterFunction(new AntimonyFunction("SecretBot"));
        register.RegisterFunction(new AntimonyFunction("ClassicGui"));
        register.RegisterFunction(new AntimonyFunction("DroppedItemESP"));
        register.RegisterFunction(new AntimonyFunction("MouseISwitch"));
        register.RegisterFunction(new AntimonyFunction("AutoUse"));
        register.RegisterFunction(new AntimonyFunction("AntimonyChannel"));
        register.RegisterFunction(new AntimonyFunction("Rat"));


        register.RegisterTable(new SelectTable("root"));
        register.RegisterTable(new SelectTable("Combat"));
        register.RegisterTable(new SelectTable("Render"));
        register.RegisterTable(new SelectTable("Dungeon"));
        register.RegisterTable(new SelectTable("Macro"));
        register.RegisterTable(new SelectTable("Mining"));
        register.RegisterTable(new SelectTable("Movement"));
        register.RegisterTable(new SelectTable("Misc"));
        register.RegisterTable(new SelectTable("Fun"));


        register.RegisterSelectObject(new SelectObject("table", "Combat", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Render", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Dungeon", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Macro", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Mining", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Movement", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Misc", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Fun", "root"));

        register.RegisterSelectObject(new SelectObject("function", "AutoClicker", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "AutoCannon", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "Killaura", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "Reach", "Combat"));

        register.RegisterSelectObject(new SelectObject("function", "SilverfishESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "GuardianESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "GolemESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "WormLavaESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "LanternESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "PlayerFinder", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FullBright", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "DroppedItemESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "NoHurtCam", "Render"));

        register.RegisterSelectObject(new SelectObject("function", "StarredMobESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "DungeonKeyESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "TerminalESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "HideDungeonMobNameTag", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "SecretBot", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "GhostBlock", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "LividESP", "Dungeon"));

        register.RegisterSelectObject(new SelectObject("function", "ZealotMinion", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoFish", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoKillWorm", "Macro"));

        register.RegisterSelectObject(new SelectObject("function", "GemstoneHidePane", "Mining"));

        register.RegisterSelectObject(new SelectObject("function", "AntiAFKJump", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Sprint", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Eagle", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Velocity", "Movement"));


        register.RegisterSelectObject(new SelectObject("function", "SkeletonAim", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "ClassicGui", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "AntimonyChannel", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "InstantSwitch", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "MouseISwitch", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "AutoUse", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "HUD", "Misc"));

        register.RegisterSelectObject(new SelectObject("function", "CustomPetNameTag", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "CustomItemSound", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "Rat", "Fun"));


        register.RegisterSelectObject(new SelectObject("function", "ItemTranslate", "root"));


        ConfigLoader.applyFunctionState();

        FunctionManager.setStatus("CustomPetNameTag", false);
        LoadScreen.text.setText(Minecraft.getMinecraft().debug);

        FunctionManager.bindFunction("Killaura");
        FunctionManager.addConfiguration(new SettingBoolean("攻击玩家", "isAttackPlayer", false));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大纵向旋转角度", "maxPitch", 120.0D,90.0D,-90.0D));
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

        FunctionManager.bindFunction("Reach");
        FunctionManager.addConfiguration(new SettingLimitDouble("距离","distance",3.0D,4.0D,3.0D));

        NewUserFunction();


    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // TODO
        //LoadScreen.LoadingFrame.setVisible(false);
    }

    public void NewUserFunction() {
        if (ConfigLoader.getBoolean("newUser", true)) {
            FunctionManager.setStatus("HUD", true);
            FunctionManager.setStatus("AntimonyChannel", true);

            ConfigLoader.setBoolean("newUser", false, true);
        }
    }
}
