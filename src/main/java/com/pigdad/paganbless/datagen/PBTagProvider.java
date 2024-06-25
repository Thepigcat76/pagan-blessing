package com.pigdad.paganbless.datagen;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.PBTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PBTagProvider {
    public static class ItemsProvider extends ItemTagsProvider {
        public ItemsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags) {
            super(pOutput, pLookupProvider, pBlockTags);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(PBTags.ItemTags.PAGAN_TOOLS).add(PBItems.BLACK_THORN_STAFF.get(), PBItems.BOLINE.get());
            tag(PBTags.ItemTags.FIRE_LIGHTER).add(Items.FLINT_AND_STEEL, Items.FIRE_CHARGE);
            tag(PBTags.ItemTags.GEMS_CINNABAR).add(PBItems.CINNABAR.get());
            tag(Tags.Items.RODS_WOODEN).add(PBItems.BLACK_THORN_STICK.get());

            tag(PBTags.ItemTags.HANGING_HERBS).add(PBBlocks.HANGING_RUE.get().asItem(), PBBlocks.HANGING_LAVENDER.get().asItem());
            tag(PBTags.ItemTags.DRIED_HERBS).add(PBBlocks.DRIED_HANGING_RUE.get().asItem(), PBBlocks.DRIED_HANGING_LAVENDER.get().asItem());
            tag(PBTags.ItemTags.CHOPPED_HERBS).add(PBItems.CHOPPED_RUE.get().asItem(), PBItems.CHOPPED_LAVENDER.get().asItem());

            tag(PBTags.ItemTags.HERB_PLANTS).add(PBBlocks.BELLADONNA_PLANT.get().asItem(), PBBlocks.HAGS_TAPER_PLANT.get().asItem(), PBBlocks.LAVENDER_PLANT.get().asItem(),
                    PBBlocks.MANDRAKE_ROOT_PLANT.get().asItem(), PBBlocks.MUGWORT_PLANT.get().asItem(), PBBlocks.RUE_PLANT.get().asItem(),
                    PBItems.WINTER_BERRIES.get());
            tag(PBTags.ItemTags.HERBS).add(PBItems.LAVENDER.get(), PBItems.RUE.get(), PBItems.BELLADONNA.get(),
                    PBItems.HAG_TAPER.get(), PBItems.MUGWORT.get(), PBItems.MANDRAKE_ROOT.get(),
                    PBItems.WINTER_BERRIES.get());
            if (PBItems.PAGAN_GUIDE != null) {
                tag(ItemTags.BOOKSHELF_BOOKS).add(PBItems.PAGAN_GUIDE.get());
            }
        }
    }

    public static class BlocksProvider extends BlockTagsProvider {
        public BlocksProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, PaganBless.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.LEAVES, PBBlocks.BLACK_THORN_LEAVES.get());
            tag(BlockTags.LOGS_THAT_BURN,
                    PBBlocks.BLACK_THORN_LOG.get(),
                    PBBlocks.STRIPPED_BLACK_THORN_LOG.get(),
                    PBBlocks.STRIPPED_BLACK_THORN_LOG.get(),
                    PBBlocks.BLACK_THORN_WOOD.get(),
                    PBBlocks.STRIPPED_BLACK_THORN_WOOD.get()
            );

            tag(BlockTags.WOODEN_BUTTONS, PBBlocks.BLACK_THORN_BUTTON.get());
            tag(BlockTags.WOODEN_DOORS, PBBlocks.BLACK_THORN_DOOR.get());
            tag(BlockTags.WOODEN_FENCES, PBBlocks.BLACK_THORN_FENCE.get());
            tag(BlockTags.FENCE_GATES, PBBlocks.BLACK_THORN_FENCE_GATE.get());
            tag(BlockTags.WOODEN_SLABS, PBBlocks.BLACK_THORN_SLAB.get());
            tag(BlockTags.WOODEN_STAIRS, PBBlocks.BLACK_THORN_STAIRS.get());
            tag(BlockTags.WOODEN_TRAPDOORS, PBBlocks.BLACK_THORN_TRAPDOOR.get());

            tag(BlockTags.MINEABLE_WITH_PICKAXE,
                    PBBlocks.IMBUING_CAULDRON.get(),
                    PBBlocks.EMPTY_INCENSE.get(),
                    PBBlocks.HERBALIST_BENCH.get(),
                    PBBlocks.CRANK.get(),
                    PBBlocks.WINCH.get(),
                    PBBlocks.RUE_INCENSE.get(),
                    PBBlocks.LAVENDER_INCENSE.get()
            );

            tag(BlockTags.MINEABLE_WITH_AXE, PBBlocks.RUNIC_CORE.get(), PBBlocks.CRANK.get());
            for (Supplier<Block> block : PBBlocks.WOOD_BLOCKS) {
                tag(BlockTags.MINEABLE_WITH_AXE, block.get());
            }
        }

        private @NotNull IntrinsicTagAppender<Block> tag(TagKey<Block> blockTagKey, Block... blocks) {
            return tag(blockTagKey).add(blocks);
        }
    }
}
