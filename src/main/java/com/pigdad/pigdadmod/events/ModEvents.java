package com.pigdad.pigdadmod.events;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.ModBlockEntities;
import com.pigdad.pigdadmod.registries.blockentities.renderer.ImbuingCauldronBERenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = PigDadMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientBus {
        @SubscribeEvent
        public static void registerBERenderer(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.IMBUING_CAULDRON.get(),
                    ImbuingCauldronBERenderer::new);
        }
    }
}
