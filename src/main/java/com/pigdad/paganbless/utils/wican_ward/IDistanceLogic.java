package com.pigdad.paganbless.utils.wican_ward;

import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface IDistanceLogic {
    boolean isPositionInRange(double posX, double posY, double posZ, BlockPos torchPos, int range);
}
