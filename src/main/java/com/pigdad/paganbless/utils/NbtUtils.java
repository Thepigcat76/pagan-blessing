package com.pigdad.paganbless.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.HashSet;
import java.util.Set;

public final class NbtUtils {
    /**
     * Will create a new tag in the `tag` parameter which will then have all the contents of the set.
     * Will also store an int that stores the length of the set in the `tag` parameter
     */
    public static void saveBlockPosSet(CompoundTag tag, String name, Set<BlockPos> blockPosSet) {
        tag.putInt(name+"_length", blockPosSet.size());
        CompoundTag setTag = new CompoundTag();
        int i = 0;
        for (BlockPos blockPos : blockPosSet) {
            setTag.putLong(String.valueOf(i), blockPos.asLong());
            i++;
        }
    }

    /**
     * Will take in a parent tag and then get the tag storing the set from it
     */
    public static Set<BlockPos> loadBlockPosSet(CompoundTag tag, String name) {
        int size = tag.getInt(name+"_length");
        CompoundTag setTag = tag.getCompound(name);
        Set<BlockPos> set = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            set.add(BlockPos.of(setTag.getLong(String.valueOf(i))));
        }
        return set;
    }
}
