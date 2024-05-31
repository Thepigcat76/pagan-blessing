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

    public CrankBlock(Properties p_49224_) {
        super(p_49224_);
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
        level.setBlockAndUpdate(blockPos, incrRotationState(blockState));
        Direction direction = blockState.getValue(FACING);
        BlockPos winchPos = blockPos.relative(direction);
        BlockState winchBlock = level.getBlockState(winchPos);
        if (winchBlock.getBlock() instanceof WinchBlock) {
            WinchBlock.liftUp(level, winchPos, winchBlock);
        }
        return InteractionResult.SUCCESS;
    }

    protected BlockState incrRotationState(BlockState blockState) {
        int oldRotation = blockState.getValue(ROTATION);
        return oldRotation == 7
                ? blockState.setValue(ROTATION, 0)
                : blockState.setValue(ROTATION, oldRotation + 1);
    }
}