package com.greencat.antimony.common.function;

import com.greencat.antimony.common.function.base.FunctionStatusTrigger;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.core.Pathfinding;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.Utils;
import com.greencat.antimony.utils.Vec3Comparable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class JadeCrystalBot extends FunctionStatusTrigger {
    public JadeCrystalBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    enum SolutionState {
        NOT_STARTED,
        MULTIPLE,
        MULTIPLE_KNOWN,
        FOUND,
        FOUND_KNOWN,
        FAILED,
        INVALID,
    }

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static Vec3Comparable prevPlayerPos;
    private static double prevDistToTreasure;
    private static HashSet<BlockPos> possibleBlocks = new HashSet<>();
    private static final HashMap<Vec3Comparable, Double> evaluatedPlayerPositions = new HashMap<>();
    private static boolean chestRecentlyFound;
    private static long chestLastFoundMillis;
    private static boolean needMove = false;
    private static boolean pressed = false;
    private static long lastPress = 0L;
    private static KeyBinding pressedBinding = null;
    private static final HashSet<BlockPos> openedChestPositions = new HashSet<>();

    // Keeper and Mines of Divan center location info
    private static Vec3i minesCenter;
    private static boolean debugDoNotUseCenter = false;
    private static boolean visitKeeperMessagePrinted;
    private static final String KEEPER_OF_STRING = "Keeper of ";
    private static final String DIAMOND_STRING = "diamond";
    private static final String LAPIS_STRING = "lapis";
    private static final String EMERALD_STRING = "emerald";
    private static final String GOLD_STRING = "gold";
    private static final HashMap<String, Vec3i> keeperOffsets = new HashMap<String, Vec3i>() {{
        put(DIAMOND_STRING, new Vec3i(33, 0, 3));
        put(LAPIS_STRING, new Vec3i(-33, 0, -3));
        put(EMERALD_STRING, new Vec3i(-3, 0, 33));
        put(GOLD_STRING, new Vec3i(3, 0, -33));
    }};

    // Chest offsets from center
    private static final HashSet<Long> knownChestOffsets = new HashSet<>(Arrays.asList(
            -10171958951910L,  // x=-38, y=-22, z=26
            10718829084646L,  // x=38, y=-22, z=-26
            -10721714765806L,  // x=-40, y=-22, z=18
            -10996458455018L,  // x=-41, y=-20, z=22
            -1100920913904L,  // x=-5, y=-21, z=16
            11268584898530L,  // x=40, y=-22, z=-30
            -11271269253148L,  // x=-42, y=-20, z=-28
            -11546281377832L,  // x=-43, y=-22, z=-40
            11818542038999L,  // x=42, y=-19, z=-41
            12093285728240L,  // x=43, y=-21, z=-16
            -1409286164L,      // x=-1, y=-22, z=-20
            1922736062492L,    // x=6, y=-21, z=28
            2197613969419L,    // x=7, y=-21, z=11
            2197613969430L,    // x=7, y=-21, z=22
            -3024999153708L,  // x=-12, y=-21, z=-44
            3571936395295L,    // x=12, y=-22, z=31
            3572003504106L,    // x=12, y=-22, z=-22
            3572003504135L,    // x=12, y=-21, z=7
            3572070612949L,    // x=12, y=-21, z=-43
            -3574822076373L,  // x=-14, y=-21, z=43
            -3574822076394L,  // x=-14, y=-21, z=22
            -4399455797228L,  // x=-17, y=-21, z=20
            -5224156626944L,  // x=-20, y=-22, z=0
            548346527764L,    // x=1, y=-21, z=20
            5496081743901L,    // x=19, y=-22, z=29
            5770959650816L,    // x=20, y=-22, z=0
            5771093868518L,    // x=20, y=-21, z=-26
            -6048790347736L,  // x=-23, y=-22, z=40
            6320849682418L,    // x=22, y=-21, z=-14
            -6323668254708L,  // x=-24, y=-22, z=12
            6595593371674L,    // x=23, y=-22, z=26
            6595660480473L,    // x=23, y=-22, z=-39
            6870471278619L,    // x=24, y=-22, z=27
            7145349185553L,    // x=25, y=-22, z=17
            8244995030996L,    // x=29, y=-21, z=-44
            -8247679385612L,  // x=-31, y=-21, z=-12
            -8247679385640L,  // x=-31, y=-21, z=-40
            8519872937959L,    // x=30, y=-21, z=-25
            -8522557292584L,  // x=-32, y=-21, z=-40
            -9622068920278L,  // x=-36, y=-20, z=42
            -9896946827278L,  // x=-37, y=-21, z=-14
            -9896946827286L    // x=-37, y=-21, z=-22
    ));

    static Predicate<BlockPos> treasureAllowedPredicate = JadeCrystalBot::treasureAllowed;
    static SolutionState currentState = SolutionState.NOT_STARTED;
    static SolutionState previousState = SolutionState.NOT_STARTED;
    static final KeyBinding[] keys = new KeyBinding[]{Minecraft.getMinecraft().gameSettings.keyBindForward,Minecraft.getMinecraft().gameSettings.keyBindLeft,Minecraft.getMinecraft().gameSettings.keyBindBack,Minecraft.getMinecraft().gameSettings.keyBindRight};
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if(FunctionManager.getStatus("JadeCrystalBot")){
                if (possibleBlocks.size() == 1) {
                    if(!FunctionManager.getStatus("Pathfinding")) {
                        BlockPos block = possibleBlocks.iterator().next();
                        Pathfinder.setup(new BlockPos(Utils.floorVec(mc.thePlayer.getPositionVector())), block, 0.0D);
                        if (Pathfinder.hasPath()) {
                            FunctionManager.setStatus("Pathfinding",true);
                        }
                    }
                }
                if(needMove && !pressed){
                    KeyBinding key = keys[new Random().nextInt(4)];
                    KeyBinding.setKeyBindState(key.getKeyCode(),true);
                    pressedBinding = key;
                    lastPress = System.currentTimeMillis();
                    pressed = true;
                    needMove = false;
                }
                if(pressed && pressedBinding != null && System.currentTimeMillis() - lastPress > 500){
                    KeyBinding.setKeyBindState(pressedBinding.getKeyCode(),false);
                    pressedBinding = null;
                    pressed = false;
                }
                Vec3i boundingBox = new Vec3i(6,6,6);
                if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                    for (BlockPos pos : BlockPos.getAllInBox(Minecraft.getMinecraft().thePlayer.getPosition().add(boundingBox), Minecraft.getMinecraft().thePlayer.getPosition().subtract(boundingBox))) {
                        if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.chest) {
                            Minecraft.getMinecraft().playerController.clickBlock(pos, EnumFacing.DOWN);
                        }
                    }
                }
        }
    }

    public interface Predicate<BlockPos> {
        boolean check(BlockPos blockPos);
    }
    @SubscribeEvent
    public void process(ClientChatReceivedEvent event) {
        if (event.type != 2) {
            return;
        }
        IChatComponent message = event.message;
        if (
                !FunctionManager.getStatus("JadeCrystalBot") ||
                !message.getUnformattedText().contains("TREASURE: ")) {
            return;
        }
        boolean centerNewlyDiscovered = locateMinesCenterIfNeeded();

        double distToTreasure = Double.parseDouble(message
                .getUnformattedText()
                .split("TREASURE: ")[1].split("m")[0].replaceAll("(?!\\.)\\D", ""));

        // Delay to keep old chest location from being treated as the new chest location
        if (chestRecentlyFound) {
            long currentTimeMillis = System.currentTimeMillis();
            if (chestLastFoundMillis == 0) {
                chestLastFoundMillis = currentTimeMillis;
                return;
            } else if (currentTimeMillis - chestLastFoundMillis < 1000 && distToTreasure < 5.0) {
                return;
            }

            chestLastFoundMillis = 0;
            chestRecentlyFound = false;
        }

        SolutionState originalState = currentState;
        int originalCount = possibleBlocks.size();
        Vec3Comparable adjustedPlayerPos = getPlayerPosAdjustedForEyeHeight();
        findPossibleSolutions(distToTreasure, adjustedPlayerPos, centerNewlyDiscovered);
        if (currentState != originalState || originalCount != possibleBlocks.size()) {
            switch (currentState) {
                case FOUND_KNOWN:
                case FOUND:
                    Utils.print("[JadeCrystalBot] 找到目标点");
                    break;
                case INVALID:
                    Utils.print("[JadeCrystalBot] 无法找到目标点");
                    needMove = true;
                    resetSolution(false);
                    break;
                case FAILED:
                    Utils.print("[JadeCrystalBot] 寻找目标点失败");
                    needMove = true;
                    resetSolution(false);
                    break;
                case MULTIPLE_KNOWN:
                    // falls through
                case MULTIPLE:
                    Utils.print("[JadeCrystalBot] 需要移动到另一个位置寻找目标点,可能的方块: " + possibleBlocks.size());
                    needMove = true;
                    break;
                default:
                    throw new IllegalStateException("Metal detector is in invalid state");
            }
        }
    }

    static void findPossibleSolutions(double distToTreasure, Vec3Comparable playerPos, boolean centerNewlyDiscovered) {
        if (prevDistToTreasure == distToTreasure && prevPlayerPos.equals(playerPos) &&
                !evaluatedPlayerPositions.containsKey(playerPos)) {
            evaluatedPlayerPositions.put(playerPos, distToTreasure);
            if (possibleBlocks.size() == 0) {
                for (int zOffset = (int) Math.floor(-distToTreasure); zOffset <= Math.ceil(distToTreasure); zOffset++) {
                    for (int y = 65; y <= 75; y++) {
                        double calculatedDist = 0;
                        int xOffset = 0;
                        while (calculatedDist < distToTreasure) {
                            BlockPos pos = new BlockPos(Math.floor(playerPos.xCoord) + xOffset,
                                    y, Math.floor(playerPos.zCoord) + zOffset
                            );
                            calculatedDist = playerPos.distanceTo(new Vec3Comparable(pos).addVector(0D, 1D, 0D));
                            if (round(calculatedDist, 1) == distToTreasure && treasureAllowedPredicate.check(pos)) {
                                possibleBlocks.add(pos);
                            }
                            xOffset++;
                        }
                        xOffset = 0;
                        calculatedDist = 0;
                        while (calculatedDist < distToTreasure) {
                            BlockPos pos = new BlockPos(Math.floor(playerPos.xCoord) - xOffset,
                                    y, Math.floor(playerPos.zCoord) + zOffset
                            );
                            calculatedDist = playerPos.distanceTo(new Vec3Comparable(pos).addVector(0D, 1D, 0D));
                            if (round(calculatedDist, 1) == distToTreasure && treasureAllowedPredicate.check(pos)) {
                                possibleBlocks.add(pos);
                            }
                            xOffset++;
                        }
                    }
                }

                updateSolutionState();
            } else if (possibleBlocks.size() != 1) {
                HashSet<BlockPos> temp = new HashSet<>();
                for (BlockPos pos : possibleBlocks) {
                    if (round(playerPos.distanceTo(new Vec3Comparable(pos).addVector(0D, 1D, 0D)), 1) == distToTreasure) {
                        temp.add(pos);
                    }
                }

                possibleBlocks = temp;
                updateSolutionState();
            } else {
                BlockPos pos = possibleBlocks.iterator().next();
                if (Math.abs(distToTreasure - (playerPos.distanceTo(new Vec3Comparable(pos)))) > 5) {
                    currentState = SolutionState.INVALID;
                }
            }
        } else if (centerNewlyDiscovered && possibleBlocks.size() > 1) {
            updateSolutionState();
        }

        prevPlayerPos = playerPos;
        prevDistToTreasure = distToTreasure;
    }

    public static void setDebugDoNotUseCenter(boolean val) {
        debugDoNotUseCenter = val;
    }

    private static String getFriendlyBlockPositions(Collection<BlockPos> positions) {
        if (positions.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (BlockPos blockPos : positions) {
            sb.append("Absolute: ");
            sb.append(blockPos.toString());
            if (minesCenter != Vec3i.NULL_VECTOR) {
                BlockPos relativeOffset = blockPos.subtract(minesCenter);
                sb.append(", Relative: ");
                sb.append(relativeOffset.toString());
                sb.append(" (" + relativeOffset.toLong() + ")");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private static String getFriendlyEvaluatedPositions() {
        if (evaluatedPlayerPositions.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Vec3Comparable vec : evaluatedPlayerPositions.keySet()) {
            sb.append("Absolute: " + vec.toString());
            if (minesCenter != Vec3i.NULL_VECTOR) {
                BlockPos positionBlockPos = new BlockPos(vec);
                BlockPos relativeOffset = positionBlockPos.subtract(minesCenter);
                sb.append(", Relative: " + relativeOffset.toString() + " (" + relativeOffset.toLong() + ")");
            }

            sb.append(" Distance: ");
            sb.append(evaluatedPlayerPositions.get(vec));

            sb.append("\n");
        }

        return sb.toString();
    }
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event){
        if(event.message.getUnformattedText().contains("You found ")){
            resetSolution(true);
        }
    }
    public static void resetSolution(Boolean chestFound) {
        if (chestFound) {
            prevPlayerPos = null;
            prevDistToTreasure = 0;
            if (possibleBlocks.size() == 1) {
                openedChestPositions.add(possibleBlocks.iterator().next().getImmutable());
            }
        }

        chestRecentlyFound = chestFound;
        possibleBlocks.clear();
        evaluatedPlayerPositions.clear();
        previousState = currentState;
        currentState = SolutionState.NOT_STARTED;
    }

    @Override
    public String getName() {
        return "JadeCrystalBot";
    }

    @Override
    public void post() {

    }

    @Override
    public void init() {
        minesCenter = Vec3i.NULL_VECTOR;
        visitKeeperMessagePrinted = false;
        openedChestPositions.clear();
        chestLastFoundMillis = 0;
        prevDistToTreasure = 0;
        prevPlayerPos = null;
        currentState = SolutionState.NOT_STARTED;
        resetSolution(false);
        pressedBinding = null;
        pressed = false;
        needMove = false;
        possibleBlocks.clear();
        FunctionManager.setStatus("Pathfinding", false);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event){
        if(FunctionManager.getStatus("JadeCrystalBot")){
            minesCenter = Vec3i.NULL_VECTOR;
            visitKeeperMessagePrinted = false;
            openedChestPositions.clear();
            chestLastFoundMillis = 0;
            prevDistToTreasure = 0;
            prevPlayerPos = null;
            currentState = SolutionState.NOT_STARTED;
            resetSolution(false);
            pressedBinding = null;
            pressed = false;
            needMove = false;
            possibleBlocks.clear();
            FunctionManager.setStatus("Pathfinding", false);
        }
    }
    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
            if (possibleBlocks.size() == 1) {
                BlockPos block = possibleBlocks.iterator().next();
                Utils.OutlinedBoxWithESP(block.add(0, 1, 0), Color.CYAN,false,2.5F);
                Utils.renderTrace(Minecraft.getMinecraft().thePlayer.getPositionVector(),new Vec3(block).addVector(0.5D,1D,0.5D),Color.CYAN,2.5F);
            } else if (possibleBlocks.size() > 1) {
                for (BlockPos block : possibleBlocks) {
                    Utils.OutlinedBoxWithESP(block.add(0, 1, 0), Color.CYAN,false,2.5F);
                }
            }
    }

    private static boolean locateMinesCenterIfNeeded() {
        if (minesCenter != Vec3i.NULL_VECTOR) {
            return false;
        }

        List<EntityArmorStand> keeperEntities = mc.theWorld.getEntities(EntityArmorStand.class, (entity) -> {
            if (!entity.hasCustomName()) return false;
            return entity.getCustomNameTag().contains(KEEPER_OF_STRING);
        });

        if (keeperEntities.size() == 0) {
            if (!visitKeeperMessagePrinted) {
                Utils.print("在手持Metal Detector的同时接近Keeper，以加快搜寻速度");
                visitKeeperMessagePrinted = true;
            }
            return false;
        }

        EntityArmorStand keeperEntity = keeperEntities.get(0);
        String keeperName = keeperEntity.getCustomNameTag();
        String keeperType = keeperName.substring(keeperName.indexOf(KEEPER_OF_STRING) + KEEPER_OF_STRING.length());
        minesCenter = keeperEntity.getPosition().add(keeperOffsets.get(keeperType.toLowerCase()));
        Utils.print("检测到Keeper存在,快速搜寻开启");
        return true;
    }

    public static void setMinesCenter(BlockPos center) {
        minesCenter = center;
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private static void updateSolutionState() {
        previousState = currentState;

        if (possibleBlocks.size() == 0) {
            currentState = SolutionState.FAILED;
            return;
        }

        if (possibleBlocks.size() == 1) {
            currentState = SolutionState.FOUND;
            return;
        }

        // Narrow solutions using known locations if the mines center is known
        if (minesCenter.equals(BlockPos.NULL_VECTOR) || debugDoNotUseCenter) {
            currentState = SolutionState.MULTIPLE;
            return;
        }

        HashSet<BlockPos> temp =
                possibleBlocks.stream()
                        .filter(block -> knownChestOffsets.contains(block.subtract(minesCenter).toLong()))
                        .collect(Collectors.toCollection(HashSet::new));
        if (temp.size() == 0) {
            currentState = SolutionState.MULTIPLE;
            return;
        }

        if (temp.size() == 1) {
            possibleBlocks = temp;
            currentState = SolutionState.FOUND_KNOWN;
            return;

        }

        currentState = SolutionState.MULTIPLE_KNOWN;
    }

    public static BlockPos getSolution() {
        if (JadeCrystalBot.possibleBlocks.size() != 1) {
            return BlockPos.ORIGIN;
        }

        return JadeCrystalBot.possibleBlocks.stream().iterator().next();
    }

    private static Vec3Comparable getPlayerPosAdjustedForEyeHeight() {
        return new Vec3Comparable(
                mc.thePlayer.posX,
                mc.thePlayer.posY + (mc.thePlayer.getEyeHeight() - mc.thePlayer.getDefaultEyeHeight()),
                mc.thePlayer.posZ
        );
    }

    static boolean isKnownOffset(BlockPos pos) {
        return knownChestOffsets.contains(pos.subtract(minesCenter).toLong());
    }

    static boolean isAllowedBlockType(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock().getRegistryName().equals("minecraft:gold_block") ||
                mc.theWorld.getBlockState(pos).getBlock().getRegistryName().equals("minecraft:prismarine") ||
                mc.theWorld.getBlockState(pos).getBlock().getRegistryName().equals("minecraft:chest") ||
                mc.theWorld.getBlockState(pos).getBlock().getRegistryName().equals("minecraft:stained_glass") ||
                mc.theWorld.getBlockState(pos).getBlock().getRegistryName().equals("minecraft:stained_glass_pane") ||
                mc.theWorld.getBlockState(pos).getBlock().getRegistryName().equals("minecraft:wool") ||
                mc.theWorld.getBlockState(pos).getBlock().getRegistryName().equals("minecraft:stained_hardened_clay");
    }

    static boolean isAirAbove(BlockPos pos) {
        return mc.theWorld.
                getBlockState(pos.add(0, 1, 0)).getBlock().getRegistryName().equals("minecraft:air");
    }

    private static boolean treasureAllowed(BlockPos pos) {
        boolean airAbove = isAirAbove(pos);
        boolean allowedBlockType = isAllowedBlockType(pos);
        return isKnownOffset(pos) || (airAbove && allowedBlockType);
    }

    /*static private String getDiagnosticMessage() {
        StringBuilder diagsMessage = new StringBuilder();

        diagsMessage.append(EnumChatFormatting.AQUA);
        diagsMessage.append("Mines Center: ");
        diagsMessage.append(EnumChatFormatting.WHITE);
        diagsMessage.append((minesCenter.equals(Vec3i.NULL_VECTOR)) ? "<NOT DISCOVERED>" : minesCenter.toString());
        diagsMessage.append("\n");

        diagsMessage.append(EnumChatFormatting.AQUA);
        diagsMessage.append("Current Solution State: ");
        diagsMessage.append(EnumChatFormatting.WHITE);
        diagsMessage.append(currentState.name());
        diagsMessage.append("\n");

        diagsMessage.append(EnumChatFormatting.AQUA);
        diagsMessage.append("Previous Solution State: ");
        diagsMessage.append(EnumChatFormatting.WHITE);
        diagsMessage.append(previousState.name());
        diagsMessage.append("\n");

        diagsMessage.append(EnumChatFormatting.AQUA);
        diagsMessage.append("Previous Player Position: ");
        diagsMessage.append(EnumChatFormatting.WHITE);
        diagsMessage.append((prevPlayerPos == null) ? "<NONE>" : prevPlayerPos.toString());
        diagsMessage.append("\n");

        diagsMessage.append(EnumChatFormatting.AQUA);
        diagsMessage.append("Previous Distance To Treasure: ");
        diagsMessage.append(EnumChatFormatting.WHITE);
        diagsMessage.append((prevDistToTreasure == 0) ? "<NONE>" : prevDistToTreasure);
        diagsMessage.append("\n");

        diagsMessage.append(EnumChatFormatting.AQUA);
        diagsMessage.append("Current Possible Blocks: ");
        diagsMessage.append(EnumChatFormatting.WHITE);
        diagsMessage.append(possibleBlocks.size());
        diagsMessage.append(getFriendlyBlockPositions(possibleBlocks));
        diagsMessage.append("\n");

        diagsMessage.append(EnumChatFormatting.AQUA);
        diagsMessage.append("Evaluated player positions: ");
        diagsMessage.append(EnumChatFormatting.WHITE);
        diagsMessage.append(evaluatedPlayerPositions.size());
        diagsMessage.append(getFriendlyEvaluatedPositions());
        diagsMessage.append("\n");

        diagsMessage.append(EnumChatFormatting.AQUA);
        diagsMessage.append("Chest locations not on known list:\n");
        diagsMessage.append(EnumChatFormatting.WHITE);
        if (minesCenter != Vec3i.NULL_VECTOR) {
            HashSet<BlockPos> locationsNotOnKnownList = openedChestPositions
                    .stream()
                    .filter(block -> !knownChestOffsets.contains(block.subtract(minesCenter).toLong()))
                    .map(block -> block.subtract(minesCenter))
                    .collect(Collectors.toCollection(HashSet::new));
            if (locationsNotOnKnownList.size() > 0) {
                for (BlockPos blockPos : locationsNotOnKnownList) {
                    diagsMessage.append(String.format(
                            "%dL,\t\t// x=%d, y=%d, z=%d",
                            blockPos.toLong(),
                            blockPos.getX(),
                            blockPos.getY(),
                            blockPos.getZ()
                    ));
                }
            }
        } else {
            diagsMessage.append("<REQUIRES MINES CENTER>");
        }

        return diagsMessage.toString();
    }*/
}
