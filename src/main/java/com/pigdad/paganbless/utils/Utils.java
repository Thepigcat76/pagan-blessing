package com.pigdad.paganbless.utils;

import com.pigdad.paganbless.data.RunicCoreSavedData;
import com.pigdad.paganbless.data.WicanWardSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import static net.neoforged.neoforge.items.ItemHandlerHelper.insertItemStacked;

public final class Utils {
    // Get capability of a block entity
    public static <T, C> @Nullable T getCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), null);
    }

    public static WicanWardSavedData getWWData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(WicanWardSavedData.factory(level), WicanWardSavedData.DATA_ID);
    }

    public static RunicCoreSavedData getRCData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(RunicCoreSavedData.factory(level), RunicCoreSavedData.DATA_ID);
    }

    public static void spawnParticles(LevelAccessor pLevel, Vector3f pos, int pCount, double pXzSpread, double pYSpread, boolean pAllowInAir, ParticleOptions pParticle) {
        RandomSource randomsource = pLevel.getRandom();

        for (int i = 0; i < pCount; ++i) {
            double d0 = randomsource.nextGaussian() * 0.02;
            double d1 = randomsource.nextGaussian() * 0.02;
            double d2 = randomsource.nextGaussian() * 0.02;
            double d3 = 0.5 - pXzSpread;
            double d4 = (double) pos.x + d3 + randomsource.nextDouble() * pXzSpread * 2.0;
            double d5 = (double) pos.y + randomsource.nextDouble() * pYSpread;
            double d6 = (double) pos.z + d3 + randomsource.nextDouble() * pXzSpread * 2.0;
            if (pAllowInAir || !pLevel.getBlockState(BlockPos.containing(d4, d5, d6).below()).isAir()) {
                pLevel.addParticle(pParticle, d4, d5, d6, d0, d1, d2);
            }
        }

    }

    public static void giveItemToPlayerNoSound(Player player, ItemStack stack, int preferredSlot) {
        if (!stack.isEmpty()) {
            IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
            Level level = player.level();
            ItemStack remainder = stack;
            if (preferredSlot >= 0 && preferredSlot < inventory.getSlots()) {
                remainder = inventory.insertItem(preferredSlot, stack, false);
            }

            if (!remainder.isEmpty()) {
                remainder = insertItemStacked(inventory, remainder, false);
            }

            if (!remainder.isEmpty() && !level.isClientSide) {
                ItemEntity entityitem = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), remainder);
                entityitem.setPickUpDelay(40);
                entityitem.setDeltaMovement(entityitem.getDeltaMovement().multiply(0.0, 1.0, 0.0));
                level.addFreshEntity(entityitem);
            }

        }
    }
}
