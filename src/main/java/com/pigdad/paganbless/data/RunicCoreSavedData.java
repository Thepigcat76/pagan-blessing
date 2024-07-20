package com.pigdad.paganbless.data;

import com.pigdad.paganbless.utils.DistanceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class RunicCoreSavedData extends LocationsSavedData {
    public static final String DATA_ID = "runiccore";

    public RunicCoreSavedData() {
    }

    public RunicCoreSavedData(Set<BlockPos> blockPoses) {
        super(blockPoses);
    }

    @Override
    public String getDataId() {
        return DATA_ID;
    }

    public @Nullable BlockPos sacrificeInRange(BlockPos sacrificePos) {
        for (BlockPos rcPos : blocks) {
            if (DistanceUtils.CUBIC.isPositionInRange(sacrificePos.getX(), sacrificePos.getY(), sacrificePos.getZ(), rcPos, 2))
                return rcPos;
        }
        return null;
    }

    private static RunicCoreSavedData load(CompoundTag tag, ServerLevel level) {
        return new RunicCoreSavedData(LocationsSavedData.load(tag, DATA_ID, level));
    }

    public static LocationsSavedData.Factory<RunicCoreSavedData> factory(ServerLevel pLevel) {
        return new LocationsSavedData.Factory<>(RunicCoreSavedData::new, (tag) -> load(tag, pLevel));
    }
}