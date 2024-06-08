package com.pigdad.paganbless.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public abstract class LocationsSavedData extends SavedData {
    protected Set<BlockPos> blocks;
    // Does not get serialized
    private int tickCounter;

    public LocationsSavedData() {
        this(new HashSet<>());
    }

    public LocationsSavedData(Set<BlockPos> blocks) {
        this.blocks = blocks;
    }

    public abstract String getDataId();

    public void addBlockPos(BlockPos blockPos) {
        this.blocks.add(blockPos);
        setDirty();
    }

    public void removeBlockPos(BlockPos blockPos) {
        this.blocks.remove(blockPos);
        setDirty();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag blocksData = new CompoundTag();
        CompoundTag blocksTag = new CompoundTag();
        int i = 0;
        for (BlockPos pos : this.blocks) {
            blocksTag.putLong(String.valueOf(i), pos.asLong());
            i++;
        }
        blocksData.put("blocks", blocksTag);
        blocksData.putInt("blocks_amount", i);
        compoundTag.put(getDataId(), blocksData);
        return compoundTag;
    }

    public static Set<BlockPos> load(CompoundTag nbt, String dataId, ServerLevel level) {
        CompoundTag wwData = nbt.getCompound(dataId);
        CompoundTag blocksTag = wwData.getCompound("blocks");
        int blocksAmount = wwData.getInt("blocks_amount");
        Set<BlockPos> blocks = new HashSet<>();

        for (int i = 0; i < blocksAmount; i++) {
            blocks.add(BlockPos.of(blocksTag.getLong(String.valueOf(i))));
        }

        return blocks;
    }

    @Override
    public String toString() {
        return "LocationsSavedData{" +
                "blocks=" + blocks +
                ", tickCounter=" + tickCounter +
                '}';
    }
}
