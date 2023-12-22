package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PBTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PaganBless.MODID);
    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.paganbless.main"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> PBBlocks.IMBUING_CAULDRON.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(PBBlocks.IMBUING_CAULDRON.get());
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
                output.accept(PBItems.BELLADONNA.get());
                output.accept(PBItems.HAG_TAPER.get());
                output.accept(PBItems.LAVENDER.get());
                output.accept(PBItems.MANDRAKE_ROOT.get());
                output.accept(PBItems.MUGWORT.get());
                output.accept(PBItems.RUE.get());
                output.accept(PBItems.WINTER_BERRIES.get());
                output.accept(PBItems.RUNIC_CHARGE.get());
                output.accept(PBItems.CINNABAR.get());
                output.accept(PBItems.ATHAME.get());
                output.accept(PBItems.CHALICE.get());
                output.accept(PBItems.ETERNAL_SNOWBALL.get());
                output.accept(PBItems.PENTACLE.get());
                output.accept(PBItems.WAND.get());
                output.accept(PBItems.WICAN_WARD.get());
                output.accept(PBBlocks.BLACK_THORN_LOG.get());
                output.accept(PBBlocks.BLACK_THORN_LEAVES.get());
                output.accept(PBBlocks.BLACK_THORN_SAPLING.get());
            }).build());
}
