package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrankBlockEntity extends BlockEntity {
    public CrankBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PBBlockEntities.CRANK.get(), pPos, pBlockState);
    }
}
