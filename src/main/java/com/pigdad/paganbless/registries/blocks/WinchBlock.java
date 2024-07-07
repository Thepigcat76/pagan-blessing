package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.api.blocks.RotatableEntityBlock;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import com.pigdad.paganbless.utils.WinchUtils;
import com.pigdad.paganbless.utils.recipes.AnvilRecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class WinchBlock extends RotatableEntityBlock {
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(3, 0, 0.5, 13, 15, 15.5),
            Block.box(2, 15, 3.5, 14, 16, 12.5),
            Block.box(13, 4, 3.5, 14, 15, 12.5),
            Block.box(14, 5.5, 5.5, 16, 10.5, 10.5),
            Block.box(0, 5.5, 5.5, 2, 10.5, 10.5),
            Block.box(2, 4, 3.5, 3, 15, 12.5)
    ).reduce(Shapes::or).get();

    public static final VoxelShape ROTATED_SHAPE = Stream.of(
            Block.box(0.5, 0, 3, 15.5, 15, 13),
            Block.box(3.5, 15, 2, 12.5, 16, 14),
            Block.box(3.5, 4, 13, 12.5, 15, 14),
            Block.box(5.5, 5.5, 13, 10.5, 10.5, 15),
            Block.box(5.5, 5.5, 1, 10.5, 10.5, 3),
            Block.box(3.5, 4, 2, 12.5, 15, 3)
    ).reduce(Shapes::or).get();

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
        return state != null ? state.setValue(RotatableEntityBlock.FACING, context.getPlayer().getDirection().getOpposite().getClockWise()) : null;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case NORTH, SOUTH -> SHAPE;
            case EAST, WEST -> ROTATED_SHAPE;
            default -> null;
        };
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        BlockEntity fireBoxBlockEntity = pLevel.getBlockEntity(pPos);
        pPlayer.openMenu((WinchBlockEntity) fireBoxBlockEntity, pPos);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (pState != pOldState) {
            WinchBlockEntity blockEntity = (WinchBlockEntity) pLevel.getBlockEntity(pPos);
            int newDistance = WinchUtils.recheckConnections(pLevel, pPos, null);
            blockEntity.setDistance(newDistance);
        }
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            WinchBlockEntity blockEntity = (WinchBlockEntity) level.getBlockEntity(pos);
            blockEntity.drop();
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, PBBlockEntities.WINCH.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WinchBlockEntity(blockPos, blockState);
    }
}
