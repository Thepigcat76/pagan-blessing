package com.pigdad.paganbless.utils;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class Utils {
    // Get capability of a block entity
    public static <T, C> @Nullable T getCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), null);
    }
}
