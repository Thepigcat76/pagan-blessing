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
            .defineListAllowEmpty("pentacle_blacklisted", List.of("minecraft:wither", "minecraft:warden", "minecraft:ender_dragon"), () -> "", PBConfig::validateEntityName);
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
    private static final ModConfigSpec.IntValue SKELETON_SKULL_FROM_BOLINE_CHANCE = BUILDER
            .comment("The chance in percent that a skeleton killed with a boline drops a skull")
            .defineInRange("skeleton_skull_from_boline_chance", 15, 0, 100);
    private static final ModConfigSpec.IntValue WAND_PROJECTILE_DAMAGE = BUILDER
            .comment("The amount of damage the wand's projectile deals")
            .defineInRange("wand_projectile_damage", 3, 0, 100);
    private static final ModConfigSpec.IntValue PENTACLE_MINIMUM_DELAY = BUILDER
            .comment("The minimum amount of time it takes for a mob to spawn from the pentacle in ticks (20 ticks = 1 second)")
            .defineInRange("pentacle_minimum_delay", 200, 0, 10_000);
    private static final ModConfigSpec.IntValue PENTACLE_MAXIMUM_DELAY = BUILDER
            .comment("The maximum amount of time it takes for a mob to spawn from the pentacle in ticks (20 ticks = 1 second)")
            .defineInRange("pentacle_maximum_delay", 800, 0, 10_000);
    private static final ModConfigSpec.IntValue PENTACLE_SPAWN_AMOUNT = BUILDER
            .comment("The amount of mobs that are spawned by the pentacle")
            .defineInRange("pentacle_spawn_amount", 4, 0, 64);
    private static final ModConfigSpec.BooleanValue GIVE_BOOK_ON_FIRST_JOIN = BUILDER
            .comment("Whether a pagan's guide book should be given to the player when they first join the world")
            .define("give_book_on_first_join", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static Set<EntityType<?>> entityTypes;
    public static int wwRange;
    public static int ritualTime;
    public static int incenseTime;
    public static int rueIncenseRange;
    public static int lavenderIncenseRange;
    public static int dryingTime;
    public static int skeletonSkullFromBolineChance;
    public static int wandProjectileDamage;
    public static int pentacleMinDelay;
    public static int pentacleMaxDelay;
    public static int pentacleSpawnAmount;
    public static boolean giveBookOnFirstJoin;

    private static boolean validateEntityName(final Object obj) {
        return obj instanceof final String itemName && BuiltInRegistries.ENTITY_TYPE.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        wwRange = WICAN_WARD_RANGE.getAsInt();
        ritualTime = RITUAL_TIME.getAsInt();
        incenseTime = INCENSE_TIME.getAsInt();
        lavenderIncenseRange = LAVENDER_INCENSE_RANGE.getAsInt();
        rueIncenseRange = RUE_INCENSE_RANGE.getAsInt();
        dryingTime = DRYING_TIME.getAsInt();
        skeletonSkullFromBolineChance = SKELETON_SKULL_FROM_BOLINE_CHANCE.getAsInt();
        wandProjectileDamage = WAND_PROJECTILE_DAMAGE.getAsInt();
        pentacleMinDelay = PENTACLE_MINIMUM_DELAY.getAsInt();
        pentacleMaxDelay = PENTACLE_MAXIMUM_DELAY.getAsInt();
        pentacleSpawnAmount = PENTACLE_SPAWN_AMOUNT.getAsInt();
        giveBookOnFirstJoin = GIVE_BOOK_ON_FIRST_JOIN.getAsBoolean();
        entityTypes = ENTITY_TYPES.get().stream()
                .map(itemName -> BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(itemName)))
                .collect(Collectors.toSet());
    }
}
