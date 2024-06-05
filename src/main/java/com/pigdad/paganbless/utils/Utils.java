package com.pigdad.paganbless.utils;

import com.pigdad.paganbless.data.WicanWardSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public final class Utils {
    // Get capability of a block entity
    public static <T, C> @Nullable T getCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), null);
    }

    public static WicanWardSavedData getWWData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(WicanWardSavedData.factory(level), WicanWardSavedData.DATA_ID);
    }
}
