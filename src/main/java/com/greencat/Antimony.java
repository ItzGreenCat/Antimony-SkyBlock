package com.greencat;

import com.greencat.common.EventLoader;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.command.CommandManager;
import com.greencat.common.command.DevCommand;
import com.greencat.common.config.ConfigLoader;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.common.function.*;
import com.greencat.common.function.rank.CustomRank;
import com.greencat.common.function.rank.RankList;
import com.greencat.common.function.title.TitleManager;
import com.greencat.common.key.KeyLoader;
import com.greencat.common.register.GCARegister;
import com.greencat.extranal.LoadScreen;
import com.greencat.type.AntimonyFunction;
import com.greencat.type.SelectObject;
import com.greencat.type.SelectTable;
import com.greencat.utils.Chroma;
import com.greencat.utils.Utils;
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
import java.io.File;
import java.io.IOException;

@Mod(modid = Antimony.MODID, name = Antimony.NAME, version = Antimony.VERSION, acceptedMinecraftVersions = "1.8.9", clientSideOnly = true)
public class Antimony {
        public static final String MODID = "antimony";
        public static final String NAME = "Antimony-SkyBlock";
        public static final String VERSION = "2.0.9.2";
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
        Utils utils = new Utils();
        public static Boolean LabymodInstallCheck;

        @Instance(Antimony.MODID)
        public static Antimony instance;

        @EventHandler
        public void preInit(FMLPreInitializationEvent event) throws IOException {
            // TODO



                new LoadScreen();
                LoadScreen.LoadingFrame.setBounds(Display.getX(),Display.getY(),Minecraft.getMinecraft().displayWidth,Minecraft.getMinecraft().displayHeight);
                LoadScreen.LoadingFrame.add(LoadScreen.Panel);
                LoadScreen.Panel.add(LoadScreen.text);
                //LoadScreen.LoadingFrame.setVisible(true);
                new com.greencat.common.config.ConfigLoader(event);
        }

        @EventHandler
        public void init(FMLInitializationEvent event) throws AWTException {
            // TODO
                new EventLoader();
                new KeyLoader();
                if(!AntimonyDirectory.exists()){
                        AntimonyDirectory.mkdir();
                }
                ClientCommandHandler.instance.registerCommand(new CommandManager());
                ClientCommandHandler.instance.registerCommand(new DevCommand());
                LabymodInstallCheck = utils.ModLoadCheck("labymod");
                AutoFish autoFish = new AutoFish();
                autoFish.AutoFishEventRegiser();

                if(Minecraft.getMinecraft().gameSettings.gammaSetting > 1){
                        Minecraft.getMinecraft().gameSettings.gammaSetting = 0;
                }

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

                new RankList();
                new CustomRank();
                //Dev
                //--

                GCARegister register = new GCARegister();
                register.RegisterFunction(new AntimonyFunction("AutoClicker"));
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


                register.RegisterSelectObject(new SelectObject("table","Combat","root"));
                register.RegisterSelectObject(new SelectObject("table","Render","root"));
                register.RegisterSelectObject(new SelectObject("table","Dungeon","root"));
                register.RegisterSelectObject(new SelectObject("table","Macro","root"));
                register.RegisterSelectObject(new SelectObject("table","Mining","root"));
                register.RegisterSelectObject(new SelectObject("table","Movement","root"));
                register.RegisterSelectObject(new SelectObject("table","Misc","root"));
                register.RegisterSelectObject(new SelectObject("table","Fun","root"));

                register.RegisterSelectObject(new SelectObject("function","AutoClicker","Combat"));
                register.RegisterSelectObject(new SelectObject("function","AutoCannon","Combat"));

                register.RegisterSelectObject(new SelectObject("function","SilverfishESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","GuardianESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","GolemESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","WormLavaESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","LanternESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","PlayerFinder","Render"));
                register.RegisterSelectObject(new SelectObject("function","FullBright","Render"));
                register.RegisterSelectObject(new SelectObject("function","DroppedItemESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","NoHurtCam","Render"));

                register.RegisterSelectObject(new SelectObject("function","StarredMobESP","Dungeon"));
                register.RegisterSelectObject(new SelectObject("function","DungeonKeyESP","Dungeon"));
                register.RegisterSelectObject(new SelectObject("function","TerminalESP","Dungeon"));
                register.RegisterSelectObject(new SelectObject("function","HideDungeonMobNameTag","Dungeon"));
                register.RegisterSelectObject(new SelectObject("function","SecretBot","Dungeon"));
                register.RegisterSelectObject(new SelectObject("function","GhostBlock","Dungeon"));
                register.RegisterSelectObject(new SelectObject("function","LividESP","Dungeon"));

                register.RegisterSelectObject(new SelectObject("function","ZealotMinion","Macro"));
                register.RegisterSelectObject(new SelectObject("function","AutoFish","Macro"));
                register.RegisterSelectObject(new SelectObject("function","AutoKillWorm","Macro"));

                register.RegisterSelectObject(new SelectObject("function","GemstoneHidePane","Mining"));

                register.RegisterSelectObject(new SelectObject("function","AntiAFKJump","Movement"));
                register.RegisterSelectObject(new SelectObject("function","Sprint","Movement"));
                register.RegisterSelectObject(new SelectObject("function","Eagle","Movement"));
                register.RegisterSelectObject(new SelectObject("function","Velocity","Movement"));


                register.RegisterSelectObject(new SelectObject("function","SkeletonAim","Misc"));
                register.RegisterSelectObject(new SelectObject("function","ClassicGui","Misc"));
                register.RegisterSelectObject(new SelectObject("function","InstantSwitch","Misc"));
                register.RegisterSelectObject(new SelectObject("function","MouseISwitch","Misc"));

                register.RegisterSelectObject(new SelectObject("function","CustomPetNameTag","Fun"));
                register.RegisterSelectObject(new SelectObject("function","CustomItemSound","Fun"));
                register.RegisterSelectObject(new SelectObject("function","Rat","Fun"));


                register.RegisterSelectObject(new SelectObject("function","ItemTranslate","root"));


                ConfigLoader.applyFunctionState();

                FunctionManager.setStatus("CustomPetNameTag",false);
                LoadScreen.text.setText(Minecraft.getMinecraft().debug);




        }

        @EventHandler
        public void postInit(FMLPostInitializationEvent event)
        {
            // TODO
                //LoadScreen.LoadingFrame.setVisible(false);
        }
}
