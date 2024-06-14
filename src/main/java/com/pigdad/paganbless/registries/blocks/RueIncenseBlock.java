package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.blockentities.IncenseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class RueIncenseBlock extends IncenseBlock {
    public RueIncenseBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void effectTick(Level level, BlockPos blockPos, BlockState blockState, IncenseBlockEntity blockEntity) {

    }

    @Override
    public Item getIncenseItem() {
        return PBItems.RUE.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(RueIncenseBlock::new);
    }
}
