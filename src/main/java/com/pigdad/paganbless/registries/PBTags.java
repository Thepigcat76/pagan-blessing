package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public final class PBTags {
    public final static class Item {
        public static final TagKey<net.minecraft.world.item.Item> HERBS = bind("herbs");
        public static final TagKey<net.minecraft.world.item.Item> HERB_PLANTS = bind("herb_plants");

        private Item() {
        }

        private static TagKey<net.minecraft.world.item.Item> bind(String p_203855_) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(PaganBless.MODID, p_203855_));
        }

        public static TagKey<net.minecraft.world.item.Item> create(final ResourceLocation name) {
            return TagKey.create(Registries.ITEM, name);
        }
    }

    public final static class Block {
        public static final TagKey<net.minecraft.world.level.block.Block> HEAT_SOURCE = bind("heat_source");

        private Block() {
        }

        private static TagKey<net.minecraft.world.level.block.Block> bind(String p_203855_) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(PaganBless.MODID, p_203855_));
        }

        public static TagKey<net.minecraft.world.level.block.Block> create(final ResourceLocation name) {
            return TagKey.create(Registries.BLOCK, name);
        }
    }
}
