package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.blockentities.RuneSlabBlockEntity;
import com.pigdad.paganbless.registries.items.RunicChargeItem;
import com.pigdad.paganbless.utils.PBParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class RuneSlabBlock extends BaseEntityBlock {
    public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");
    public static final EnumProperty<RuneState> RUNE_STATE = EnumProperty.create("rune_state", RuneState.class);

    private final int color;
    private final boolean inert;

    public RuneSlabBlock(Properties properties, int color, boolean inert) {
        super(properties);
        registerDefaultState(this.defaultBlockState()
                .setValue(IS_TOP, false)
                .setValue(RUNE_STATE, RuneState.VARIANT0));
        this.color = color;
        this.inert = inert;
    }

    public int getColor() {
        return color;
    }

    public boolean isInert() {
        return this.inert;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((properties1) -> new RuneSlabBlock(properties1, 0, inert));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        level.setBlockAndUpdate(blockPos.above(), this.defaultBlockState()
                .setValue(IS_TOP, true)
                .setValue(RUNE_STATE, p_49849_.getValue(RUNE_STATE)));
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (state.getValue(IS_TOP)) {
            level.removeBlock(pos.below(), true);
        } else {
            level.removeBlock(pos.above(), true);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(IS_TOP, RUNE_STATE);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        if (p_60555_.getValue(IS_TOP)) {
            return Stream.of(
                    Block.box(3, -16, 3, 13, -14, 13),
                    Block.box(4, -14, 4, 12, -4, 12),
                    Block.box(4.5, -4, 4.5, 11.5, 8, 11.5)
            ).reduce(Shapes::or).get();
        }
        return Stream.of(
                Block.box(3, 0, 3, 13, 2, 13),
                Block.box(4, 2, 4, 12, 12, 12),
                Block.box(4.5, 12, 4.5, 11.5, 24, 11.5)
        ).reduce(Shapes::or).get();
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        if (blockState.getValue(IS_TOP)) {
            return RenderShape.INVISIBLE;
        } else {
            return RenderShape.MODEL;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        RandomSource randomSource = context.getLevel().random;
        int i = randomSource.nextInt(0, RuneState.values().length);
        return super.getStateForPlacement(context).setValue(RUNE_STATE, RuneState.values()[i]);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand p_316595_, BlockHitResult p_316140_) {
        if ((itemStack.getItem() instanceof RunicChargeItem))
            return ItemInteractionResult.FAIL;

        if (itemStack.is(PBItems.BLACK_THORN_STAFF.get()))
            incrementRuneState(level, blockState, blockPos);

        player.displayClientMessage(Component.literal("Rune State: " + blockState.getValue(RUNE_STATE).ordinal()), true);

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (state.getValue(IS_TOP) && isInert()) {
            ParticleUtils.spawnParticles(level, pos, 2, 0.4, 0.4, false, ParticleTypes.ASH);
        }
    }

    public static void incrementRuneState(Level level, BlockState blockState, BlockPos blockPos) {
        // Check if player is interacting with top or bottom part of the block
        if (blockState.getValue(IS_TOP)) {
            onIncrementSlab(level, blockPos, false);
            if (blockState.getValue(RUNE_STATE).equals(RuneState.VARIANT5)) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(RUNE_STATE, RuneState.VARIANT0));
                // always update bottom and top blocks
                updateBottomSlabFromTop(level, blockPos, RuneState.VARIANT0);
                return;
            }
            level.setBlockAndUpdate(blockPos, blockState.setValue(RUNE_STATE, RuneState.getByIndex(blockState.getValue(RUNE_STATE).ordinal() + 1)));
            updateBottomSlabFromTop(level, blockPos, RuneState.getByIndex(blockState.getValue(RUNE_STATE).ordinal() + 1));
        } else {
            onIncrementSlab(level, blockPos.above(), true);
            if (blockState.getValue(RUNE_STATE).equals(RuneState.VARIANT5)) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(RUNE_STATE, RuneState.VARIANT0));
                updateTopSlabFromBottom(level, blockPos, RuneState.VARIANT0);
                return;
            }
            level.setBlockAndUpdate(blockPos, blockState.setValue(RUNE_STATE, RuneState.getByIndex(blockState.getValue(RUNE_STATE).ordinal() + 1)));
            updateTopSlabFromBottom(level, blockPos, RuneState.getByIndex(blockState.getValue(RUNE_STATE).ordinal() + 1));
        }
    }

    private static void updateBottomSlabFromTop(Level level, BlockPos blockPos, RuneState state) {
        level.setBlockAndUpdate(blockPos.below(),
                level.getBlockState(blockPos.below()).setValue(RUNE_STATE, state));
    }

    private static void updateTopSlabFromBottom(Level level, BlockPos blockPos, RuneState state) {
        level.setBlockAndUpdate(blockPos.above(),
                level.getBlockState(blockPos.above()).setValue(RUNE_STATE, state));
    }

    private static void onIncrementSlab(Level level, BlockPos blockPos, boolean isBottom) {
        level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.DEEPSLATE_PLACE, SoundSource.BLOCKS);
        if (level.isClientSide()) {
            int count = 3;
            for (int i = 0; i < count; i++) {
                int rand = level.random.nextInt(-3, 3);
                PBParticleUtils.spawnBreakParticleForRuneSlab(blockPos, rand, i);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        if (!p_153216_.getValue(IS_TOP)) {
            return new RuneSlabBlockEntity(p_153215_, p_153216_);
        }
        return null;
    }

    public enum RuneState implements StringRepresentable {
        VARIANT0("variant0"),
        VARIANT1("variant1"),
        VARIANT2("variant2"),
        VARIANT3("variant3"),
        VARIANT4("variant4"),
        VARIANT5("variant5");

        private final String name;

        RuneState(String name) {
            this.name = name;
        }

        public static RuneState getByIndex(int index) {
            return switch (index) {
                case 0 -> RuneState.VARIANT0;
                case 1 -> RuneState.VARIANT1;
                case 2 -> RuneState.VARIANT2;
                case 3 -> RuneState.VARIANT3;
                case 4 -> RuneState.VARIANT4;
                case 5 -> RuneState.VARIANT5;
                default -> throw new IllegalStateException("Unexpected value: " + index);
            };
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
