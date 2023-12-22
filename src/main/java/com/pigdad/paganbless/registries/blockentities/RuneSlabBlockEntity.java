package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class RuneSlabBlockEntity extends BlockEntity {
    private @Nullable String prevBlock;

    public RuneSlabBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.RUNE_SLAB.get(), p_155229_, p_155230_);
    }

    public void setPrevBlock(String prevBlock) {
        this.prevBlock = prevBlock;
    }

    public @Nullable String getPrevBlock() {
        return prevBlock;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        if (prevBlock != null) {
            nbt.putString("prevBlock", prevBlock);
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        prevBlock = nbt.getString("prevBlock");
    }
}
