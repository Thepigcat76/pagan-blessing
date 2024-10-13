package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.compat.modonomicon.ModonomiconCompat;
import com.pigdad.paganbless.content.items.PentacleItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class PBTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PaganBless.MODID);

    static {
        CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                .title(Component.translatable("tab.paganbless.main"))
                .icon(() -> PBBlocks.IMBUING_CAULDRON.get().asItem().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    // Blocks
                    output.accept(PBBlocks.IMBUING_CAULDRON.get());
                    output.accept(PBBlocks.HERBALIST_BENCH.get());
                    output.accept(PBBlocks.WINCH.get());
                    output.accept(PBBlocks.CRANK.get());
                    output.accept(PBBlocks.EMPTY_INCENSE.get());
                    output.accept(PBItems.JAR.get());
                    output.accept(PBBlocks.RUNIC_CORE.get());
                    output.accept(PBBlocks.RUNE_SLAB_INERT.get());
                    output.accept(PBBlocks.RUNE_SLAB_AMETHYST.get());
                    output.accept(PBBlocks.RUNE_SLAB_CINNABAR.get());
                    output.accept(PBBlocks.RUNE_SLAB_DIAMOND.get());
                    output.accept(PBBlocks.RUNE_SLAB_EMERALD.get());
                    output.accept(PBBlocks.RUNE_SLAB_QUARTZ.get());
                    output.accept(PBBlocks.RUNE_SLAB_LAPIS.get());
                    output.accept(PBBlocks.BELLADONNA_PLANT.get());
                    output.accept(PBBlocks.HAGS_TAPER_PLANT.get());
                    output.accept(PBBlocks.LAVENDER_PLANT.get());
                    output.accept(PBBlocks.MANDRAKE_ROOT_PLANT.get());
                    output.accept(PBBlocks.MUGWORT_PLANT.get());
                    output.accept(PBBlocks.RUE_PLANT.get());
                    output.accept(PBBlocks.HANGING_LAVENDER.get());
                    output.accept(PBBlocks.HANGING_RUE.get());
                    output.accept(PBBlocks.DRIED_HANGING_LAVENDER.get());
                    output.accept(PBBlocks.DRIED_HANGING_RUE.get());
                    output.accept(PBBlocks.WAXED_HANGING_LAVENDER.get());
                    output.accept(PBBlocks.WAXED_HANGING_RUE.get());

                    // Items
                    output.accept(PBItems.BELLADONNA.get());
                    output.accept(PBItems.HAG_TAPER.get());
                    output.accept(PBItems.LAVENDER.get());
                    output.accept(PBItems.MANDRAKE_ROOT.get());
                    output.accept(PBItems.MUGWORT.get());
                    output.accept(PBItems.RUE.get());
                    output.accept(PBItems.WINTER_BERRIES.get());
                    output.accept(PBItems.GLAZED_BERRIES.get());
                    output.accept(PBItems.CHOPPED_LAVENDER.get());
                    output.accept(PBItems.CHOPPED_RUE.get());

                    output.accept(PBItems.BLACK_THORN_STAFF.get());
                    output.accept(PBItems.BOLINE.get());
                    output.accept(PBItems.RUNIC_CHARGE.get());
                    addBook(output);
                    output.accept(PBItems.BLACK_THORN_STICK.get());
                    output.accept(PBItems.ROPE.get());
                    output.accept(PBItems.CINNABAR.get());
                    output.accept(PBItems.ESSENCE_OF_THE_FOREST.get());
                    output.accept(PBItems.MECHANICAL_COMPONENT.get());

                    // Artifacts
                    output.accept(PBItems.WAND.get());
                    output.accept(PBItems.CHALICE.get());
                    output.accept(PBItems.ETERNAL_SNOWBALL.get());
                    output.accept(PentacleItem.getPentacleDefaultStack());
                    output.accept(PBItems.ATHAME.get());
                    output.accept(PBItems.WICAN_WARD.get());

                    // Wood blocks
                    output.accept(PBBlocks.BLACK_THORN_LEAVES.get());
                    output.accept(PBBlocks.BLACK_THORN_SAPLING.get());
                    output.accept(PBBlocks.BLACK_THORN_PLANKS.get());
                    output.accept(PBBlocks.BLACK_THORN_SLAB.get());
                    output.accept(PBBlocks.BLACK_THORN_STAIRS.get());
                    output.accept(PBBlocks.BLACK_THORN_FENCE.get());
                    output.accept(PBBlocks.BLACK_THORN_FENCE_GATE.get());
                    output.accept(PBBlocks.BLACK_THORN_PRESSURE_PLATE.get());
                    output.accept(PBBlocks.BLACK_THORN_BUTTON.get());
                    output.accept(PBBlocks.BLACK_THORN_LOG.get());
                    output.accept(PBBlocks.STRIPPED_BLACK_THORN_LOG.get());
                    output.accept(PBBlocks.BLACK_THORN_WOOD.get());
                    output.accept(PBBlocks.STRIPPED_BLACK_THORN_WOOD.get());
                    output.accept(PBBlocks.BLACK_THORN_TRAPDOOR.get());
                    output.accept(PBBlocks.BLACK_THORN_DOOR.get());
                }).build());
    }

    public static void addBook(CreativeModeTab.Output output) {
        if (PBItems.PAGAN_GUIDE != null) {
            output.accept(ModonomiconCompat.getItemStack());
        }
    }
}
