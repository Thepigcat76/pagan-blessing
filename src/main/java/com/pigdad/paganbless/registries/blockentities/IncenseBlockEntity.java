package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IncenseBlockEntity extends BlockEntity {
    public IncenseBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.INCENSE.get(), p_155229_, p_155230_);
    }
}
