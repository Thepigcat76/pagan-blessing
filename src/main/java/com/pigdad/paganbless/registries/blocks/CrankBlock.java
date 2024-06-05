package com.pigdad.paganbless.registries.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CrankBlock extends RotatableBlock {
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 7);

    public CrankBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockState = super.getStateForPlacement(pContext);
        return blockState != null ? blockState.setValue(ROTATION, 0) : null;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ROTATION));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide() && !player.isShiftKeyDown()) {
            level.setBlockAndUpdate(blockPos, incrRotationState(blockState));
            BlockPos winchPos = getWinchPos(blockState, blockPos);
            BlockState winchBlock = level.getBlockState(winchPos);
            int distance = winchBlock.getValue(WinchBlock.DISTANCE);
            if (winchBlock.getBlock() instanceof WinchBlock && distance > 1) {
                WinchBlock.liftUp(level, winchPos, winchBlock);
            } else {
                return InteractionResult.FAIL;
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public static @NotNull BlockPos getWinchPos(BlockState blockState, BlockPos blockPos) {
        Direction direction = blockState.getValue(FACING);
        return blockPos.relative(direction);
    }

    public static BlockState incrRotationState(BlockState blockState) {
        int oldRotation = blockState.getValue(ROTATION);
        return oldRotation == 7
                ? blockState.setValue(ROTATION, 0)
                : blockState.setValue(ROTATION, oldRotation + 1);
    }

    public static BlockState decrRotationState(BlockState blockState) {
        int oldRotation = blockState.getValue(ROTATION);
        return oldRotation == 0
                ? blockState.setValue(ROTATION, 7)
                : blockState.setValue(ROTATION, oldRotation - 1);
    }
}