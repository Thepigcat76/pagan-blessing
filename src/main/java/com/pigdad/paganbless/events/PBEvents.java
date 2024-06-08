package com.pigdad.paganbless.events;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.data.RunicCoreSavedData;
import com.pigdad.paganbless.networking.PayloadActions;
import com.pigdad.paganbless.networking.RunicCoreExplodePayload;
import com.pigdad.paganbless.networking.RunicCoreRecipePayload;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.registries.blockentities.renderer.HerbalistBenchBERenderer;
import com.pigdad.paganbless.registries.blockentities.renderer.ImbuingCauldronBERenderer;
import com.pigdad.paganbless.registries.blockentities.renderer.JarBERenderer;
import com.pigdad.paganbless.registries.blockentities.renderer.RunicCoreBERenderer;
import com.pigdad.paganbless.registries.blocks.CrankBlock;
import com.pigdad.paganbless.registries.blocks.RunicCoreBlock;
import com.pigdad.paganbless.registries.blocks.WinchBlock;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PBEvents {
    @EventBusSubscriber(modid = PaganBless.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientBus {
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
        public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            BlockState blockState = level.getBlockState(pos);
            Player player = event.getEntity();
            if (blockState.getBlock() instanceof CrankBlock && player.isShiftKeyDown()) {
                BlockPos winchPos = CrankBlock.getWinchPos(blockState, pos);
                BlockState winchBlock = level.getBlockState(winchPos);
                level.setBlockAndUpdate(pos, CrankBlock.decrRotationState(blockState));
                level.setBlockAndUpdate(winchPos, winchBlock.setValue(WinchBlock.LIFT_DOWN, true));
                player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }

    @EventBusSubscriber(modid = PaganBless.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.IMBUING_CAULDRON.get(), (be, ctx) -> be.getItemHandler().get());
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.JAR.get(), (be, ctx) -> be.getItemHandler().get());
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PBBlockEntities.RUNIC_CORE.get(), (be, ctx) -> be.getItemHandler().get());
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PBBlockEntities.IMBUING_CAULDRON.get(), (be, ctx) -> be.getFluidTank().get());
        }

        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar(PaganBless.MODID);
            registrar.playToClient(RunicCoreRecipePayload.TYPE, RunicCoreRecipePayload.STREAM_CODEC, PayloadActions::runicCoreRecipeSync);
            registrar.playToClient(RunicCoreExplodePayload.TYPE, RunicCoreExplodePayload.STREAM_CODEC, PayloadActions::runicCoreExplodeSync);
        }
    }
}
