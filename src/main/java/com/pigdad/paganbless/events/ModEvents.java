package com.pigdad.paganbless.events;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.registries.blockentities.renderer.ImbuingCauldronBERenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = PaganBless.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientBus {
        @SubscribeEvent
        public static void registerBERenderer(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(PBBlockEntities.IMBUING_CAULDRON.get(),
                    ImbuingCauldronBERenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = PaganBless.MODID)
    public static class ServerBus {
        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            BlockState block = event.getEntity().level().getBlockState(event.getEntity().getOnPos());
            if (block.is(PBBlocks.RUNIC_CORE.get())) {
                if (event.getEntity().level().getBlockEntity(event.getEntity().getOnPos()) instanceof RunicCoreBlockEntity runicCoreBlockEntity) {
                    runicCoreBlockEntity.craftItem(event.getEntity());
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = PaganBless.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.IMBUING_CAULDRON.get(), (be, ctx) -> be.getItemHandler());
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PBBlockEntities.IMBUING_CAULDRON.get(), (be, ctx) -> be.getFluidTank());
        }
    }
}
