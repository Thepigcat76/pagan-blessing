package com.pigdad.paganbless.utils;

import net.minecraft.core.BlockPos;

public final class DistanceUtils {
    public static final IDistanceLogic CUBIC = (posX, posY, posZ, searchForPos, range) -> {
        double minX = searchForPos.getX() - range;
        double minY = searchForPos.getY() - range;
        double minZ = searchForPos.getZ() - range;
        double maxX = searchForPos.getX() + range + 1;
        double maxY = searchForPos.getY() + range + 1;
        double maxZ = searchForPos.getZ() + range + 1;
        return minX <= posX && maxX >= posX &&
                minY <= posY && maxY >= posY &&
                minZ <= posZ && maxZ >= posZ;
    };

    public static final IDistanceLogic CYLINDER = (posX, posY, posZ, searchForPos, range) -> {
        double dx = searchForPos.getX() + 0.5 - posX;
        double dy = Math.abs(searchForPos.getY() + 0.5 - posY);
        double dz = searchForPos.getZ() + 0.5 - posZ;
        return (dx * dx + dz * dz) <= range && dy <= range;
    };

    @FunctionalInterface
    public interface IDistanceLogic {
        boolean isPositionInRange(double posX, double posY, double posZ, BlockPos torchPos, int range);
    }
}
