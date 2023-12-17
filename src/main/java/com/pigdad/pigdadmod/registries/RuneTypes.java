package com.pigdad.pigdadmod.registries;

import net.minecraft.world.level.block.Block;

public enum RuneTypes implements RuneType {
    CINNABAR,
    DIAMOND,
    EMERALD,
    QUARTZ,
    AMETHYST,
    LAPIS;

    @Override
    public RuneType getTypeByBlock(Block block) {
        if (block.equals(ModBlocks.RUNE_SLAB_AMETHYST.get())) {
            return RuneTypes.AMETHYST;
        } else if (block.equals(ModBlocks.RUNE_SLAB_CINNABAR.get())) {
            return RuneTypes.CINNABAR;
        } else if (block.equals(ModBlocks.RUNE_SLAB_DIAMOND.get())) {
            return RuneTypes.DIAMOND;
        } else if (block.equals(ModBlocks.RUNE_SLAB_LAPIS.get())) {
            return RuneTypes.LAPIS;
        } else if (block.equals(ModBlocks.RUNE_SLAB_EMERALD.get())) {
            return RuneTypes.EMERALD;
        } else if (block.equals(ModBlocks.RUNE_SLAB_QUARTZ.get())) {
            return RuneTypes.QUARTZ;
        }
        return null;
    }
}
