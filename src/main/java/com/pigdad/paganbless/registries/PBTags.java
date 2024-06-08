package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class PBTags {
    public final static class ItemTags {
        public static final TagKey<Item> HERBS = bind("herbs");
        public static final TagKey<Item> HERB_PLANTS = bind("herb_plants");
        public static final TagKey<Item> PAGAN_TOOLS = bind("pagan_tools");

        private ItemTags() {
        }

        private static TagKey<Item> bind(String p_203855_) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(PaganBless.MODID, p_203855_));
        }

        public static TagKey<Item> create(final ResourceLocation name) {
            return TagKey.create(Registries.ITEM, name);
        }
    }

    public final static class BlockTags {
        public static final TagKey<Block> HEAT_SOURCE = bind("heat_source");
        public static final TagKey<Block> RUNIC_CHARGE_NO_REFILL = bind("runic_charge_no_refill");

        private BlockTags() {
        }

        private static TagKey<Block> bind(String p_203855_) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(PaganBless.MODID, p_203855_));
        }

        public static TagKey<Block> create(final ResourceLocation name) {
            return TagKey.create(Registries.BLOCK, name);
        }
    }
}
