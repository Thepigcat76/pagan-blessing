package com.pigdad.paganbless.content.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HangingHerbBlockEntity extends BlockEntity {
    private int dryingProgress;

    public HangingHerbBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PBBlockEntities.HANGING_HERB.get(), pPos, pBlockState);
    }

    public void setDryingProgress(int dryingProgress) {
        this.dryingProgress = dryingProgress;
    }

    public int getDryingProgress() {
        return dryingProgress;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("drying_progress", this.dryingProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.dryingProgress = pTag.getInt("drying_progress");
    }
}
