package com.pigdad.paganbless.registries.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BaseHangingHerbBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(6, 2, 6, 10, 16, 10);
    private static final VoxelShape COLLISION_SHAPE = Block.box(6, 11, 6, 10, 16, 10);

    public BaseHangingHerbBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockState = pContext.getLevel().getBlockState(pContext.getClickedPos().above());
        return !blockState.isEmpty() && blockState.isFaceSturdy(pContext.getLevel(), pContext.getClickedPos().above(), Direction.DOWN, SupportType.CENTER) ? super.getStateForPlacement(pContext) : null;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return COLLISION_SHAPE;
    }
}
