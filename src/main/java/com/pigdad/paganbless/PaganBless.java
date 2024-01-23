package com.pigdad.paganbless;

import com.mojang.logging.LogUtils;
import com.pigdad.paganbless.registries.*;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(PaganBless.MODID)
public class PaganBless {
    public static final String MODID = "paganbless";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PaganBless() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        PBBlocks.BLOCKS.register(modEventBus);
        PBItems.ITEMS.register(modEventBus);
        PBTabs.CREATIVE_MODE_TABS.register(modEventBus);
        PBRecipes.SERIALIZERS.register(modEventBus);
        PBBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        PBPlacerTypes.FOLIAGE_PLACERS.register(modEventBus);
        PBPlacerTypes.TRUNK_PLACERS.register(modEventBus);
        PBEntities.ENTITY_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PBConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PBConfig.entityTypes.forEach((item) -> LOGGER.info("Entity >> {}", item.toString()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                EntityRenderers.register(PBEntities.ETERNAL_SNOWBALL.get(), pContext -> new ThrownItemRenderer<>(pContext, 1, false));
            });
        }
    }
}
