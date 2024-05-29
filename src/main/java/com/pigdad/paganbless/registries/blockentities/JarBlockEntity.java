package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class JarBlockEntity extends ContainerBlockEntity {
    public JarBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.JAR.get(), p_155229_, p_155230_);
        addItemHandler(1);
    }
}
