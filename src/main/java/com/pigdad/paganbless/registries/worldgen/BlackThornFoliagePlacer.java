package com.pigdad.paganbless.registries.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.registries.PBPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class BlackThornFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<BlackThornFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            foliagePlacerParts(instance).apply(instance, BlackThornFoliagePlacer::new)
    );

    public BlackThornFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return PBPlacerTypes.BLACK_THORN_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliagePlacer.FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        boolean flag = attachment.doubleTrunk();
        BlockPos blockpos = attachment.pos();
        this.placeLeavesRow(level, foliageSetter, random, config, blockpos, foliageRadius, foliageHeight - 3, flag);
        this.placeLeavesRow(level, foliageSetter, random, config, blockpos, foliageRadius - 1, foliageHeight - 2, flag);
        this.placeLeavesRow(level, foliageSetter, random, config, blockpos, 0, foliageHeight - 1, flag);
    }

    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return height;
    }

    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        if (range == localX && range == localZ & range > 0)
            return random.nextBoolean();
        return false;
    }
}
