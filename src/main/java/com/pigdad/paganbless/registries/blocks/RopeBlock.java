package com.pigdad.paganbless.registries.blocks;

import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import com.pigdad.paganbless.utils.WinchUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WaterloggedTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RopeBlock extends WaterloggedTransparentBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HAS_WINCH = BooleanProperty.create("has_winch");

    public RopeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState p_60475_, PathComputationType p_60478_) {
        return true;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(6, 6, 6, 10, 10, 10);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction p_60542_, BlockState p_60543_, LevelAccessor level, BlockPos currentPos, BlockPos p_60546_) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state;
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState p_313789_) {
        return p_313789_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(true) : super.getFluidState(p_313789_);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos clickedPos = ctx.getClickedPos();
        FluidState fluidstate = level.getFluidState(clickedPos);
        BlockPos aboveRopePos = clickedPos.above();
        BlockState aboveBlock = level.getBlockState(aboveRopePos);
        Direction facing = ctx.getNearestLookingDirection();
        // Checks if the above block has a rope that has a winch or has a winch directly above it.
        // Also requires that the rope is facing up or down
        boolean hasWinch = ((aboveBlock.getBlock() instanceof RopeBlock && aboveBlock.getValue(HAS_WINCH))
                || aboveBlock.getBlock() instanceof WinchBlock)
                && facingUpOrDown(facing);
        if (hasWinch) {
            BlockPos winchPos = level.getBlockState(aboveRopePos).getBlock() instanceof WinchBlock ? aboveRopePos : getWinchPos(level, aboveRopePos);
            if (winchPos != null) {
                WinchBlockEntity blockEntity = (WinchBlockEntity) level.getBlockEntity(winchPos);
                int newDistance = WinchUtils.recheckConnections(level, winchPos, clickedPos);
                blockEntity.setDistance(newDistance);
            }
        }
        return super.getStateForPlacement(ctx)
                .setValue(WATERLOGGED, fluidstate.is(Fluids.WATER))
                .setValue(FACING, facing)
                .setValue(HAS_WINCH, hasWinch);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            BlockState aboveBlock = pLevel.getBlockState(pPos.above());
            if (pState.getValue(HAS_WINCH) || aboveBlock.getBlock() instanceof WinchBlock) {
                invalidateDownwards(pLevel, pPos);
                BlockPos winchPos = aboveBlock.getBlock() instanceof WinchBlock ? pPos.above() : getWinchPos(pLevel, pPos.above());
                WinchUtils.recheckConnections(pLevel, winchPos, null);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, WATERLOGGED, HAS_WINCH);
    }

    // Go through all ropes above the block to get the next winch
    // NOTE: If the winch is directly above `ropePos`, it will not be detected
    public static @Nullable BlockPos getWinchPos(Level level, BlockPos ropePos) {
        BlockState blockState = level.getBlockState(ropePos);
        if (blockState.getBlock() instanceof RopeBlock && !blockState.getValue(HAS_WINCH)) return null;

        BlockPos curBlockPos = ropePos.above();
        while (level.getBlockState(curBlockPos).getBlock() instanceof RopeBlock) {
            curBlockPos = curBlockPos.above();
        }
        if (level.getBlockState(curBlockPos).getBlock() instanceof WinchBlock) {
            return curBlockPos;
        }
        return null;
    }

    // Go through all blocks below `blockPoses` and set HAS_WINCH to false
    public static void invalidateDownwards(Level level, BlockPos blockPos) {
        BlockPos curBlockPos = blockPos.below();
        while (level.getBlockState(curBlockPos).getBlock() instanceof RopeBlock && facingUpOrDown(level.getBlockState(curBlockPos).getValue(FACING))) {
            BlockState ropeBlock = level.getBlockState(curBlockPos);
            level.setBlockAndUpdate(curBlockPos, ropeBlock.setValue(RopeBlock.HAS_WINCH, false));
            curBlockPos = curBlockPos.below();
        }
    }

    public static boolean facingUpOrDown(Direction direction) {
        return direction == Direction.DOWN || direction == Direction.UP;
    }
}
