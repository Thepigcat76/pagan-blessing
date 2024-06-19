package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.api.blocks.RotatableEntityBlock;
import com.pigdad.paganbless.networking.CrankAnglePayload;
import com.pigdad.paganbless.networking.CrankRotatePayload;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrankBlock extends BaseEntityBlock {
    public static final int CRANK_MIN_ROTATION = 1;
    public static final int CRANK_MAX_ROTATION = 3;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", CRANK_MIN_ROTATION, CRANK_MAX_ROTATION);
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
        return blockState != null ? blockState.setValue(ROTATION, CRANK_MIN_ROTATION).setValue(FACING, pContext.getNearestLookingDirection()) : null;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ROTATION, FACING));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!player.isShiftKeyDown()) {
            level.setBlockAndUpdate(blockPos, incrRotationState(blockState));
            BlockPos winchPos = getWinchPos(blockState, blockPos);
            BlockState winchBlock = level.getBlockState(winchPos);
            if (winchBlock.getBlock() instanceof WinchBlock) {
                int distance = winchBlock.getValue(WinchBlock.DISTANCE);
                if (winchBlock.getBlock() instanceof WinchBlock && distance > 1) {
                    CrankBlockEntity blockEntity = (CrankBlockEntity) level.getBlockEntity(blockPos);
                    blockEntity.turn();
                    WinchBlock.liftUp(level, winchPos, winchBlock);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.FAIL;
    }

    public static @NotNull BlockPos getWinchPos(BlockState blockState, BlockPos blockPos) {
        Direction direction = blockState.getValue(FACING);
        return blockPos.relative(direction);
    }

    public static BlockState incrRotationState(BlockState blockState) {
        int oldRotation = blockState.getValue(ROTATION);
        return oldRotation == CRANK_MAX_ROTATION
                ? blockState.setValue(ROTATION, CRANK_MIN_ROTATION)
                : blockState.setValue(ROTATION, oldRotation + 1);
    }

    public static BlockState decrRotationState(BlockState blockState) {
        int oldRotation = blockState.getValue(ROTATION);
        return oldRotation == CRANK_MIN_ROTATION
                ? blockState.setValue(ROTATION, CRANK_MAX_ROTATION)
                : blockState.setValue(ROTATION, oldRotation - 1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, PBBlockEntities.CRANK.get(),
                (level, blockPos, blockState, crankBlockEntity) -> crankBlockEntity.tick());
    }
}
