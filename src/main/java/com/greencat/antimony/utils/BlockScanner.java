package com.greencat.antimony.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.greencat.antimony.utils.Utils.print;

public class BlockScanner {
    public boolean finished = false;
    public boolean running = false;
    public List<BlockPos> finalList = new ArrayList<BlockPos>();
    public List<Thread> threadList = new ArrayList<Thread>();
    List<AbstractMap.SimpleEntry<BlockPos, BlockPos>> cutBlockPos = null;
    private BlockPos pos1;
    private BlockPos pos2;
    private long startTime;
    private Block block;
    private int step;
    private int maxThread;
    private long blockSize = 0;

    public void start(BlockPos pos1, BlockPos pos2, Block block, int step, int maxThread) {
        finished = false;
        finalList.clear();
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.block = block;
        this.step = step;
        this.maxThread = maxThread;
        startTime = System.currentTimeMillis();
        cutBlockPos = null;
        running = true;
        MinecraftForge.EVENT_BUS.register(this);
        print("开始扫描");
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (event.phase == TickEvent.Phase.START) {
                if (!finished) {
                    if (cutBlockPos == null) {
                        cutBlockPos();
                    }
                    if (threadList.size() + 1 < maxThread) {
                        Thread scannerThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (cutBlockPos != null) {
                                    Map.Entry<BlockPos, BlockPos> currentEntry = null;
                                    currentEntry = cutBlockPos.get(0);
                                    for (BlockPos pos : BlockPos.getAllInBox(currentEntry.getKey(), currentEntry.getValue())) {
                                        if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == block) {
                                            finalList.add(pos);
                                        }
                                    }
                                }
                            }
                        });
                        scannerThread.start();
                        threadList.add(scannerThread);
                        cutBlockPos.remove(0);
                    }
                    List<Thread> shouldRemoved = new ArrayList<>();
                    for (Thread thread : threadList) {
                        if (!thread.isAlive()) {
                            shouldRemoved.add(thread);
                        }
                    }
                    for (Thread thread : shouldRemoved) {
                        threadList.remove(thread);
                    }
                    if (cutBlockPos != null && cutBlockPos.isEmpty()) {
                        print("扫描完成,耗时: " + ((System.currentTimeMillis() - startTime) / 1000) + "秒,扫描了" + blockSize + "个方块");
                        finished = true;
                        cutBlockPos = null;
                        running = false;
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                }
            }
        }
    }

    public void cutBlockPos() {
        float current1X = pos1.getX();
        float current1Z = pos1.getZ();
        float current2X = pos2.getX();
        float current2Z = pos2.getZ();
        if (current1X > current2X) {
            float temp = current1X;
            current1X = current2X;
            current2X = temp;
        }
        if (current1Z > current2Z) {
            float temp = current1Z;
            current1Z = current2Z;
            current2Z = temp;
        }
        float original1Z = current1Z;
        List<AbstractMap.SimpleEntry<BlockPos, BlockPos>> SeparateBlockPos = new ArrayList<AbstractMap.SimpleEntry<BlockPos, BlockPos>>();
        while (current1X <= current2X) {
            while (current1Z <= current2Z) {
                SeparateBlockPos.add(new AbstractMap.SimpleEntry<BlockPos, BlockPos>(new BlockPos(current1X, pos1.getY(), current1Z), new BlockPos(current2X, pos2.getY(), current2Z)));
                current1Z = Math.min(current1Z + step, current2Z + 1);
            }
            current1Z = original1Z;
            current1X = Math.min(current1X + step, current2X + 1);
        }
        print("坐标分割完成,长度: " + SeparateBlockPos.size());
        blockSize = ((long) SeparateBlockPos.size() * step * step  * (Math.max(pos1.getY(), pos2.getY()) - Math.min(pos1.getY(), pos2.getY())));
        cutBlockPos = SeparateBlockPos;
    }

    public void reset() {
        destroy();
        finished = false;
    }

    public void destroy() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
