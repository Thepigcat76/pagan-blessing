package com.pigdad.paganbless.events;

import com.klikli_dev.modonomicon.data.BookDataManager;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.data.LocationsSavedData;
import com.pigdad.paganbless.data.RunicCoreSavedData;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.registries.blockentities.renderer.ImbuingCauldronBERenderer;
import com.pigdad.paganbless.registries.items.PaganGuideItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

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
            Level level = event.getEntity().level();
            if (level instanceof ServerLevel level1) {
                RunicCoreSavedData savedData = getSavedData(level1, RunicCoreSavedData.factory(level1));
                BlockPos rcPos = savedData.sacrificeInRange(event.getEntity().getOnPos());
                if (rcPos != null && level.getBlockEntity(rcPos) instanceof RunicCoreBlockEntity runicCoreBlockEntity) {
                    runicCoreBlockEntity.craftItem(event.getEntity());
                }
            }
        }

        private static @NotNull RunicCoreSavedData getSavedData(ServerLevel serverLevel, LocationsSavedData.Factory<RunicCoreSavedData> factory) {
            return serverLevel.getDataStorage().computeIfAbsent(factory.deserializer(), factory.constructor(), RunicCoreSavedData.DATA_ID);
        }

        @SubscribeEvent
        public static void onCrafted(PlayerEvent.ItemCraftedEvent event) {
            ItemStack crafting = event.getCrafting();
            if (crafting.getItem() instanceof PaganGuideItem) {
                ResourceLocation id = BookDataManager.get().getBook(new ResourceLocation("paganbless:pagan_guide")).getId();
                crafting.getOrCreateTag().putString("modonomicon:book_id", id.toString());
            }
        }
    }
}
