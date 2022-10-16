package com.GreenCat.common;

import com.GreenCat.Antimony;
import com.GreenCat.common.FunctionManager.FunctionManager;
import com.GreenCat.common.FunctionManager.SelectGuiFunctionExecutant;
import com.GreenCat.common.function.AutoFish;
import com.GreenCat.common.key.KeyLoader;
import com.GreenCat.common.storage.SelectGUIStorage;
import com.GreenCat.common.ui.FunctionList;
import com.GreenCat.common.ui.NoticeManager;
import com.GreenCat.common.ui.SelectGUI;
import com.GreenCat.test.Screenshot;
import com.GreenCat.type.SelectTable;
import com.GreenCat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


import java.awt.*;
import java.util.List;

public class EventLoader {
    Utils utils = new Utils();
    NoticeManager n = new NoticeManager();
    SelectGUI selectGui = new SelectGUI();
    FunctionList functions = new FunctionList();
    SelectGuiFunctionExecutant exec = new SelectGuiFunctionExecutant();

    static Boolean AutoFishStatus = false;

    int KeyTime = 0;

    Boolean NowFunctionKeyStatus = true;

    int index = 0;

    public EventLoader() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void RenderEvent(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
            n.Notice();
            selectGui.draw();
            functions.draw();
        }
        if(!Antimony.shouldRenderBossBar) {
            if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event) {
        if (Minecraft.func_71410_x().field_71441_e != null) {
            //utils.print(String.valueOf(Minecraft.getMinecraft().thePlayer.rotationYaw));
            //AutoEnderman
            if (FunctionManager.getStatus("ZealotMinion")) {
                Double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
                Double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
                Double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
                List<EntityEnderman> entity = Minecraft.func_71410_x().field_71441_e.func_175647_a(EntityEnderman.class, new AxisAlignedBB(x - (60 / 2d), y - (100 / 2d), z - (60 / 2d), x + (60 / 2d), y + (100 / 2d), z + (60 / 2d)), null);
                if (!entity.isEmpty()) {
                    double[] PlayerLocationArray = {x, y, z};
                    double[] EndermanLocationArray = {entity.get(0).field_70165_t, entity.get(0).field_70163_u, entity.get(0).field_70161_v};
                    if (Minecraft.func_71410_x().field_71439_g.func_70694_bm() != null) {
                        Minecraft.func_71410_x().field_71439_g.func_70080_a(x, y, z, (int) Minecraft.func_71410_x().field_71439_g.field_70177_z, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                        double Angle = utils.FlatAngle(x, z, entity.get(0).field_70165_t, entity.get(0).field_70161_v);
                        //utils.print(String.valueOf(Angle));
                        if (utils.RangeInDefined(Minecraft.func_71410_x().field_71439_g.field_70177_z, Angle - 3, Angle + 3)) {
                            Minecraft.func_71410_x().field_71442_b.func_78769_a(Minecraft.func_71410_x().field_71439_g, Minecraft.func_71410_x().field_71441_e, Minecraft.func_71410_x().field_71439_g.func_70694_bm());
                        }
                        if (Minecraft.func_71410_x().field_71439_g.field_70177_z > Angle) {
                            if (Minecraft.func_71410_x().field_71439_g.field_70177_z - Angle < 10) {
                                Minecraft.func_71410_x().field_71439_g.func_70080_a(x, y, z, Minecraft.func_71410_x().field_71439_g.field_70177_z - 2, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                            } else if (Minecraft.func_71410_x().field_71439_g.field_70177_z - Angle > 10) {
                                Minecraft.func_71410_x().field_71439_g.func_70080_a(x, y, z, Minecraft.func_71410_x().field_71439_g.field_70177_z - 8, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                            }
                        } else if (Minecraft.func_71410_x().field_71439_g.field_70177_z < Angle) {
                            if (Angle - Minecraft.func_71410_x().field_71439_g.field_70177_z < 10) {
                                Minecraft.func_71410_x().field_71439_g.func_70080_a(x, y, z, Minecraft.func_71410_x().field_71439_g.field_70177_z + 2, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                            } else if (Angle - Minecraft.func_71410_x().field_71439_g.field_70177_z > 10) {
                                Minecraft.func_71410_x().field_71439_g.func_70080_a(x, y, z, Minecraft.func_71410_x().field_71439_g.field_70177_z + 8, (float) utils.ErectAngle(PlayerLocationArray, EndermanLocationArray));
                            }
                        }


                    }
                }
            }
            //ArmorStandFind
            /*if (FunctionManager.getStatus("ArmorStandFinder")) {
                Double x = Minecraft.getMinecraft().thePlayer.posX;
                Double y = Minecraft.getMinecraft().thePlayer.posY;
                Double z = Minecraft.getMinecraft().thePlayer.posZ;
                List<EntityArmorStand> entity = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - (40 / 2d), y - (40 / 2d), z - (40 / 2d), x + (40 / 2d), y + (40 / 2d), z + (40 / 2d)), null);
                utils.print("----------当前附近盔甲架----------");
                for (EntityArmorStand entityNonList : entity) {
                    utils.print("发现盔甲架-坐标 X:" + entityNonList.posX + " Y:" + entityNonList.posY + " Z:" + entityNonList.posZ);
                }
                utils.print("---------------------------");

            }*/
            if (!FunctionManager.getStatus("AutoFish")){
                AutoFishStatus = false;
            }
        }
    }
    @SubscribeEvent
    public void onPacketReceived(PlaySoundEvent event) throws AWTException {
        if(Minecraft.func_71410_x().field_71441_e != null) {
            if (FunctionManager.getStatus("AutoFish")) {


                if (event.name.equals("game.player.swim.splash")) {
                    if (AutoFishStatus) {
                        new AutoFish();
                        AutoFishStatus = false;
                        utils.print("钓鱼检测状态:关闭");
                    } else {
                        AutoFishStatus = true;
                        utils.print("钓鱼检测状态:开启");
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void PLayerInteract(PlayerInteractEvent event) {
        if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            try{
                if (Minecraft.func_71410_x().field_71439_g.func_70694_bm().func_77973_b() == Items.field_151112_aM) {
                    if (AutoFishStatus) {
                        AutoFishStatus = false;
                        utils.print("钓鱼检测状态:关闭");
                    }
                }
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void OnKeyPressed(InputEvent.KeyInputEvent event) throws Exception {
        if (KeyLoader.SelectGuiUP.func_151468_f()) {
            for (SelectTable table : SelectGUIStorage.TableStorage) {
                if (table.getID().equals(SelectGUI.PresentGUI)) {
                    if (index - 1 < 0) {
                        index = table.getList().size() - 1;
                    } else {
                        index = index - 1;
                    }
                    if (index >= 0 && index < table.getList().size()) {
                        SelectGUI.PresentFunction = table.getList().get(index).getName();
                    }
                }
            }
        }
        if (KeyLoader.SelectGuiDown.func_151468_f()) {
            for (SelectTable table : SelectGUIStorage.TableStorage) {
                if (table.getID().equals(SelectGUI.PresentGUI)) {
                    if (index + 1 > table.getList().size() - 1) {
                        index = 0;
                    } else {
                        index = index + 1;
                    }
                    if (index >= 0 && index < table.getList().size()) {
                        SelectGUI.PresentFunction = table.getList().get(index).getName();
                    }
                }
            }
        }
            if (KeyLoader.SelectGuiEnter.func_151468_f()) {
                if(KeyTime == 0) {
                    KeyTime = 1;
                    for (SelectTable table : SelectGUIStorage.TableStorage) {
                        if (table.getID().equals(SelectGUI.PresentGUI)) {
                            if (table.getList().get(index).getType().equals("table")) {
                                exec.EnterTable(table.getList().get(index).getName());
                                exec.SetRunFunctionStatus(false);
                                NowFunctionKeyStatus = false;
                                index = 0;
                            } else if (table.getList().get(index).getType().equals("function")) {

                                if(!exec.getRunFunctionStatus()) {
                                    if(NowFunctionKeyStatus) {
                                        exec.SetRunFunctionStatus(true);
                                        exec.RunFunction(table.getList().get(index).getName());
                                    } else {
                                        NowFunctionKeyStatus = true;
                                    }
                                } else {
                                    if(NowFunctionKeyStatus) {
                                        exec.RunFunction(table.getList().get(index).getName());
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                KeyTime = 0;
            }
            if (KeyLoader.SelectGuiBack.func_151468_f()) {
                SelectGUI.PresentGUI = "root";
                for (SelectTable table : SelectGUIStorage.TableStorage) {
                    if (table.getID().equals(SelectGUI.PresentGUI)) {
                        SelectGUI.PresentFunction = table.getList().get(0).getName();
                    }
                }
                index = 0;
            }
        if (KeyLoader.HugeScreenshot.func_151468_f()) {
            Screenshot screenshot = new Screenshot();
            screenshot.CreateScreenshot(Antimony.ImageScaling);
        }

        }
    }

