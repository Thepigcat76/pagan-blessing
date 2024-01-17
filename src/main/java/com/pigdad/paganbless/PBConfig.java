package com.pigdad.paganbless;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = PaganBless.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PBConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // a list of strings that are treated as resource locations for items
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ENTITY_TYPES = BUILDER
            .comment("A blacklist for mobs that should not be captured by the pentacle on sacrifice")
            .defineListAllowEmpty("pentacle_blacklisted", List.of("minecraft:wither", "minecraft:warden", "minecraft:ender_dragon"), PBConfig::validateEntityName);

    protected static final ModConfigSpec SPEC = BUILDER.build();

    public static Set<EntityType<?>> entityTypes;

    private static boolean validateEntityName(final Object obj) {
        return obj instanceof final String itemName && BuiltInRegistries.ENTITY_TYPE.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        entityTypes = ENTITY_TYPES.get().stream()
                .map(itemName -> BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());
    }
}
