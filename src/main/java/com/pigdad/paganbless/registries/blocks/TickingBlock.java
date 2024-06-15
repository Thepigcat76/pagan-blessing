package com.pigdad.paganbless.registries.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface TickingBlock<B extends BlockEntity> extends EntityBlock {
    void clientTick(Level level, BlockPos blockPos, BlockState blockState, B blockEntity);

    void serverTick(Level level, BlockPos blockPos, BlockState blockState, B blockEntity);

    BlockEntityType<B> getBlockEntityType();

    @SuppressWarnings("unchecked")
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> serverBlockEntityType) {
        if (serverBlockEntityType == getBlockEntityType()) {
            return (level, blockPos, blockState, blockEntity) -> {
                if (pLevel.isClientSide()) {
                    clientTick(level, blockPos, blockState, (B) blockEntity);
                } else {
                    serverTick(level, blockPos, blockState, (B) blockEntity);
                }
            };
        }
        return null;
    }
}
