package com.pigdad.paganbless.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaxedHangingHerbBlock extends BaseHangingHerbBlock {
    public static final int MAX_HANGING_AMOUNT = 4;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty HANGING_AMOUNT = IntegerProperty.create("hanging_amount", 1, MAX_HANGING_AMOUNT);
    public static final VoxelShape SHAPE_2_NS = Block.box(3, 0, 4, 13, 16, 12);
    public static final VoxelShape SHAPE_3_NS = Block.box(2, 0, 3, 14, 16, 13);
    public static final VoxelShape SHAPE_4_NS = Block.box(0, 0, 3, 16, 16, 13);

    public static final VoxelShape SHAPE_2_EW = Block.box(4, 0, 3, 12, 16, 13);
    public static final VoxelShape SHAPE_3_EW = Block.box(3, 0, 2, 13, 16, 14);
    public static final VoxelShape SHAPE_4_EW = Block.box(3, 0, 0, 13, 16, 16);

    public WaxedHangingHerbBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(FACING) == Direction.NORTH || pState.getValue(FACING) == Direction.SOUTH) {
            return switch (pState.getValue(HANGING_AMOUNT)) {
                case 2 -> SHAPE_2_NS;
                case 3 -> SHAPE_3_NS;
                case 4 -> SHAPE_4_NS;
                default -> SHAPE;
            };
        } else {
            return switch (pState.getValue(HANGING_AMOUNT)) {
                case 2 -> SHAPE_2_EW;
                case 3 -> SHAPE_3_EW;
                case 4 -> SHAPE_4_EW;
                default -> SHAPE;
            };
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int amount = state.getValue(HANGING_AMOUNT);
        if (Block.byItem(stack.getItem()) == this) {
            if (amount < MAX_HANGING_AMOUNT) {
                level.setBlockAndUpdate(pos, state.setValue(HANGING_AMOUNT, amount + 1));
                level.playSound(player, pos, SoundEvents.BAMBOO_SAPLING_PLACE, SoundSource.BLOCKS);
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                return ItemInteractionResult.SUCCESS;
            }
        } else if (stack.isEmpty()) {
            if (amount > 1) {
                level.setBlockAndUpdate(pos, state.setValue(HANGING_AMOUNT, amount - 1));
                ItemHandlerHelper.giveItemToPlayer(player, this.asItem().getDefaultInstance());
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state != null ? state.setValue(FACING, context.getPlayer().getDirection()) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(HANGING_AMOUNT, FACING));
    }
}
