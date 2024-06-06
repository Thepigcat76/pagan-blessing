package com.pigdad.paganbless.utils.wicanward;

import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface IDistanceLogic {
    boolean isPositionInRange(double posX, double posY, double posZ, BlockPos torchPos, int range);
}
