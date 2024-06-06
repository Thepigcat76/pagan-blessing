package com.pigdad.paganbless.data;

import com.pigdad.paganbless.registries.blocks.WicanWardBlock;
import com.pigdad.paganbless.utils.wicanward.DistanceLogics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class WicanWardSavedData extends SavedData {
    public static final String DATA_ID = "wicanward";

    private final HashSet<BlockPos> wicanWards;
    // Does not get serialized
    private int tickCounter;

    public WicanWardSavedData() {
        this(new HashSet<>());
    }

    public WicanWardSavedData(HashSet<BlockPos> wicanWards) {
        this.wicanWards = wicanWards;
    }

    public void addWicanWard(BlockPos blockPos) {
        this.wicanWards.add(blockPos);
        setDirty();
    }

    public void removeWicanWard(BlockPos blockPos) {
        this.wicanWards.remove(blockPos);
        setDirty();
    }

    public boolean shouldPreventEntitySpawn(BlockPos pos) {
        for (BlockPos wwPos : wicanWards) {
            if (DistanceLogics.Cubic.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), wwPos, WicanWardBlock.RANGE)) return true;
        }
        return false;

    }

    public static WicanWardSavedData load(CompoundTag nbt, ServerLevel level) {
        CompoundTag wwData = nbt.getCompound(DATA_ID);
        CompoundTag wicanWards = wwData.getCompound("wican_wards");
        int wicanWardsAmount = wwData.getInt("wican_wards_amount");

        HashSet<BlockPos> wicanWardsSet = new HashSet<>();

        for (int i = 0; i < wicanWardsAmount; i++) {
            wicanWardsSet.add(BlockPos.of(wicanWards.getLong(String.valueOf(i))));
        }

        return new WicanWardSavedData(wicanWardsSet);
    }

    public void onGlobalTick(Level level)
    {
        if(tickCounter++ < 200)
            return;
        tickCounter = 0;
        wicanWards.removeIf(wwPos -> cleanupCheck(level, wwPos));
    }

    private boolean cleanupCheck(Level level, BlockPos wwPos) {
        return level.isLoaded(wwPos) && !(level.getBlockState(wwPos).getBlock() instanceof WicanWardBlock);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag wwData = new CompoundTag();
        CompoundTag wicanWards = new CompoundTag();
        int i = 0;
        for (BlockPos pos : this.wicanWards) {
            wicanWards.putLong(String.valueOf(i), pos.asLong());
            i++;
        }
        wwData.put("wican_wards", wicanWards);
        wwData.putInt("wican_wards_amount", i);
        compoundTag.put(DATA_ID, wwData);
        return compoundTag;
    }

    public static SavedData.Factory<WicanWardSavedData> factory(ServerLevel pLevel) {
        return new SavedData.Factory<>(WicanWardSavedData::new, (p_294039_, p_324123_) -> load(p_294039_, pLevel));
    }
}
