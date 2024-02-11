package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrankBlock extends BaseEntityBlock {
    public CrankBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CrankBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrankBlockEntity(blockPos, blockState);
    }
}
