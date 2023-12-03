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
    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.pigdadmod.main"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModBlocks.IMBUING_CAULDRON.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.RUNE.get());
                output.accept(ModBlocks.IMBUING_CAULDRON.get());
            }).build());
}
