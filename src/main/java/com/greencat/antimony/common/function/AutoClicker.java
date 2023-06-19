package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.PlayerControllerAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.config.ConfigInterface;
import com.greencat.antimony.core.register.AntimonyRegister;
import com.greencat.antimony.core.settings.SettingLimitInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class AutoClicker {
    public AutoClicker() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isEnable = false;
    Minecraft mc = Minecraft.getMinecraft();
    private int minCPSValue = 5;
    private int maxCPSValue = 8;
    private boolean rightValue = true;
    private boolean leftValue = true;
    private boolean jitterValue = false;
    
    final Random random = new Random();

    private long rightDelay = Timer.randomClickDelay(minCPSValue, maxCPSValue);
    private long rightLastSwing = 0L;
    private long leftDelay = Timer.randomClickDelay(minCPSValue, maxCPSValue);
    private long leftLastSwing = 0L;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (FunctionManager.getStatus("AutoClicker")) {
            isEnable = true;
            minCPSValue = (Integer) ConfigInterface.get("AutoClicker","minCPS");
            maxCPSValue = (Integer) ConfigInterface.get("AutoClicker","maxCPS");
            rightValue = (Boolean) ConfigInterface.get("AutoClicker","right");
            leftValue = (Boolean) ConfigInterface.get("AutoClicker","left");
            jitterValue = (Boolean) ConfigInterface.get("AutoClicker","jitter");
            if(minCPSValue > maxCPSValue){
                AntimonyRegister.FunctionList.entrySet().stream().filter(it -> it.getKey().equals("AutoClicker")).findFirst().ifPresent(it -> it.getValue().getConfigurationList().stream().filter(setting -> setting instanceof SettingLimitInt).forEach(setting -> {if(((SettingLimitInt) setting).ID.equals("minCPS")){((SettingLimitInt) setting).setText(String.valueOf(maxCPSValue));((SettingLimitInt) setting).setValue();}}));
                minCPSValue = maxCPSValue;
            }
        } else {
            isEnable = false;
        }
    }


    private long blockBrokenDelay = 1000L / 20 * (6 + 2);
    private long blockLastBroken = 0L;
    private boolean isBreakingBlock = false;
    private boolean wasBreakingBlock = false;

    private boolean leftCanAutoClick(long currentTime) {
        return !isBreakingBlock
                && !(currentTime - blockLastBroken < blockBrokenDelay &&
                mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && mc.theWorld != null &&
                mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() != Blocks.air);
    }

    private boolean rightCanAutoClick(){
        return !mc.thePlayer.isUsingItem();
    }

    private void leftClick(long currentTime) {
        if (leftValue && mc.gameSettings.keyBindAttack.isKeyDown()) {
            isBreakingBlock = ((PlayerControllerAccessor)mc.playerController).getCurBlockDamageMP() != 0F;
            if (!isBreakingBlock && wasBreakingBlock) {
                blockLastBroken = currentTime;
            }
            wasBreakingBlock = isBreakingBlock;
            if (currentTime - leftLastSwing < leftDelay || !leftCanAutoClick(currentTime)) {
                return;
            }
            KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());

            leftLastSwing = currentTime;
            blockLastBroken = 0L;
            leftDelay = Timer.randomClickDelay(minCPSValue, maxCPSValue);
        }
    }

    private void rightClick(long currentTime) {
        if (rightValue && mc.gameSettings.keyBindUseItem.isKeyDown() && currentTime - rightLastSwing >= rightDelay && rightCanAutoClick()) {
            KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());

            rightLastSwing = currentTime;
            rightDelay = Timer.randomClickDelay(minCPSValue, maxCPSValue);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(!isEnable){
            return;
        }
        long currentTime = System.currentTimeMillis();
        leftClick(currentTime);
        rightClick(currentTime);
    }

    @SubscribeEvent
    public void onTick2(TickEvent.ClientTickEvent event) {
        if(!isEnable){
            return;
        }
        if (jitterValue && ((leftValue && mc.gameSettings.keyBindAttack.isKeyDown() && leftCanAutoClick(System.currentTimeMillis()))
                || (rightValue && mc.gameSettings.keyBindUseItem.isKeyDown() && rightCanAutoClick()))) {
            if (Minecraft.getMinecraft().thePlayer == null) {
                return;
            }
            EntityPlayerSP thePlayer = mc.thePlayer;
            if (random.nextBoolean()) thePlayer.rotationYaw += random.nextBoolean() ? -nextFloat(0F, 1F) : nextFloat(0F, 1F);

            if (random.nextBoolean()) {
                thePlayer.rotationPitch += random.nextBoolean() ? -nextFloat(0F, 1F) : nextFloat(0F, 1F);

                if (thePlayer.rotationPitch > 90)
                    thePlayer.rotationPitch = 90F;
                else if (thePlayer.rotationPitch < -90)
                    thePlayer.rotationPitch = -90F;
            }
        }
    }
    public float nextFloat(float startInclusive, float endInclusive) {

        if (startInclusive == endInclusive) {
            return startInclusive;
        }

        return startInclusive + ((endInclusive - startInclusive) * random.nextFloat());
    }
    public static final class Timer {
        private static final Random random = new Random();
        public static long randomDelay(final int minDelay, final int maxDelay) {
            return nextInt(minDelay, maxDelay);
        }
        public static int nextInt(int startInclusive, int endExclusive) {
            if (startInclusive == endExclusive) {
                return startInclusive;
            }

            return startInclusive + random.nextInt(endExclusive - startInclusive);
        }

        public static long randomClickDelay(final int minCPS, final int maxCPS) {
            return (long) ((Math.random() * (1000 / minCPS - 1000 / maxCPS + 1)) + 1000 / maxCPS);
        }
    }
}
