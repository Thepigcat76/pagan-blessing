package com.pigdad.pigdadmod.registries;

import com.pigdad.pigdadmod.PigDadMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PigDadMod.MODID);
    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.pigdadmod.main"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModBlocks.IMBUING_CAULDRON.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.RUNE_SLAB_AMETHYST.get());
                output.accept(ModBlocks.RUNE_SLAB_CINNABAR.get());
                output.accept(ModBlocks.RUNE_SLAB_DIAMOND.get());
                output.accept(ModBlocks.RUNE_SLAB_EMERALD.get());
                output.accept(ModBlocks.RUNE_SLAB_QUARTZ.get());
                output.accept(ModBlocks.RUNE_SLAB_LAPIS.get());
                output.accept(ModBlocks.IMBUING_CAULDRON.get());
                output.accept(ModBlocks.BELLADONNA_PLANT.get());
                output.accept(ModBlocks.HAGS_TAPER_PLANT.get());
                output.accept(ModBlocks.LAVENDER_PLANT.get());
                output.accept(ModBlocks.MANDRAKE_ROOT_PLANT.get());
                output.accept(ModBlocks.MUGWORT_PLANT.get());
                output.accept(ModBlocks.RUE_PLANT.get());
                output.accept(ModItems.BELLADONNA.get());
                output.accept(ModItems.HAG_TAPER.get());
                output.accept(ModItems.LAVENDER.get());
                output.accept(ModItems.MANDRAKE_ROOT.get());
                output.accept(ModItems.MUGWORT.get());
                output.accept(ModItems.RUE.get());
                output.accept(ModItems.WINTER_BERRIES.get());
                output.accept(ModItems.CINNABAR.get());
            }).build());
}
