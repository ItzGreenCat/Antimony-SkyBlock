package com.greencat.core;



import java.util.List;

import com.greencat.utils.Chroma;
import com.greencat.utils.Utils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Pathfinder {
    private static CPathfinder pathfinder;
    public static List<Vec3> path;
    public static BlockPos fromPos;
    public static BlockPos toPos;

    public static void setup(BlockPos from, BlockPos to, double minDistance) {
        fromPos = from;
        toPos = to;
        pathfinder = new CPathfinder(new Vec3(from), new Vec3(to), minDistance);
        pathfinder.compute();
        path = pathfinder.getPath();
    }

    public static Vec3 getCurrent() {
        return path != null && !path.isEmpty() ? (Vec3)path.get(0) : null;
    }

    public static boolean hasNext() {
        return path != null && path.size() > 1;
    }

    public static Vec3 getNext() {
        return (Vec3)path.get(1);
    }

    public static boolean goNext() {
            if (path != null && path.size() > 1) {
                path.remove(0);
                return true;
            } else {
                path = null;
                return false;
            }
    }

    public static boolean hasPath() {
        return path != null && !path.isEmpty();
    }

    public static Vec3 getGoal() {
        return (Vec3)path.get(path.size() - 1);
    }
}
