package com.pigdad.paganbless.content.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IncenseBlockEntity extends BlockEntity {
    private boolean burning;
    private int burningProgress;

    public IncenseBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.INCENSE.get(), p_155229_, p_155230_);
    }

    public int getBurningProgress() {
        return burningProgress;
    }

    public void setBurningProgress(int burningProgress) {
        this.burningProgress = burningProgress;
    }

    public boolean isBurning() {
        return burning;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("burningProgress", burningProgress);
        pTag.putBoolean("burning", burning);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.burning = pTag.getBoolean("burning");
        this.burningProgress = pTag.getInt("burningProgress");
    }
}
