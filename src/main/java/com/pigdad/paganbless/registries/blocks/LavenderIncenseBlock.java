package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LavenderIncenseBlock extends IncenseBlock{
    public LavenderIncenseBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {

    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(LavenderIncenseBlock::new);
    }
}
