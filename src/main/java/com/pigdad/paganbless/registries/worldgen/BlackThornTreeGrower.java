package com.pigdad.paganbless.registries.worldgen;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class BlackThornTreeGrower {
    @Nullable
    public ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean p_222911_) {
        return registerConfigKey("black_thorn_tree");
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerConfigKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(PaganBless.MODID, name));
    }
}
