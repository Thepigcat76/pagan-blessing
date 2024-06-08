package com.pigdad.paganbless.data;

import com.pigdad.paganbless.registries.blocks.WicanWardBlock;
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

    public @Nullable BlockPos sacrificeInRange(BlockPos pos) {
        for (BlockPos rcPos : blocks) {
            if (DistanceUtils.CUBIC.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), rcPos, 2))
                return rcPos;
        }
        return null;
    }

    private static RunicCoreSavedData load(CompoundTag tag, ServerLevel level) {
        return new RunicCoreSavedData(LocationsSavedData.load(tag, DATA_ID, level));
    }

    public static SavedData.Factory<RunicCoreSavedData> factory(ServerLevel pLevel) {
        return new SavedData.Factory<>(RunicCoreSavedData::new, (tag, provider) -> load(tag, pLevel));
    }
}
