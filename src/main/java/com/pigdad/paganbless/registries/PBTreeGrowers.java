package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class PBTreeGrowers {
    private static final ResourceKey<ConfiguredFeature<?, ?>> BLACK_THORN_KEY = registerConfigKey("black_thorn_tree");

    public static final TreeGrower BLACK_THORN = new TreeGrower("black_thorn", Optional.empty(), Optional.of(BLACK_THORN_KEY), Optional.empty());

    public static ResourceKey<ConfiguredFeature<?, ?>> registerConfigKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(PaganBless.MODID, name));
    }

    private PBTreeGrowers() {
    }
}
