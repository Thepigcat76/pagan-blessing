package com.pigdad.paganbless;

import com.mojang.logging.LogUtils;
import com.pigdad.paganbless.registries.*;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.slf4j.Logger;

@Mod(PaganBless.MODID)
public final class PaganBless {
    public static final String MODID = "paganbless";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static MinecraftServer server;

    public PaganBless(IEventBus modEventBus, ModContainer container) {
        PBBlocks.BLOCKS.register(modEventBus);
        PBItems.ITEMS.register(modEventBus);
        PBTabs.CREATIVE_MODE_TABS.register(modEventBus);
        PBRecipes.SERIALIZERS.register(modEventBus);
        PBBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        PBPlacerTypes.FOLIAGE_PLACERS.register(modEventBus);
        PBPlacerTypes.TRUNK_PLACERS.register(modEventBus);
        PBEntities.ENTITY_TYPES.register(modEventBus);
        PBDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        container.registerConfig(ModConfig.Type.COMMON, PBConfig.SPEC);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MODID)
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerStarted(ServerStartedEvent event) {
            server = event.getServer();
        }

        @SubscribeEvent
        public static void onServerStopping(ServerStoppedEvent event) {
            server = null;
        }
    }
}
