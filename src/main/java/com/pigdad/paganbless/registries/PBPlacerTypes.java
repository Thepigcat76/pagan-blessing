package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.worldgen.BlackThornFoliagePlacer;
import com.pigdad.paganbless.registries.worldgen.BlackThornTrunkPlacer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PBPlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(BuiltInRegistries.FOLIAGE_PLACER_TYPE, PaganBless.MODID);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, PaganBless.MODID);

    public static final Supplier<FoliagePlacerType<BlackThornFoliagePlacer>> BLACK_THORN_FOLIAGE_PLACER =
            FOLIAGE_PLACERS.register("black_thorn_foliage_placer", () -> new FoliagePlacerType<>(BlackThornFoliagePlacer.CODEC));
    public static final Supplier<TrunkPlacerType<BlackThornTrunkPlacer>> BLACK_THORN_TRUNK_PLACER =
            TRUNK_PLACERS.register("black_thorn_trunk_placer", () -> new TrunkPlacerType<>(BlackThornTrunkPlacer.CODEC));
}
