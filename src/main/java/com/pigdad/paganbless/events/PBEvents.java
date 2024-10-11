package com.pigdad.paganbless.events;

import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.api.blocks.ContainerBlockEntity;
import com.pigdad.paganbless.api.blocks.TranslucentHighlightFix;
import com.pigdad.paganbless.api.capabilities.InfiniteFluidHandler;
import com.pigdad.paganbless.compat.modonomicon.ModonomiconCompat;
import com.pigdad.paganbless.content.blockentities.renderer.*;
import com.pigdad.paganbless.data.PBAttachmentTypes;
import com.pigdad.paganbless.data.saved_data.RunicCoreSavedData;
import com.pigdad.paganbless.mixins.LevelRendererAccess;
import com.pigdad.paganbless.networking.*;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBEntities;
import com.pigdad.paganbless.registries.PBMenuTypes;
import com.pigdad.paganbless.content.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.content.blocks.JarBlock;
import com.pigdad.paganbless.content.items.JarItem;
import com.pigdad.paganbless.content.items.renderer.JarItemRenderer;
import com.pigdad.paganbless.content.screens.WinchScreen;
import com.pigdad.paganbless.utils.PBRenderTypes;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PBEvents {
    @EventBusSubscriber(modid = PaganBless.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModBus {
        public static final JarItemRenderer RENDERER = new JarItemRenderer();

        @SubscribeEvent
        public static void registerBERenderer(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(PBBlockEntities.IMBUING_CAULDRON.get(),
                    ImbuingCauldronBERenderer::new);
            event.registerBlockEntityRenderer(PBBlockEntities.JAR.get(),
                    JarBERenderer::new);
            event.registerBlockEntityRenderer(PBBlockEntities.HERBALIST_BENCH.get(),
                    HerbalistBenchBERenderer::new);
            event.registerBlockEntityRenderer(PBBlockEntities.RUNIC_CORE.get(),
                    RunicCoreBERenderer::new);
            event.registerBlockEntityRenderer(PBBlockEntities.CRANK.get(),
                    CrankBERenderer::new);
            event.registerBlockEntityRenderer(PBBlockEntities.INCENSE.get(),
                    IncenseBERenderer::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> EntityRenderers.register(PBEntities.ETERNAL_SNOWBALL.get(), pContext -> new ThrownItemRenderer<>(pContext, 1, false)));
            event.enqueueWork(() -> EntityRenderers.register(PBEntities.WAND_PROJECTILE.get(), pContext -> new ThrownItemRenderer<>(pContext, 1, false)));
        }

        @SubscribeEvent
        public static void onClientSetup(RegisterClientExtensionsEvent event) {
            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return RENDERER;
                }
            }, getInheritedBlocks(JarItem.class));
        }

        private static Item[] getInheritedBlocks(Class<? extends Item> itemClass) {
            List<Item> items = BuiltInRegistries.ITEM.stream().filter(itemClass::isInstance).toList();
            Item[] itemArray = new Item[items.size()];
            return items.toArray(itemArray);
        }

        @SubscribeEvent
        public static void onClientSetup(RegisterMenuScreensEvent event) {
            event.register(PBMenuTypes.WINCH_MENU.get(), WinchScreen::new);
        }
    }

    @EventBusSubscriber(modid = PaganBless.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    public static class ClientGameBus {

        // From Immersive Engineering. Thank you blu, for figuring out this fix <3
        @SubscribeEvent
        public static void renderOutline(RenderHighlightEvent.Block event) {
            if (event.getCamera().getEntity() instanceof LivingEntity living) {
                Level world = living.level();
                BlockHitResult rtr = event.getTarget();
                BlockPos pos = rtr.getBlockPos();
                Vec3 renderView = event.getCamera().getPosition();

                BlockState targetBlock = world.getBlockState(rtr.getBlockPos());
                if (targetBlock.getBlock() instanceof TranslucentHighlightFix) {
                    if (!(targetBlock.getBlock() instanceof JarBlock)) {
                        ((LevelRendererAccess) event.getLevelRenderer()).callRenderHitOutline(
                                event.getPoseStack(), event.getMultiBufferSource().getBuffer(PBRenderTypes.LINES_NONTRANSLUCENT),
                                living, renderView.x, renderView.y, renderView.z,
                                pos, targetBlock
                        );
                    }
                    event.setCanceled(true);
                }
            }
        }

    }

    @EventBusSubscriber(modid = PaganBless.MODID)
    public static class CommonBus {
        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            Level level = event.getEntity().level();
            if (level instanceof ServerLevel level1) {
                RunicCoreSavedData savedData = Utils.getRCData(level1);
                BlockPos rcPos = savedData.sacrificeInRange(event.getEntity().getOnPos());
                if (rcPos != null && level.getBlockEntity(rcPos) instanceof RunicCoreBlockEntity runicCoreBlockEntity) {
                    runicCoreBlockEntity.craftItem(event.getEntity());
                }
            }
        }
        
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (ModList.get().isLoaded("modonomicon")) {
                Player player = event.getEntity();
                if (PBConfig.giveBookOnFirstJoin && !player.getData(PBAttachmentTypes.HAS_PAGAN_GUIDE.get())) {
                    ItemHandlerHelper.giveItemToPlayer(player, ModonomiconCompat.getItemStack());
                    player.setData(PBAttachmentTypes.HAS_PAGAN_GUIDE.get(), true);
                }
            }
        }
    }

    @EventBusSubscriber(modid = PaganBless.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.IMBUING_CAULDRON.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.JAR.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.RUNIC_CORE.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.WINCH.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.HERBALIST_BENCH.get(), ContainerBlockEntity::getItemHandlerOnSide);

            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PBBlockEntities.IMBUING_CAULDRON.get(), (be, ctx) -> be.getFluidTank());

            event.registerBlock(Capabilities.FluidHandler.BLOCK,
                    ((level, blockPos, blockState, blockEntity, direction) -> new InfiniteFluidHandler(NeoForgeMod.MILK.get())), PBBlocks.CHALICE.get());
        }

        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar(PaganBless.MODID);
            registrar.playToClient(RunicCoreRecipePayload.TYPE, RunicCoreRecipePayload.STREAM_CODEC, PayloadActions::runicCoreRecipeSync);
            registrar.playToClient(RunicCoreExplodePayload.TYPE, RunicCoreExplodePayload.STREAM_CODEC, PayloadActions::runicCoreExplodeSync);
            registrar.playToClient(IncenseBurningPayload.TYPE, IncenseBurningPayload.STREAM_CODEC, PayloadActions::incenseBurningSync);
            registrar.playToClient(WinchPayload.TYPE, WinchPayload.STREAM_CODEC, PayloadActions::winchSync);
        }
    }
}
