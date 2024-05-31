package com.pigdad.paganbless.registries.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class WinchBlock extends RotatableBlock {
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 0, 512);

    public WinchBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (pState != pOldState) {
            pLevel.setBlockAndUpdate(pPos, pState.setValue(DISTANCE, recheckConnections(pLevel, pPos, null)));
        }
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(DISTANCE));
    }

    public static void liftUp(Level level, BlockPos winchPos, BlockState winchBlock) {
        // BlockPos of the block being lifted
        int distance = winchBlock.getValue(DISTANCE);
        BlockPos liftedBlockPos = winchPos.below(distance + 1);
        BlockState blockState = level.getBlockState(liftedBlockPos);

        if (blockState.hasBlockEntity() || distance <= 0) return;

        level.removeBlock(liftedBlockPos, true);
        level.setBlock(liftedBlockPos.above(), blockState, 64);
        level.sendBlockUpdated(liftedBlockPos.above(), blockState, Blocks.AIR.defaultBlockState(), 3);
        level.setBlockAndUpdate(winchPos, winchBlock.setValue(DISTANCE, distance-1));
    }

    public static int recheckConnections(Level level, BlockPos winchPos, @Nullable BlockPos excludedPosition) {
        int length = 0;

        BlockPos blockPos = winchPos.below();
        while ((level.getBlockState(blockPos).getBlock() instanceof RopeBlock && RopeBlock.facingUpOrDown(level.getBlockState(blockPos).getValue(RopeBlock.FACING))) || blockPos.equals(excludedPosition)) {
            BlockState ropeBlock = level.getBlockState(blockPos);
            if (!blockPos.equals(excludedPosition)) {
                level.setBlockAndUpdate(blockPos, ropeBlock.setValue(RopeBlock.HAS_WINCH, true));
            }
            blockPos = blockPos.below();
            length++;
        }
        return length;
    }
}
