package com.pigdad.pigdadmod.registries.blockentities;

import com.pigdad.pigdadmod.registries.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RunicCoreBlockEntity extends BlockEntity {
    public RunicCoreBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.RUNIC_CORE.get(), p_155229_, p_155230_);
    }
}
