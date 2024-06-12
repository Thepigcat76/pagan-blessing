package com.pigdad.paganbless.registries.blocks;

import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.IncenseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public abstract class IncenseBlock extends BaseEntityBlock {
    public static final EnumProperty<IncenseStates> INCENSE_STATE = EnumProperty.create("incense_state", IncenseStates.class);
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    public IncenseBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(INCENSE_STATE, ROTATION));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new IncenseBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, PBBlockEntities.INCENSE.get(), pLevel.isClientSide() ? this::clientTick : this::serverTick);
    }

    public void clientTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {

    }

    public abstract void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity);

    public enum IncenseStates implements StringRepresentable {
        EMPTY("empty"),
        ASH("ash"),
        ONE("one"),
        TWO("two"),
        THREE("three"),
        FOUR("four");

        private final String name;

        IncenseStates(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
