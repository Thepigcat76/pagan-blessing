package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.api.blocks.RotatableEntityBlock;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import com.pigdad.paganbless.utils.AnvilUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WinchBlock extends RotatableEntityBlock {
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 0, 512);
    public static final BooleanProperty LIFT_DOWN = BooleanProperty.create("lift_down");

    public WinchBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(WinchBlock::new);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state != null ? state.setValue(LIFT_DOWN, false).setValue(RotatableEntityBlock.FACING, context.getPlayer().getDirection().getOpposite().getClockWise()) : null;
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
        super.createBlockStateDefinition(p_49915_.add(DISTANCE, LIFT_DOWN));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WinchBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, PBBlockEntities.WINCH.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick());
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
        level.setBlockAndUpdate(winchPos, winchBlock.setValue(DISTANCE, distance - 1));
    }

    // Will return true as long as the winch can lift down
    public static boolean liftDown(Level level, BlockPos winchPos, BlockState winchBlock) {
        // BlockPos of the block being lifted
        int distance = winchBlock.getValue(DISTANCE);
        BlockPos liftedBlockPos = winchPos.below(distance + 1);
        BlockState blockState = level.getBlockState(liftedBlockPos);

        if (blockState.hasBlockEntity() || distance <= 0) return false;

        if (!level.getBlockState(liftedBlockPos.below()).canBeReplaced()) {
            AnvilUtils.onAnvilLand(level, liftedBlockPos);
            return false;
        }

        level.setBlockAndUpdate(winchPos, winchBlock.setValue(DISTANCE, distance + 1));
        level.setBlockAndUpdate(liftedBlockPos, PBBlocks.ROPE.get().defaultBlockState().setValue(RopeBlock.FACING, Direction.DOWN));
        level.setBlockAndUpdate(liftedBlockPos.below(), blockState);
        return true;
    }

    // Will return length of the connection
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
