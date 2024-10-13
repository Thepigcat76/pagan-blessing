package com.pigdad.paganbless.content.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.content.blocks.EssenceLogBlock;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class BlackThornTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<BlackThornTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            trunkPlacerParts(instance).apply(instance, BlackThornTrunkPlacer::new)
    );

    public BlackThornTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    protected TrunkPlacerType<?> type() {
        return PBPlacerTypes.BLACK_THORN_TRUNK_PLACER.get();
    }

    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int height, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.below(), config);

        for (int i = 0; i < height; i++) {
            if (i > 0 && random.nextInt(0, 3) == 1) {
                BlockState state = PBBlocks.ESSENCE_BLACK_THORN_LOG.get().defaultBlockState();
                state = state.setValue(EssenceLogBlock.ESSENCE, true);
                blockSetter.accept(pos.above(i), state);
            } else {
                placeLog(level, blockSetter, random, pos.above(i), config);
            }
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pos.above(height), 0, false));
    }
}
