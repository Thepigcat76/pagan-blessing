package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RueIncenseBlock extends IncenseBlock {
    public RueIncenseBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        PaganBless.LOGGER.debug("Ticking incense");
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(RueIncenseBlock::new);
    }
}
