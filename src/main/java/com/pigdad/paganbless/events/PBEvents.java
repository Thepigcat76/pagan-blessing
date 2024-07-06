package com.pigdad.paganbless.events;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.api.blocks.ContainerBlockEntity;
import com.pigdad.paganbless.api.blocks.TranslucentHighlightFix;
import com.pigdad.paganbless.compat.modonomicon.ModonomiconCompat;
import com.pigdad.paganbless.data.PBAttachmentTypes;
import com.pigdad.paganbless.data.saved_data.RunicCoreSavedData;
import com.pigdad.paganbless.mixins.LevelRendererAccess;
import com.pigdad.paganbless.networking.IncenseBurningPayload;
import com.pigdad.paganbless.networking.PayloadActions;
import com.pigdad.paganbless.networking.RunicCoreExplodePayload;
import com.pigdad.paganbless.networking.RunicCoreRecipePayload;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBEntities;
import com.pigdad.paganbless.registries.PBMenuTypes;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import com.pigdad.paganbless.registries.blockentities.renderer.*;
import com.pigdad.paganbless.registries.blocks.CrankBlock;
import com.pigdad.paganbless.registries.blocks.JarBlock;
import com.pigdad.paganbless.registries.blocks.WinchBlock;
import com.pigdad.paganbless.registries.screens.WinchScreen;
import com.pigdad.paganbless.utils.PBRenderTypes;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PBEvents {
    @EventBusSubscriber(modid = PaganBless.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModBus {
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
                PaganBless.LOGGER.debug("Data: {}", savedData);
                BlockPos rcPos = savedData.sacrificeInRange(event.getEntity().getOnPos());
                if (rcPos != null && level.getBlockEntity(rcPos) instanceof RunicCoreBlockEntity runicCoreBlockEntity) {
                    runicCoreBlockEntity.craftItem(event.getEntity());
                    PaganBless.LOGGER.debug("Pos: {}", rcPos);
                }
            }
        }
        
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (ModList.get().isLoaded("modonomicon")) {
                Player player = event.getEntity();
                if (!player.getData(PBAttachmentTypes.HAS_PAGAN_GUIDE.get())) {
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
        }

        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar(PaganBless.MODID);
            registrar.playToClient(RunicCoreRecipePayload.TYPE, RunicCoreRecipePayload.STREAM_CODEC, PayloadActions::runicCoreRecipeSync);
            registrar.playToClient(RunicCoreExplodePayload.TYPE, RunicCoreExplodePayload.STREAM_CODEC, PayloadActions::runicCoreExplodeSync);
            registrar.playToClient(IncenseBurningPayload.TYPE, IncenseBurningPayload.STREAM_CODEC, PayloadActions::incenseBurningSync);
        }
    }
}
