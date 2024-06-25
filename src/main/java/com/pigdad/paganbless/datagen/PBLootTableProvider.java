package com.pigdad.paganbless.datagen;

import com.pigdad.paganbless.registries.PBBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Collections;

public class PBLootTableProvider extends BlockLootSubProvider {
    protected PBLootTableProvider(HolderLookup.Provider provider) {
        super(Collections.emptySet(), FeatureFlags.VANILLA_SET, provider);
    }

    @Override
    protected void generate() {
        //dropSelf(PBBlocks.IMBUING_CAULDRON.get());
        /*
        dropSelf(PBBlocks.HERBALIST_BENCH.get());
        dropSelf(PBBlocks.WINCH.get());
        dropSelf(PBBlocks.CRANK.get());
        dropSelf(PBBlocks.RUNIC_CORE.get());

        dropSelf(PBBlocks.EMPTY_INCENSE.get());
        dropOther(PBBlocks.LAVENDER_INCENSE.get(), PBBlocks.EMPTY_INCENSE.get());
        dropOther(PBBlocks.RUE_INCENSE.get(), PBBlocks.EMPTY_INCENSE.get());

        dropSelf(PBBlocks.RUNE_SLAB_INERT.get());
        dropSelf(PBBlocks.RUNE_SLAB_AMETHYST.get());
        dropSelf(PBBlocks.RUNE_SLAB_QUARTZ.get());
        dropSelf(PBBlocks.RUNE_SLAB_LAPIS.get());
        dropSelf(PBBlocks.RUNE_SLAB_EMERALD.get());
        dropSelf(PBBlocks.RUNE_SLAB_DIAMOND.get());
        dropSelf(PBBlocks.RUNE_SLAB_QUARTZ.get());

        dropSelf(PBBlocks.HANGING_LAVENDER.get());
        dropSelf(PBBlocks.HANGING_RUE.get());
        dropSelf(PBBlocks.DRIED_HANGING_LAVENDER.get());
        dropSelf(PBBlocks.DRIED_HANGING_RUE.get());
         */
    }
}
