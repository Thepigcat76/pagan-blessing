package com.pigdad.pigdadmod.registries;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.worldgen.BlackThornFoliagePlacer;
import com.pigdad.pigdadmod.registries.worldgen.BlackThornTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, PigDadMod.MODID);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, PigDadMod.MODID);

    public static final RegistryObject<FoliagePlacerType<BlackThornFoliagePlacer>> BLACK_THORN_FOLIAGE_PLACER =
            FOLIAGE_PLACERS.register("black_thorn_foliage_placer", () -> new FoliagePlacerType<>(BlackThornFoliagePlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<BlackThornTrunkPlacer>> BLACK_THORN_TRUNK_PLACER =
            TRUNK_PLACERS.register("black_thorn_trunk_placer", () -> new TrunkPlacerType<>(BlackThornTrunkPlacer.CODEC));
}
