package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.blockentities.RuneSlabBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

import static com.pigdad.paganbless.registries.blocks.RotatableEntityBlock.FACING;

public class RuneSlabBlock extends BaseEntityBlock {
    public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");
    public static final EnumProperty<RuneState> RUNE_STATE = EnumProperty.create("rune_state", RuneState.class);

    public RuneSlabBlock(Properties properties) {
        super(properties.mapColor(MapColor.STONE).noOcclusion());
        registerDefaultState(this.defaultBlockState().setValue(IS_TOP, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(RuneSlabBlock::new);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        level.setBlockAndUpdate(blockPos.above(), this.defaultBlockState().setValue(IS_TOP, true));
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
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
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

    @Override
    public void appendHoverText(ItemStack p_49816_, Item.TooltipContext context, List<Component> tooltip, TooltipFlag p_49819_) {
        tooltip.add(Component.translatable("desc.paganbless.rune_slab").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack p_316304_, BlockState p_316362_, Level level, BlockPos blockPos, Player p_316132_, InteractionHand p_316595_, BlockHitResult p_316140_) {
        if (p_316304_.is(PBItems.BLACK_THORN_STAFF.get())) {
            incrementRuneState(level, blockPos);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    public static void incrementRuneState(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        // Check if player is interacting with top or bottom part of the block
        if (blockState.getValue(IS_TOP)) {
            if (blockState.getValue(RUNE_STATE).equals(RuneState.VARIANT5)) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(RUNE_STATE, RuneState.VARIANT0));
                // always update bottom and top blocks
                level.setBlockAndUpdate(blockPos.offset(0, -1, 0),
                        level.getBlockState(blockPos.offset(0, -1, 0))
                                .setValue(RUNE_STATE, RuneState.VARIANT0));
                return;
            }
            level.setBlockAndUpdate(blockPos, blockState.setValue(RUNE_STATE, RuneState.getByIndex(blockState.getValue(RUNE_STATE).ordinal() + 1)));
            level.setBlockAndUpdate(blockPos.offset(0, -1, 0),
                    level.getBlockState(blockPos.offset(0, -1, 0))
                            .setValue(RUNE_STATE, RuneState.getByIndex(blockState.getValue(RUNE_STATE).ordinal() + 1)));
        } else {
            if (blockState.getValue(RUNE_STATE).equals(RuneState.VARIANT5)) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(RUNE_STATE, RuneState.VARIANT0));
                level.setBlockAndUpdate(blockPos.offset(0, 1, 0),
                        level.getBlockState(blockPos.offset(0, 1, 0))
                                .setValue(RUNE_STATE, RuneState.VARIANT0));
                return;
            }
            level.setBlockAndUpdate(blockPos, blockState.setValue(RUNE_STATE, RuneState.getByIndex(blockState.getValue(RUNE_STATE).ordinal() + 1)));
            level.setBlockAndUpdate(blockPos.offset(0, 1, 0),
                    level.getBlockState(blockPos.offset(0, 1, 0))
                            .setValue(RUNE_STATE, RuneState.getByIndex(blockState.getValue(RUNE_STATE).ordinal() + 1)));
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
