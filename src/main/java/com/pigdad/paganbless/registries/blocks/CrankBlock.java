package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import com.pigdad.paganbless.utils.WinchUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrankBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(0, 1, 3, 14, 15, 5),
            Block.box(2, 3, 1, 12, 13, 3)
    );
    public static final VoxelShape SHAPE_SOUTH = Shapes.or(
            Block.box(1, 1, 12, 15, 15, 14),
            Block.box(3, 3, 14, 13, 13, 16)
    );
    public static final VoxelShape SHAPE_WEST = Shapes.or(
            Block.box(2, 1, 1, 4, 15, 15),
            Block.box(0, 3, 3, 2, 13, 13)
    );
    public static final VoxelShape SHAPE_EAST = Shapes.or(
            Block.box(12, 1, 1, 14, 15, 15),
            Block.box(14, 3, 3, 16, 13, 13)
    );
    public static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(1, 12, 1, 15, 14, 15),
            Block.box(3, 14, 3, 13, 16, 13)
    );
    public static final VoxelShape SHAPE_DOWN = Shapes.or(
            Block.box(1, 2, 1, 15, 4, 15),
            Block.box(3, 0, 3, 13, 2, 13)
    );

    public CrankBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CrankBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrankBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case NORTH -> SHAPE_NORTH;
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case UP -> SHAPE_UP;
            case DOWN -> SHAPE_DOWN;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockState = super.getStateForPlacement(pContext);
        return blockState != null
                ? blockState.setValue(FACING, pContext.getNearestLookingDirection())
                : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING));
    }


    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(blockPos) instanceof CrankBlockEntity crankBlockEntity) {
            BlockPos winchPos = getWinchPos(blockState, blockPos);
            if (level.getBlockEntity(winchPos) instanceof WinchBlockEntity winchBlockEntity) {
                if (player.isShiftKeyDown()) {
                    dropCrank(crankBlockEntity, winchBlockEntity);
                } else {
                    liftCrank(crankBlockEntity, winchBlockEntity);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void liftCrank(CrankBlockEntity crankBlockEntity, WinchBlockEntity winchBlockEntity) {
        WinchUtils.liftUp(winchBlockEntity);
        crankBlockEntity.turn();
    }

    private void dropCrank(CrankBlockEntity crankBlockEntity, WinchBlockEntity winchBlockEntity) {
        WinchUtils.liftDown(winchBlockEntity);
        crankBlockEntity.drop();
    }

    public static @NotNull BlockPos getWinchPos(BlockState crankState, BlockPos crankPos) {
        Direction direction = crankState.getValue(FACING);
        return crankPos.relative(direction);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, PBBlockEntities.CRANK.get(),
                (level, blockPos, blockState, crankBlockEntity) -> crankBlockEntity.tick());
    }
}
