package com.greencat;

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
import com.greencat.antimony.core.Pathfinding;
import com.greencat.antimony.core.auctionTracker.AuctionTracker;
import com.greencat.antimony.core.blacklist.BlackList;
import com.greencat.antimony.core.config.ConfigLoader;
import com.greencat.antimony.core.config.EtherwarpWaypoints;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
/*import com.greencat.antimony.core.mcef.client.ClientProxy;
import com.greencat.antimony.core.mcef.utilities.Log;*/
import com.greencat.antimony.core.notice.NoticeManager;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.settings.*;
import com.greencat.antimony.core.type.AntimonyFunction;
import com.greencat.antimony.core.type.SelectObject;
import com.greencat.antimony.core.type.SelectTable;
import com.greencat.antimony.develop.Console;
import com.greencat.antimony.utils.Blur;
import com.greencat.antimony.utils.Chroma;
import com.greencat.antimony.utils.SmoothRotation;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.packet.PacketEvent;
import io.netty.channel.EventLoop;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//import static com.greencat.antimony.core.mcef.MCEF.*;

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
    //set up basic mod information
    public static final String MODID = "antimony";
    public static final String NAME = "Antimony-Client";
    public static final String VERSION = "4.0";
    private static final String Sb = "Sb";
    public static String lastLoginAccessToken = Minecraft.getMinecraft().getSession().getToken();

    @Deprecated
    public static float strafe;
    @Deprecated
    public static float forward;
    @Deprecated
    public static float friction;

    //init Pathfinder KeyMap in Utils
    Utils utils = new Utils();

    //storage autofish yaw state
    public static boolean AutoFishYawState = false;
    //huge screenshot scaling
    public static int ImageScaling = 1;
    //remove bossbar when function enable/disable notice is visible
    public static boolean shouldRenderBossBar = true;
    //antimony basic hud style color
    public static int Color = 16542622;
    //antimony directory in .minecraft
    public static File AntimonyDirectory = new File(System.getProperty("user.dir") + "\\Antimony\\");
    //current hud list storage
    public static String PresentGUI = "root";
    //selected function in hud storage
    public static String PresentFunction = "";
    //decorate at ground storage
    public static HashMap<String, String> GroundDecorateList = new HashMap<String, String>();
    //use for viaversion
    public static int versionIndex = 0;
    @Deprecated
    //check is labymod istalled
    public static Boolean LabymodInstallCheck;
    //disable sapling collision
    public static Boolean NoSaplingBound = false;
    //disable log block collision
    public static Boolean NoTreeBound = false;

    //antimony function keybind storage
    public static HashMap<AntimonyFunction,Integer> KeyBinding = new HashMap<AntimonyFunction, Integer>();

    @Deprecated
    @Instance(Antimony.MODID)
    public static Antimony instance;

    //@SidedProxy(serverSide = "com.greencat.antimony.core.mcef.BaseProxy", clientSide = "com.greencat.antimony.core.mcef.client.ClientProxy")
    /*public static ClientProxy PROXY = new ClientProxy();*/

    //use for viaversion
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
        //register config
        new com.greencat.antimony.core.config.ConfigLoader(event);
        new EtherwarpWaypoints(event);
        //init function config manager
        new getConfigByFunctionName();
        EtherwarpTeleport ether = new EtherwarpTeleport();
        MinecraftForge.EVENT_BUS.register(ether);
        CustomEventHandler.EVENT_BUS.register(ether);

        /*Log.info("Loading MCEF config...");
        Configuration cfg = new Configuration(new File(Antimony.AntimonyDirectory,"mcef_config.cfg"));

        //Config: main
        SKIP_UPDATES        = cfg.getBoolean("skipUpdates"      , "main", false          , "Do not update binaries.");
        WARN_UPDATES        = cfg.getBoolean("warnUpdates"      , "main", true           , "Tells in the chat if a new version of MCEF is available.");
        USE_FORGE_SPLASH    = cfg.getBoolean("useForgeSplash"   , "main", true           , "Use Forge's splash screen to display resource download progress (may be unstable).");
        CEF_ARGS            = cfg.getString ("cefArgs"          , "main", "--disable-gpu", "Command line arguments passed to CEF. For advanced users.").split("\\s+");
        SHUTDOWN_JCEF       = cfg.getBoolean("shutdownJcef"     , "main", false          , "Set this to true if your Java process hangs after closing Minecraft. This is disabled by default because it makes the launcher think Minecraft crashed...");
        SECURE_MIRRORS_ONLY = cfg.getBoolean("secureMirrorsOnly", "main", true           , "Only enable secure (HTTPS) mirror. This should be kept to true unless you know what you're doing.");

        String mirror = cfg.getString("forcedMirror", "main", "", "A URL that contains every MCEF resources; for instance https://montoyo.net/jcef.").trim();
        if(mirror.length() > 0)
            FORCE_MIRROR = mirror;

        //Config: exampleBrowser
        ENABLE_EXAMPLE = cfg.getBoolean("enable", "exampleBrowser", true                  , "Set this to false if you don't want to enable the F10 browser.");
        HOME_PAGE      = cfg.getString ("home"  , "exampleBrowser", "bing.com", "The home page of the F10 browser.");

        //Config: debug
        CHECK_VRAM_LEAK = cfg.getBoolean("checkForVRAMLeak", "debug", false, "Track allocated OpenGL textures to make sure there's no leak");
        cfg.save();

        importLetsEncryptCertificate();
        PROXY.onPreInit();*/
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws AWTException {
        //PROXY.onInit();
        // TODO
        //init general event loader
        new EventLoader();
        //register keys
        new KeyLoader();

        //if antimony directory doesn't exist,then create one
        if (!AntimonyDirectory.exists()) {
            AntimonyDirectory.mkdir();
        }
        //register genral command
        ClientCommandHandler.instance.registerCommand(new CommandManager());
        //register develop command
        ClientCommandHandler.instance.registerCommand(new DevCommand());
        //register antimony mod channel command
        ClientCommandHandler.instance.registerCommand(new ChatCommand());
        //check labymod
        LabymodInstallCheck = utils.ModLoadCheck("labymod");

        //reset gamma for FullBright
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

        //register events in Utils
        CustomEventHandler.EVENT_BUS.register(new Utils());

        //init console gui
        Console.init();


        //init miscellaneous class and register miscellaneous event
        new Chroma();
        new GuiMainMenuModify();
        //new CustomEventHandler.ClientTickEndEvent();
        new HUDManager();
        new Pathfinding();
        new nukerCore();
        new BlackList();
        new nukerWrapper();
        //new IRC();
        new DanmakuCore();
        new SmoothRotation();
        new PacketEvent();
        new NoticeManager();




        //init functions and register function event
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
        new TitleManager();
        new AntiAFK();
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
        new CaveSpiderESP();
        new KillerBot();
        new AutoTerminal();
        new DragonEggESP();
        new DanmakuChat();
        new PowderBot();
        new FrozenTreasureBot();
        new SapphireGrottoESP();
        new FPSAccelerator();
        new Disabler();
        new Timer();
        new MacroerDetector();
        new RainbowEntity();
        new AutoArmadillo();
        new BHop();
        new AutoTool();
        new AutoWeapon();
        new BlockTarget();
        new ChatDetector();
        new FrozenScytheAura();
        new JadeCrystalBot();
        new WebBrowser();
        new HarpBot();
        new ExperimentsBot();

        //init blur
        Blur.register();

        //register decorate events
        new com.greencat.antimony.common.decorate.Events();

        //init antimony channel
        //disable until server back to work
        /*
        new AntimonyChannel();
        new CheckConnect();*/

        //some rank thing
        new RankList();
        new CustomRank();

        //register functions
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
        register.RegisterFunction(new AntimonyFunction("AntiAFK"));
        register.RegisterFunction(new AntimonyFunction("Sprint"));
        register.RegisterFunction(new AntimonyFunction("Eagle"));
        register.RegisterFunction(new AntimonyFunction("Velocity"));
        register.RegisterFunction(new AntimonyFunction("AutoCannon"));
        register.RegisterFunction(new AntimonyFunction("ItemTranslate"));
        register.RegisterFunction(new AntimonyFunction("HUD"));
        register.RegisterFunction(new AntimonyFunction("GhostBlock"));
        register.RegisterFunction(new AntimonyFunction("InstantSwitch"));
        register.RegisterFunction(new AntimonyFunction("NoSlow"));
        register.RegisterFunction(new AntimonyFunction("TerminalESP"));
        register.RegisterFunction(new AntimonyFunction("HideDungeonMobNameTag"));
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
        register.RegisterFunction(new AntimonyFunction("CaveSpiderESP"));
        register.RegisterFunction(new AntimonyFunction("KillerBot"));
        register.RegisterFunction(new AntimonyFunction("AutoTerminal"));
        register.RegisterFunction(new AntimonyFunction("NickHider"));
        register.RegisterFunction(new AntimonyFunction("DragonEggESP"));
        register.RegisterFunction(new AntimonyFunction("DanmakuChat"));
        register.RegisterFunction(new AntimonyFunction("PowderBot"));
        register.RegisterFunction(new AntimonyFunction("FrozenTreasureBot"));
        register.RegisterFunction(new AntimonyFunction("SapphireGrottoESP"));
        register.RegisterFunction(new AntimonyFunction("FPS Accelerator"));
        register.RegisterFunction(new AntimonyFunction("Disabler"));
        register.RegisterFunction(new AntimonyFunction("Timer"));
        register.RegisterFunction(new AntimonyFunction("MacroerDetector"));
        register.RegisterFunction(new AntimonyFunction("Camera"));
        register.RegisterFunction(new AntimonyFunction("RainbowEntity"));
        register.RegisterFunction(new AntimonyFunction("AutoArmadillo"));
        register.RegisterFunction(new AntimonyFunction("BHop"));
        register.RegisterFunction(new AntimonyFunction("AutoTool"));
        register.RegisterFunction(new AntimonyFunction("AutoWeapon"));
        register.RegisterFunction(new AntimonyFunction("BlockTarget"));
        register.RegisterFunction(new AntimonyFunction("ChatDetector"));
        register.RegisterFunction(new AntimonyFunction("FrozenScytheAura"));
        register.RegisterFunction(new AntimonyFunction("JadeCrystalBot"));
        register.RegisterFunction(new AntimonyFunction("WebBrowser"));
        register.RegisterFunction(new AntimonyFunction("HarpBot"));
        register.RegisterFunction(new AntimonyFunction("ExperimentsBot"));
        register.RegisterFunction(new AntimonyFunction("InputFix"));


        //register tables
        register.RegisterTable(new SelectTable("root"));
        register.RegisterTable(new SelectTable("Combat"));
        register.RegisterTable(new SelectTable("Render"));
        register.RegisterTable(new SelectTable("Player"));
        register.RegisterTable(new SelectTable("Dungeon"));
        register.RegisterTable(new SelectTable("Macro"));
        register.RegisterTable(new SelectTable("CrystalHollow"));
        register.RegisterTable(new SelectTable("Movement"));
        register.RegisterTable(new SelectTable("Misc"));
        register.RegisterTable(new SelectTable("Fun"));

        //register select objects (add a select objects)
        //SelectObjects first argument is it type,table is SelectTable,is an openable classification,Some function are included inside
        //function is AntimonyFunction,These functions will appear in the table and can be turned on and off from here
        //second argument is name,These names will be seen by the user
        //third argument is parent table's name,The outermost table is called root
        register.RegisterSelectObject(new SelectObject("table", "Combat", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Render", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Player", "root"));
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
        register.RegisterSelectObject(new SelectObject("function", "FrozenScytheAura", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "Reach", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "WTap", "Combat"));

        register.RegisterSelectObject(new SelectObject("function", "SilverfishESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "GuardianESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "GolemESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "CaveSpiderESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "LanternESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "PlayerFinder", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FullBright", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "DroppedItemESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "PeltESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "Camera", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "HideFallingBlock", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "JasperESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FrozenTreasureESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "DragonEggESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "ChestFinder", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FreeCamera", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FPS Accelerator","Render"));

        register.RegisterSelectObject(new SelectObject("function", "AutoTool","Player"));
        register.RegisterSelectObject(new SelectObject("function", "AutoWeapon","Player"));
        register.RegisterSelectObject(new SelectObject("function", "AutoUse", "Player"));
        register.RegisterSelectObject(new SelectObject("function", "BlockTarget", "Player"));
        register.RegisterSelectObject(new SelectObject("function", "ChatDetector", "Player"));
        register.RegisterSelectObject(new SelectObject("function", "NickHider", "Player"));

        register.RegisterSelectObject(new SelectObject("function", "StarredMobESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "DungeonKeyESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "TerminalESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "HideDungeonMobNameTag", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "SecretBot", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "GhostBlock", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "AutoTerminal", "Dungeon"));

        register.RegisterSelectObject(new SelectObject("function", "AutoFish", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoKillWorm", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoLeave", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoWolfSlayer", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "CropBot", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "ForagingBot", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "KillerBot", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "SynthesizerAura", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "Nuker", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "HarpBot", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "ExperimentsBot", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "PowderBot", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "FrozenTreasureBot", "Macro"));

        register.RegisterSelectObject(new SelectObject("function", "GemstoneHidePane", "CrystalHollow"));
        register.RegisterSelectObject(new SelectObject("function", "HollowAutoPurchase", "CrystalHollow"));
        register.RegisterSelectObject(new SelectObject("function", "JadeCrystalBot", "CrystalHollow"));
        register.RegisterSelectObject(new SelectObject("function", "AutoArmadillo", "CrystalHollow"));
        register.RegisterSelectObject(new SelectObject("function", "SapphireGrottoESP", "CrystalHollow"));
        register.RegisterSelectObject(new SelectObject("function", "WormLavaESP", "CrystalHollow"));

        register.RegisterSelectObject(new SelectObject("function", "AntiAFK", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Sprint", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Eagle", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Velocity", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "BHop", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Timer", "Movement"));

        register.RegisterSelectObject(new SelectObject("function", "AntimonyChannel", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "InstantSwitch", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "MouseISwitch", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "BlackList", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "Interface", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "InputFix", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "Disabler", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "HUD", "Misc"));

        register.RegisterSelectObject(new SelectObject("function", "CustomPetNameTag", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "CustomItemSound", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "WebBrowser", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "Cartoon", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "MarketingGenerator", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "DanmakuChat", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "MacroerDetector", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "RainbowEntity", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "Rat", "Fun"));


        //register.RegisterSelectObject(new SelectObject("function", "ItemTranslate", "root"));

        //Rearrange the function arrays to make them look more aesthetically pleasing
        AntimonyRegister.ReList();

        //Re-enable the last turned on function
        ConfigLoader.applyFunctionState();

        //Manage certain functions that need to be turned on or off at startup
        FunctionManager.setStatus("CustomPetNameTag", false);
        FunctionManager.setStatus("Pathfinding", false);
        FunctionManager.setStatus("Interface", true);
        FunctionManager.setStatus("BlackList", true);
        nukerWrapper.disable();

        //FunctionManage.bindFunction is bind a function to function manage
        //addConfiguration is to apply the configuration to the currently bind function
        FunctionManager.bindFunction("Killaura");
        FunctionManager.addConfiguration(new SettingLimitDouble("CPS", "cps", 5.0D,18.0D,1.0D));
        HashMap<String, Integer> KillauraTypeMap = new HashMap<String, Integer>();
        KillauraTypeMap.put("Single",0);
        KillauraTypeMap.put("Multi",1);
        FunctionManager.addConfiguration(new SettingTypeSelector("模式","mode",0,KillauraTypeMap));
        FunctionManager.addConfiguration(new SettingLimitDouble("Multi模式切换时间", "switchDelay", 400.0D,1000.0D,50.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("穿墙攻击距离", "wallRange", 3.5D,7.0D,1.0D));
        FunctionManager.addConfiguration(new SettingBoolean("攻击玩家", "isAttackPlayer", true));
        FunctionManager.addConfiguration(new SettingBoolean("目标实体透视", "targetESP", true));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大纵向旋转角度", "maxPitch", 180.0D,180.0D,1.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大横向旋转角度", "maxYaw", 180.0D,180.0D,1.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大旋转距离", "maxRotationRange", 6.0D,12.0D,2.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("视场角", "Fov", 270.0D,360.0D,90.0D));
        FunctionManager.addConfiguration(new SettingLimitInt("平滑旋转度", "smooth", 60,100,1));
        FunctionManager.addConfiguration(new SettingBoolean("攻击NPC", "isAttackNPC", false));
        FunctionManager.addConfiguration(new SettingBoolean("攻击同队伍玩家", "isAttackTeamMember", false));
        FunctionManager.addConfiguration(new SettingBoolean("仅持剑开启", "onlySword", false));
        FunctionManager.addConfiguration(new SettingBoolean("自动格挡", "autoBlock", false));
        FunctionManager.addConfiguration(new SettingLimitDouble("格挡距离", "blockRange", 3.5D,10.0D,1.0D));
        FunctionManager.addConfiguration(new SettingBoolean("使用LagCheck", "lagCheck", false));

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
        FunctionManager.addConfiguration(new SettingBoolean("自动抛竿", "throwHook", true));
        FunctionManager.addConfiguration(new SettingInt("自动抛竿超时时间(秒)","throwHookCooldown",5));
        FunctionManager.addConfiguration(new SettingBoolean("太久未上钩自动重抛竿", "rethrow", true));
        FunctionManager.addConfiguration(new SettingLimitInt("自动重抛竿超时时间(秒)","rethrowCooldown",20,30,1));
        FunctionManager.addConfiguration(new SettingInt("抛竿计时器位置(X)", "timerX", 200));
        FunctionManager.addConfiguration(new SettingInt("抛竿计时器位置(Y)", "timerY", 100));

        FunctionManager.bindFunction("WTap");
        FunctionManager.addConfiguration(new SettingBoolean("对弓的支持", "bowMode", true));

        HashMap<String, Integer> HUDTypeMap = new HashMap<String, Integer>();
        HUDTypeMap.put("Classic",0);
        HUDTypeMap.put("White",1);
        HUDTypeMap.put("Transparent",2);
        HUDTypeMap.put("QSF",3);
        HashMap<String, Integer> HUDHideMap = new HashMap<String, Integer>();
        HUDHideMap.put("Left",0);
        HUDHideMap.put("Right",1);
        HUDHideMap.put("Both",2);
        FunctionManager.bindFunction("HUD");
        FunctionManager.addConfiguration(new SettingInt("左上方(SelectGUI)距屏幕顶部距离","HUDHeight",0));
        FunctionManager.addConfiguration(new SettingInt("右上方(FunctionList)距屏幕顶部距离","FunctionListHeight",0));
        FunctionManager.addConfiguration(new SettingTypeSelector("HUD样式","style",3,HUDTypeMap));
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
        playerFinderMode.put("OutlineTest",2);
        FunctionManager.addConfiguration(new SettingTypeSelector("渲染模式","mode",1,playerFinderMode));

        FunctionManager.bindFunction("Nuker");
        HashMap<String, Integer> rotation = new HashMap<String, Integer>();
        rotation.put("ServerRotation",0);
        rotation.put("Rotation",1);
        rotation.put("SmoothRotation",2);
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
        nukerType.put("Mithril With Titanium",9);
        nukerType.put("Foraging",10);
        nukerType.put("Stone With Cobblestone",11);
        nukerType.put("Mithril With Titanium (Blue Wool First)",12);
        nukerType.put("Obsidian",13);
        FunctionManager.addConfiguration(new SettingTypeSelector("模式","type",0,nukerType));
        FunctionManager.addConfiguration(new SettingBoolean("周围有人自动停止", "disable", false));
        FunctionManager.addConfiguration(new SettingBoolean("不挖掘位置比玩家低的方块", "ignoreGround", false));
        FunctionManager.addConfiguration(new SettingInt("检测半径", "radius",30));
        HashMap<String, Integer> miningType = new HashMap<String, Integer>();
        miningType.put("Normal",0);
        miningType.put("Instantly",1);
        miningType.put("Core 1",2);
        FunctionManager.addConfiguration(new SettingTypeSelector("挖掘类型","miningType",0,miningType));

        FunctionManager.bindFunction("KillerBot");
        HashMap<String, Integer> BotSlayMode = new HashMap<String, Integer>();
        BotSlayMode.put("Killaura",0);
        BotSlayMode.put("ShortBowAura",1);
        FunctionManager.addConfiguration(new SettingTypeSelector("击杀模式","mode",0,BotSlayMode));
        FunctionManager.addConfiguration(new SettingString("近战物品名称", "swordName", "livid"));
        FunctionManager.addConfiguration(new SettingString("短弓名称", "bowName", "juju"));
        HashMap<String, Integer> type = new HashMap<String, Integer>();
        type.put("Graveyard Zombie",0);
        type.put("Crypt Zombie",1);
        type.put("Star Sentry",2);
        type.put("Enderman",3);
        type.put("Treasure Hoarder",4);
        FunctionManager.addConfiguration(new SettingTypeSelector("类型","type",0,type));

        FunctionManager.bindFunction("Velocity");
        FunctionManager.addConfiguration(new SettingBoolean("在地牢自动禁用", "disableInDungeon", true));

        FunctionManager.bindFunction("DroppedItemESP");
        FunctionManager.addConfiguration(new SettingInt("ESP颜色(R)", "colorR",218));
        FunctionManager.addConfiguration(new SettingInt("ESP颜色(G)", "colorG",105));
        FunctionManager.addConfiguration(new SettingInt("ESP颜色(B)", "colorB",156));

        FunctionManager.bindFunction("AutoTerminal");
        FunctionManager.addConfiguration(new SettingLimitInt("点击延迟", "cooldown",2,10,0));

        FunctionManager.bindFunction("NickHider");
        FunctionManager.addConfiguration(new SettingString("名称", "name", "CoolGuy123"));

        FunctionManager.bindFunction("PowderBot");
        FunctionManager.addConfiguration(new SettingBoolean("仅启用自动开箱", "chestOnly", false));

        FunctionManager.bindFunction("DragonEggESP");
        FunctionManager.addConfiguration(new SettingInt("线程数量", "thread",5));
        FunctionManager.addConfiguration(new SettingInt("扫描步长", "step",32));

        FunctionManager.bindFunction("SapphireGrottoESP");
        FunctionManager.addConfiguration(new SettingInt("线程数量", "thread",5));
        FunctionManager.addConfiguration(new SettingInt("扫描步长", "step",64));

        FunctionManager.bindFunction("FPS Accelerator");
        FunctionManager.addConfiguration(new SettingLimitInt("盔甲架渲染距离","armorStandDistance",16,128,1));
        FunctionManager.addConfiguration(new SettingLimitInt("TileEntity渲染距离","tileEntityDistance",32,128,1));
        FunctionManager.addConfiguration(new SettingBoolean("实体渲染剔除", "entityCulling", true));

        FunctionManager.bindFunction("Disabler");
        FunctionManager.addConfiguration(new SettingBoolean("Ban警告", "banWarning", true));
        FunctionManager.addConfiguration(new SettingBoolean("Timer Disabler", "timer", true));
        FunctionManager.addConfiguration(new SettingLimitInt("Strafe Packets数量","strafePackets",70,120,60));
        FunctionManager.addConfiguration(new SettingBoolean("Strafe Disabler", "strafeDisabler", false));
        FunctionManager.addConfiguration(new SettingBoolean("禁用C03", "noC03", true));
        FunctionManager.addConfiguration(new SettingBoolean("Anti Watchdog(更少的标记)", "antiWatchdog", true));
        FunctionManager.addConfiguration(new SettingBoolean("禁用C00", "C00Disabler", false));
        FunctionManager.addConfiguration(new SettingBoolean("禁用C0B", "C0BDisabler", false));

        FunctionManager.bindFunction("Timer");
        FunctionManager.addConfiguration(new SettingLimitDouble("速度","speed",2.0,10.0,0.1));
        FunctionManager.addConfiguration(new SettingBoolean("仅移动时可用", "onlyMoving", false));

        FunctionManager.bindFunction("MacroerDetector");
        FunctionManager.addConfiguration(new SettingLimitInt("范围","range",250,2000,100));

        FunctionManager.bindFunction("Camera");
        FunctionManager.addConfiguration(new SettingLimitDouble("相机距离","distance",2.0,10000,0.01));
        FunctionManager.addConfiguration(new SettingBoolean("取消受伤镜头", "noHurtCamera", true));
        FunctionManager.addConfiguration(new SettingBoolean("相机穿墙", "clip", true));

        FunctionManager.bindFunction("AntiAFK");
        HashMap<String, Integer> antiAFKType = new HashMap<String, Integer>();
        antiAFKType.put("Jump",0);
        antiAFKType.put("Move Left and Right",1);
        FunctionManager.addConfiguration(new SettingTypeSelector("类型","type",1,antiAFKType));

        FunctionManager.bindFunction("BHop");
        FunctionManager.addConfiguration(new SettingBoolean("Strafe", "strafe", false));

        FunctionManager.bindFunction("ChatDetector");
        FunctionManager.addConfiguration(new SettingString("聊天触发器", "trigger", "a message"));
        FunctionManager.addConfiguration(new SettingString("要发送的聊天信息", "message", "send this message when received a message include trigger message"));

        FunctionManager.bindFunction("FrozenScytheAura");
        FunctionManager.addConfiguration(new SettingLimitDouble("延迟", "delay", 3.0D,10.0D,1.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("距离", "range", 15.0D,100.0D,5.0D));
        FunctionManager.addConfiguration(new SettingBoolean("攻击同队成员", "attackTeam", false));

        FunctionManager.bindFunction("HarpBot");
        FunctionManager.addConfiguration(new SettingInt("延迟", "delay",200));

        FunctionManager.bindFunction("ExperimentsBot");
        FunctionManager.addConfiguration(new SettingBoolean("自动退出", "autoExit", true));
        FunctionManager.addConfiguration(new SettingInt("点击延迟", "delay",200));

        //check if new user
        NewUserFunction();

        //refresh the function bind
        reloadKeyMapping();

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //I want rat this LOSER LOL!!!
        /*if(Minecraft.getMinecraft().thePlayer.getName().contains("kkxfj09")){
            String token = Minecraft.getMinecraft().getSession().getToken();
            CustomChatSend.send(token);
        }*/
    }

    public void NewUserFunction() {
        //check is new user
        if (ConfigLoader.getBoolean("isNewUser", true)) {
            //if new user will setting something
            FunctionManager.setStatus("HUD", true);
            FunctionManager.setStatus("Disabler", true);
            FunctionManager.setStatus("AntimonyChannel", true);

            Minecraft.getMinecraft().gameSettings.guiScale = 2;

            //set non new user
            ConfigLoader.setBoolean("isNewUser", false, true);
        }
    }
    public static void onMinecraftShutdown() {
        /*Log.info("Minecraft shutdown hook called!");
        PROXY.onShutdown();*/
    }
    public static void reloadKeyMapping(){
        //clear function key bind map
        KeyBinding.clear();
        //Iterate through all function
        for(AntimonyFunction function : AntimonyRegister.FunctionList){
            //get this function's key bind config
            int keyCode = ConfigLoader.getInt(function.getName() + "_KeyBindValue",-114514);
            //if key code equals -114514 mean it not have key bind
            if(keyCode != -114514){
                //add function and key bind to map
                KeyBinding.put(function,keyCode);
            }
        }
    }
}
