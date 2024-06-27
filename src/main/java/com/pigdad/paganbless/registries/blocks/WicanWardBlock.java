package com.pigdad.paganbless.registries.blocks;

import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.data.saved_data.WicanWardSavedData;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WicanWardBlock extends Block {
    public static final int RANGE = PBConfig.wwRange;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape NORTH_SOUTH_SHAPE = Block.box(0, 11, 7, 16, 16, 9);
    public static final VoxelShape EAST_WEST_SHAPE = Block.box(7, 11, 0, 9, 16, 16);

    public WicanWardBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getPlayer().getDirection();
        BlockPos blockPos = ctx.getClickedPos().relative(direction);
        BlockState blockState = ctx.getLevel().getBlockState(blockPos);
        if (blockState.is(this)) {
            return this.defaultBlockState().setValue(FACING, blockState.getValue(FACING));
        }
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch (state.getValue(FACING)) {
            case EAST, WEST -> EAST_WEST_SHAPE;
            default -> NORTH_SOUTH_SHAPE;
        };
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if (pLevel instanceof ServerLevel serverLevel) {
            WicanWardSavedData savedData = Utils.getWWData(serverLevel);
            savedData.addBlockPos(pPos);
        }
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        if (pLevel instanceof ServerLevel serverLevel) {
            WicanWardSavedData savedData = Utils.getWWData(serverLevel);
            savedData.removeBlockPos(pPos);
        }
    }
}
