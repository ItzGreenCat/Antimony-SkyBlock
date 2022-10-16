package com.GreenCat;

import com.GreenCat.common.EventLoader;
import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.common.command.CommandManager;
import com.GreenCat.common.command.DevCommand;
import com.GreenCat.common.config.ConfigLoader;
import com.GreenCat.common.function.*;
import com.GreenCat.common.function.rank.CustomRank;
import com.GreenCat.common.function.rank.RankList;
import com.GreenCat.common.function.title.TitleManager;
import com.GreenCat.common.key.KeyLoader;
import com.GreenCat.common.register.GCARegister;
import com.GreenCat.extranal.LoadScreen;
import com.GreenCat.type.AntimonyFunction;
import com.GreenCat.type.SelectObject;
import com.GreenCat.type.SelectTable;
import com.GreenCat.utils.Chroma;
import com.GreenCat.utils.Utils;
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

@Mod(modid = Antimony.MODID, name = Antimony.NAME, version = Antimony.VERSION, acceptedMinecraftVersions = "1.8.9", clientSideOnly = true)
public class Antimony {
        public static final String MODID = "antimony";
        public static final String NAME = "Antimony-SkyBlock";
        public static final String VERSION = "1.9.0";
        public static int AutoFishYaw = 0;
        public static int RodIndex = 0;
        public static int SwordIndex = 0;
        public static boolean AutoFishYawState = false;
        public static int ImageScaling = 1;
        public static boolean shouldRenderBossBar = true;
        public static int Color = 16542622;
        public static File GreenCatAddonDirectory = new File(System.getProperty("user.dir") + "\\Antimony\\");
        Utils utils = new Utils();
        public static Boolean LabymodInstallCheck;

        @Instance(Antimony.MODID)
        public static Antimony instance;

        @EventHandler
        public void preInit(FMLPreInitializationEvent event)
        {
            // TODO
                new LoadScreen();
                LoadScreen.LoadingFrame.setBounds(Display.getX(),Display.getY(),Minecraft.func_71410_x().field_71443_c,Minecraft.func_71410_x().field_71440_d);
                LoadScreen.LoadingFrame.add(LoadScreen.Panel);
                LoadScreen.Panel.add(LoadScreen.text);
                LoadScreen.LoadingFrame.setVisible(true);
                new com.GreenCat.common.config.ConfigLoader(event);
        }

        @EventHandler
        public void init(FMLInitializationEvent event) throws AWTException {
            // TODO
                new EventLoader();
                new KeyLoader();
                if(!GreenCatAddonDirectory.exists()){
                        GreenCatAddonDirectory.mkdir();
                }
                ClientCommandHandler.instance.func_71560_a(new CommandManager());
                ClientCommandHandler.instance.func_71560_a(new DevCommand());
                LabymodInstallCheck = utils.ModLoadCheck("labymod");
                AutoFish autoFish = new AutoFish();
                autoFish.AutoFishEventRegiser();
                AutoKillWorm autoKillWorm = new AutoKillWorm();
                autoKillWorm.EventRegister();

                if(Minecraft.func_71410_x().field_71474_y.field_74333_Y > 1){
                        Minecraft.func_71410_x().field_71474_y.field_74333_Y = 0;
                }

                new Chroma();

                new WormLavaESP();
                new SilverfishESP();
                new GolemESP();
                new GuardianESP();
                new GemstoneHidePane();
                new AutoPickaxe();
                new FullBright();
                new StarredMobESP();
                new DungeonKeyESP();
                new CustomPetName();
                new CustomItemSound();
                new LanternESP();
                new WitherSkeletonAim();
                new TitleManager();

                new RankList();
                new CustomRank();
                //Dev
                //--

                GCARegister register = new GCARegister();
                register.RegisterFunction(new AntimonyFunction("WormLavaESP"));
                register.RegisterFunction(new AntimonyFunction("LanternESP"));
                register.RegisterFunction(new AntimonyFunction("SilverfishESP"));
                register.RegisterFunction(new AntimonyFunction("GolemESP"));
                register.RegisterFunction(new AntimonyFunction("GuardianESP"));
                register.RegisterFunction(new AntimonyFunction("ZealotMinion"));
                register.RegisterFunction(new AntimonyFunction("AutoFish"));
                register.RegisterFunction(new AntimonyFunction("GemstoneHidePane"));
                register.RegisterFunction(new AntimonyFunction("AutoPickaxe"));
                register.RegisterFunction(new AntimonyFunction("FullBright"));
                register.RegisterFunction(new AntimonyFunction("StarredMobESP"));
                register.RegisterFunction(new AntimonyFunction("DungeonKeyESP"));
                register.RegisterFunction(new AntimonyFunction("CustomPetNameTag"));
                register.RegisterFunction(new AntimonyFunction("CustomItemSound"));
                register.RegisterFunction(new AntimonyFunction("WitherSkeletonAim"));


                register.RegisterTable(new SelectTable("root"));
                register.RegisterTable(new SelectTable("Render"));
                register.RegisterTable(new SelectTable("Dungeon"));
                register.RegisterTable(new SelectTable("Macro"));
                register.RegisterTable(new SelectTable("Mining"));
                register.RegisterTable(new SelectTable("Misc"));

                register.RegisterSelectObject(new SelectObject("table","Render","root"));
                register.RegisterSelectObject(new SelectObject("table","Dungeon","root"));
                register.RegisterSelectObject(new SelectObject("table","Macro","root"));
                register.RegisterSelectObject(new SelectObject("table","Mining","root"));
                register.RegisterSelectObject(new SelectObject("table","Misc","root"));


                register.RegisterSelectObject(new SelectObject("function","SilverfishESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","GuardianESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","GolemESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","WormLavaESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","LanternESP","Render"));
                register.RegisterSelectObject(new SelectObject("function","FullBright","Render"));

                register.RegisterSelectObject(new SelectObject("function","StarredMobESP","Dungeon"));
                register.RegisterSelectObject(new SelectObject("function","DungeonKeyESP","Dungeon"));

                register.RegisterSelectObject(new SelectObject("function","ZealotMinion","Macro"));
                register.RegisterSelectObject(new SelectObject("function","AutoFish","Macro"));

                register.RegisterSelectObject(new SelectObject("function","GemstoneHidePane","Mining"));
                //register.RegisterSelectObject(new SelectObject("function","AutoPickaxe","Mining"));

                register.RegisterSelectObject(new SelectObject("function","CustomPetNameTag","Misc"));
                register.RegisterSelectObject(new SelectObject("function","CustomItemSound","Misc"));
                register.RegisterSelectObject(new SelectObject("function","WitherSkeletonAim","Misc"));


                ConfigLoader.applyFunctionState();

                FunctionManager.setStatus("CustomPetNameTag",false);
                LoadScreen.text.setText(Minecraft.func_71410_x().field_71426_K);




        }

        @EventHandler
        public void postInit(FMLPostInitializationEvent event)
        {
            // TODO
                LoadScreen.LoadingFrame.setVisible(false);
        }
}
