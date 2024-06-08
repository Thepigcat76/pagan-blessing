package com.pigdad.paganbless.data;

import com.pigdad.paganbless.registries.blocks.WicanWardBlock;
import com.pigdad.paganbless.utils.DistanceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Set;

public class WicanWardSavedData extends LocationsSavedData {
    public static final String DATA_ID = "wicanward";

    // Does not get serialized
    private int tickCounter;

    public WicanWardSavedData() {
    }

    @Override
    public String getDataId() {
        return DATA_ID;
    }

    public WicanWardSavedData(Set<BlockPos> wicanWards) {
        this.blocks = wicanWards;
    }

    public boolean shouldPreventEntitySpawn(BlockPos pos) {
        for (BlockPos wwPos : blocks) {
            if (DistanceUtils.CUBIC.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), wwPos, WicanWardBlock.RANGE))
                return true;
        }
        return false;
    }

    public void onGlobalTick(Level level) {
        if (tickCounter++ < 200)
            return;
        tickCounter = 0;
        blocks.removeIf(wwPos -> cleanupCheck(level, wwPos));
    }

    private boolean cleanupCheck(Level level, BlockPos wwPos) {
        return level.isLoaded(wwPos) && !(level.getBlockState(wwPos).getBlock() instanceof WicanWardBlock);
    }

    private static WicanWardSavedData load(CompoundTag nbt, ServerLevel level) {
        return new WicanWardSavedData(LocationsSavedData.load(nbt, DATA_ID, level));
    }

    public static SavedData.Factory<WicanWardSavedData> factory(ServerLevel pLevel) {
        return new SavedData.Factory<>(WicanWardSavedData::new, (tag, provider) -> load(tag, pLevel));
    }
}
