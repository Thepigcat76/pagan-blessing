package com.pigdad.paganbless;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = PaganBless.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PBConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_TYPES = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("entity_types", List.of("minecraft:pig"), PBConfig::validateEntityName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Set<EntityType<?>> entityTypes;

    private static boolean validateEntityName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        entityTypes = ENTITY_TYPES.get().stream()
                .map(itemName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());
    }
}
