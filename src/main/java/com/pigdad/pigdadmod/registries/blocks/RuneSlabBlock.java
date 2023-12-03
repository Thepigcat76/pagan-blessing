package com.pigdad.pigdadmod.registries.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class RuneSlabBlock extends Block {
    public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");

    public RuneSlabBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        level.setBlockAndUpdate(blockPos.offset(0, 1, 0), this.defaultBlockState().setValue(IS_TOP, true));
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (state.getValue(IS_TOP)) {
            level.removeBlock(pos.offset(0, -1, 0), true);
        } else {
            level.removeBlock(pos.offset(0, 1, 0), true);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(IS_TOP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(IS_TOP, false);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Stream.of(
                Block.box(3, 0, 3, 13, 2, 13),
                Block.box(4, 2, 4, 12, 12, 12),
                Block.box(4.5, 12, 4.5, 11.5, 24, 11.5)
        ).reduce(Shapes::or).get();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        if (blockState.getValue(IS_TOP)) {
            return RenderShape.INVISIBLE;
        } else {
            return RenderShape.MODEL;
        }
    }
}
