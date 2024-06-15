package com.pigdad.paganbless;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@EventBusSubscriber(modid = PaganBless.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class PBConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // a list of strings that are treated as resource locations for items
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ENTITY_TYPES = BUILDER
            .comment("A blacklist for mobs that should not be captured by the pentacle on sacrifice")
            .defineListAllowEmpty("pentacle_blacklisted", List.of("minecraft:wither", "minecraft:warden", "minecraft:ender_dragon"), PBConfig::validateEntityName);
    private static final ModConfigSpec.IntValue WICAN_WARD_RANGE = BUILDER
            .comment("The range that the wican ward will prevent mob spawning in. This area is cubic")
            .defineInRange("wican_ward_range", 10, 0, 128);
    private static final ModConfigSpec.IntValue RITUAL_TIME = BUILDER
            .comment("The time rituals take to complete in ticks. 20 ticks are one second")
            .defineInRange("ritual_time", 120, 0, 20_000);
    private static final ModConfigSpec.IntValue INCENSE_TIME = BUILDER
            .comment("The time incenses burn in ticks. 20 ticks are one second")
            .defineInRange("incense_time", 20_000, 0, 20_000);
    private static final ModConfigSpec.IntValue RUE_INCENSE_RANGE = BUILDER
            .comment("Range of the rue incense, responsible for growing crops")
            .defineInRange("rue_incense_range", 4, 1, 100);
    private static final ModConfigSpec.IntValue LAVENDER_INCENSE_RANGE = BUILDER
            .comment("Range of the lavender incense, responsible for regenerating health")
            .defineInRange("lavender_incense_range", 5, 1, 100);
    private static final ModConfigSpec.IntValue DRYING_TIME = BUILDER
            .comment("The time hanging herbs take to dry. 20 ticks is one second")
            .defineInRange("drying_time", 2_000, 0, 20_000);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static Set<EntityType<?>> entityTypes;
    public static int wwRange;
    public static int ritualTime;
    public static int incenseTime;
    public static int rueIncenseRange;
    public static int lavenderIncenseRange;
    public static int dryingTime;

    private static boolean validateEntityName(final Object obj) {
        return obj instanceof final String itemName && BuiltInRegistries.ENTITY_TYPE.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        entityTypes = ENTITY_TYPES.get().stream()
                .map(itemName -> BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());
        wwRange = WICAN_WARD_RANGE.getAsInt();
        ritualTime = RITUAL_TIME.getAsInt();
        incenseTime = INCENSE_TIME.getAsInt();
        lavenderIncenseRange = LAVENDER_INCENSE_RANGE.getAsInt();
        rueIncenseRange = RUE_INCENSE_RANGE.getAsInt();
        dryingTime = DRYING_TIME.getAsInt();
    }
}
