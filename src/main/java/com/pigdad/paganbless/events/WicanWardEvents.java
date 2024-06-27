/**
 * Mostly taken from the torchmasters mod, licensed under MIT.
 * Thank you to Xalcon and the mod's contributors
 */

package com.pigdad.paganbless.events;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.data.saved_data.WicanWardSavedData;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = PaganBless.MODID)
public class WicanWardEvents {
    private static boolean isIntentionalSpawn(MobSpawnType spawnType) {
        return switch (spawnType) {
            case BREEDING, DISPENSER, BUCKET, CONVERSION, SPAWN_EGG, TRIGGERED, COMMAND, EVENT -> true;
            default -> false;
        };
    }

    private static boolean isNaturalSpawn(MobSpawnType spawnType) {
        return switch (spawnType) { // Patrol can be considered natural
            case NATURAL, CHUNK_GENERATION, PATROL, TRIAL_SPAWNER -> true;
            case BREEDING, CONVERSION, BUCKET, DISPENSER, SPAWNER, STRUCTURE, MOB_SUMMONED, JOCKEY, REINFORCEMENT,
                 TRIGGERED, SPAWN_EGG, COMMAND, EVENT -> false;
        };
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        // Check if the spawn was intentional (i.e. player invoked), we don't block those
        if (isIntentionalSpawn(event.getSpawnType()))
            return;

        if (!isNaturalSpawn(event.getSpawnType()))
            return;

        Mob entity = event.getEntity();
        Level level = entity.getCommandSenderWorld();

        if (level instanceof ServerLevel serverLevel) {
            BlockPos pos = new BlockPos((int) event.getX(), (int) event.getY(), (int) event.getZ());

            WicanWardSavedData data = Utils.getWWData(serverLevel);
            if (data.shouldPreventEntitySpawn(pos)) {
                event.setSpawnCancelled(true);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onGlobalTick(ServerTickEvent.Post event) {
        if (PaganBless.server == null) return;

        for (ServerLevel level : PaganBless.server.getAllLevels()) {
            level.getProfiler().push("paganbless_wicanward_" + level.dimension().registry());
            WicanWardSavedData data = Utils.getWWData(level);
            data.onGlobalTick(level);
            level.getProfiler().pop();
        }

    }
}
