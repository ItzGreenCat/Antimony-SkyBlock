package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AutoSS{

    public AutoSS(){
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }

    private static BlockPos clickPos;
    private boolean clicked;
    private boolean clickedButton;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer != null && isInDungeon() && FunctionManager.getStatus("AutoSS")) {
            if (Minecraft.getMinecraft().thePlayer.getPositionEyes(0.0F).distanceTo(new Vec3((double) 309, (double) 121, (double) 290)) < 5.5D && !this.clicked && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(309, 121, 290)).getBlock() == Blocks.stone_button) {
                this.clickBlock(new BlockPos(309, 121, 290));
                this.clicked = true;
                this.clickedButton = false;
            }

            if (clickPos != null && Minecraft.getMinecraft().thePlayer.getDistance((double)clickPos.getX(), (double)((float)clickPos.getY() - Minecraft.getMinecraft().thePlayer.getEyeHeight()), (double)clickPos.getZ()) < 5.5D && !this.clickedButton && Minecraft.getMinecraft().theWorld.getBlockState(clickPos).getBlock() == Blocks.stone_button) {
                for (int i = 0; i < 20; ++i) {
                    this.clickBlock(clickPos);
                }

                clickPos = null;
                this.clickedButton = true;
            }

        }
    }

    @SubscribeEvent
    public void onBlockChange(CustomEventHandler.BlockChangeEvent event) {
        if (this.clicked && !this.clickedButton && event.state.getBlock() == Blocks.sea_lantern && event.pos.getX() == 310 && event.pos.getY() >= 120 && event.pos.getY() <= 123 && event.pos.getZ() >= 291 && event.pos.getZ() <= 294) {
            clickPos = new BlockPos(event.pos.getX() - 1, event.pos.getY(), event.pos.getZ());
        }

    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        this.clicked = false;
        clickPos = null;
        this.clickedButton = false;
    }

    private void clickBlock(BlockPos hitPos) {
        Vec3 hitVec = new Vec3(0.0D, 0.0D, 0.0D);
        float f = (float) (hitVec.xCoord - (double) hitPos.getX());
        float f1 = (float) (hitVec.yCoord - (double) hitPos.getY());
        float f2 = (float) (hitVec.zCoord - (double) hitPos.getZ());
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(hitPos, EnumFacing.fromAngle((double)Minecraft.getMinecraft().thePlayer.rotationYaw).getIndex(), Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), f, f1, f2));
    }
    private boolean isInDungeon() {
        boolean isInTheCatacombs = false;
        Utils utils = new Utils();
        List<String> scoreBoardLines = utils.getSidebarLines();
        int size = scoreBoardLines.size() - 1;
        final String combatZoneName = "the catacombs";
        final String clearedName = "dungeon cleared";
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), clearedName)) {
                isInTheCatacombs = true;
            }
            if (Utils.containedByCharSequence(scoreBoardLines.get(size - i).toLowerCase(), combatZoneName) && !scoreBoardLines.get(size - i).toLowerCase().contains("to")) {
                isInTheCatacombs = true;
            }
        }
        return isInTheCatacombs;
    }
}