package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.api.blocks.RotatableEntityBlock;
import com.pigdad.paganbless.api.blocks.TranslucentHighlightFix;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.registries.blockentities.HerbalistBenchBlockEntity;
import com.pigdad.paganbless.registries.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.paganbless.registries.recipes.BenchCuttingRecipe;
import com.pigdad.paganbless.utils.recipes.PBRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class HerbalistBenchBlock extends RotatableEntityBlock implements TranslucentHighlightFix {
    public static final EnumProperty<BenchVariant> BENCH_PART = EnumProperty.create("bench_part", BenchVariant.class);
    public static final VoxelShape NORTH_SHAPE = Stream.of(
            Block.box(0, 3, 4, 12, 11, 12),
            Block.box(0, 11, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 3, 16)
    ).reduce(Shapes::or).get();
    public static final VoxelShape EAST_SHAPE = Stream.of(
            Block.box(0, 11, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 3, 16),
            Block.box(4, 3, 4, 12, 11, 16)
    ).reduce(Shapes::or).get();
    public static final VoxelShape SOUTH_SHAPE = Stream.of(
            Block.box(0, 11, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 3, 16),
            Block.box(4, 3, 4, 16, 11, 12)
    ).reduce(Shapes::or).get();
    public static final VoxelShape WEST_SHAPE = Stream.of(
            Block.box(0, 11, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 3, 16),
            Block.box(4, 3, 0, 12, 11, 12)
    ).reduce(Shapes::or).get();

    public HerbalistBenchBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(BENCH_PART, BenchVariant.LEFT));
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(BENCH_PART) == BenchVariant.LEFT) {
            return switch (pState.getValue(FACING)) {
                case NORTH -> SOUTH_SHAPE;
                case EAST -> EAST_SHAPE;
                case SOUTH -> NORTH_SHAPE;
                case WEST -> WEST_SHAPE;
                default -> null;
            };
        } else {
            return switch (pState.getValue(FACING)) {
                case NORTH -> NORTH_SHAPE;
                case EAST -> WEST_SHAPE;
                case SOUTH -> SOUTH_SHAPE;
                case WEST -> EAST_SHAPE;
                default -> null;
            };
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return switch (pState.getValue(BENCH_PART)) {
            case RIGHT -> RenderShape.MODEL;
            case LEFT -> RenderShape.INVISIBLE;
        };
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(HerbalistBenchBlock::new);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState state, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        Direction value = state.getValue(FACING);
        level.setBlockAndUpdate(blockPos.relative(value.getClockWise()), this.defaultBlockState()
                .setValue(BENCH_PART, BenchVariant.RIGHT)
                .setValue(FACING, value));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getPlayer().getDirection();
        BlockState blockState = defaultBlockState().setValue(FACING, direction.getOpposite());
        BlockPos relativePos = context.getClickedPos().relative(blockState.getValue(FACING).getClockWise());
        if (!context.getLevel().getBlockState(relativePos).canBeReplaced())
            return null;
        return blockState;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        Direction direction = state.getValue(FACING);
        BlockPos pPos = pos.relative(state.getValue(BENCH_PART) == BenchVariant.LEFT ? direction.getClockWise() : direction.getCounterClockWise());
        if (level.getBlockState(pPos).is(this)) {
            level.removeBlock(pPos, false);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        BlockEntity blockEntity = pLevel.getBlockEntity(getMainBlock(pState, pPos));
        int slot = pState.getValue(BENCH_PART) == BenchVariant.LEFT ? 1 : 0;
        if (blockEntity instanceof HerbalistBenchBlockEntity herbalistBenchBlockEntity) {
            ItemStack stackInSlot = herbalistBenchBlockEntity.getItemHandler().getStackInSlot(slot);
            if (pStack.isEmpty()) {
                PaganBless.LOGGER.debug("Extract");
                ItemStack stack = herbalistBenchBlockEntity.getItemHandler().extractItem(slot, stackInSlot.getCount(), false);
                if (!stack.isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(pPlayer, stack);
                } else {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
            } else if (slot == 0 && pStack.is(PBTags.ItemTags.PAGAN_TOOLS)) {
                PaganBless.LOGGER.debug("Cut");
                cutItem(pLevel, pPlayer, herbalistBenchBlockEntity, pHand, stackInSlot, pStack);
            } else if (!pStack.isEmpty()) {
                if (stackInSlot.isEmpty() || (stackInSlot.is(pStack.getItem()) && stackInSlot.getCount() + pStack.getCount() <= pStack.getMaxStackSize())) {
                    int oldCount = pStack.getCount();
                    int count = herbalistBenchBlockEntity.getItemHandler().insertItem(slot, pStack.copy(), false).getCount();
                    if (oldCount != count) {
                        pStack.setCount(count);
                    } else {
                        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                    }
                } else {

                }
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    public static BlockPos getMainBlock(BlockState state, BlockPos pos) {
        return switch (state.getValue(BENCH_PART)) {
            case RIGHT -> pos;
            case LEFT -> pos.relative(state.getValue(FACING).getClockWise());
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(BENCH_PART));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return switch (blockState.getValue(BENCH_PART)) {
            case RIGHT -> new HerbalistBenchBlockEntity(blockPos, blockState);
            case LEFT -> null;
        };
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof HerbalistBenchBlockEntity blockEntity1) {
                blockEntity1.drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    public static void cutItem(Level level, Player player, HerbalistBenchBlockEntity blockEntity, InteractionHand hand, ItemStack itemStack, ItemStack tool) {
        PBRecipeInput container = new PBRecipeInput(itemStack, tool);
        Optional<BenchCuttingRecipe> recipeOptional = level.getRecipeManager()
                .getRecipeFor(BenchCuttingRecipe.Type.INSTANCE, container, level)
                .map(RecipeHolder::value);
        recipeOptional.ifPresent(recipe -> {
            if (recipe.tryDamage() && tool.isDamageableItem()) {
                tool.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }

            if (recipe.cuts() > 1) {
                if (blockEntity.getCuts() + 1 >= recipe.cuts()) {
                    performRecipeAction(level, blockEntity, itemStack, recipe);
                    blockEntity.setCuts(0);
                } else {
                    blockEntity.incrCuts();
                }
            } else {
                performRecipeAction(level, blockEntity, itemStack, recipe);
            }
        });
    }

    private static void performRecipeAction(Level level, HerbalistBenchBlockEntity blockEntity, ItemStack itemStack, BenchCuttingRecipe recipe) {
        itemStack.setCount(itemStack.getCount() - recipe.ingredient().count());
        BlockPos blockPos = blockEntity.getBlockPos().above();
        Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), recipe.resultStack().copy());
    }

    public enum BenchVariant implements StringRepresentable {
        RIGHT,
        LEFT;

        @Override
        public @NotNull String getSerializedName() {
            return switch (this) {
                case RIGHT -> "right";
                case LEFT -> "left";
            };
        }
    }
}
