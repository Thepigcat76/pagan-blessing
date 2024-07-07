package com.pigdad.paganbless.utils;

import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import com.pigdad.paganbless.registries.blocks.RopeBlock;
import com.pigdad.paganbless.utils.recipes.AnvilRecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public final class WinchUtils {
    // Will return length of the connection
    public static int recheckConnections(Level level, BlockPos winchPos, @Nullable BlockPos excludedPosition) {
        int length = 0;

        BlockPos blockPos = winchPos.below();
        while ((level.getBlockState(blockPos).getBlock() instanceof RopeBlock && RopeBlock.facingUpOrDown(level.getBlockState(blockPos).getValue(RopeBlock.FACING))) || blockPos.equals(excludedPosition)) {
            BlockState ropeBlock = level.getBlockState(blockPos);
            if (!blockPos.equals(excludedPosition)) {
                level.setBlockAndUpdate(blockPos, ropeBlock.setValue(RopeBlock.HAS_WINCH, true));
            }
            blockPos = blockPos.below();
            length++;
        }
        return length;
    }

    public static void liftDown(WinchBlockEntity winchBlockEntity) {
        BlockPos winchPos = winchBlockEntity.getBlockPos();
        Level level = winchBlockEntity.getLevel();

        int distance = winchBlockEntity.getDistance();

        BlockPos lastRopePos = winchPos.below(distance);
        BlockPos liftedPos = lastRopePos.below();
        BlockPos belowLiftedPos = liftedPos.below();

        BlockState liftedState = level.getBlockState(liftedPos);
        BlockState belowLiftedState = level.getBlockState(belowLiftedPos);

        if (liftedState.hasBlockEntity() || (liftedState.getPistonPushReaction() != PushReaction.NORMAL && !liftedState.isEmpty() && !liftedState.is(Blocks.ANVIL)))
            return;

        ItemStackHandler itemHandler = winchBlockEntity.getItemHandler();
        ItemStack stackInSlot = itemHandler.getStackInSlot(0);
        // TODO: If there is a block two blocks below the winch then it cannot lift down
        if (!stackInSlot.isEmpty()) {
            BlockState newRopeState = Block.byItem(stackInSlot.getItem()).defaultBlockState();
            if (!newRopeState.isEmpty()) {
                 if (belowLiftedState.canBeReplaced()) {
                     if (!(liftedState.getBlock() instanceof RopeBlock)) {
                         level.setBlockAndUpdate(belowLiftedPos, liftedState);
                     }
                    level.setBlockAndUpdate(liftedPos, newRopeState.setValue(RopeBlock.FACING, Direction.DOWN));
                    winchBlockEntity.setDistance(distance + 1);
                    winchBlockEntity.setLiftDown(true);
                    itemHandler.extractItem(0, 1, false);
                } else {
                    winchBlockEntity.setLiftDown(false);
                    if (liftedState.is(Blocks.ANVIL)) {
                        level.playSound(null, belowLiftedPos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 2F, 1F);
                        AnvilRecipeUtils.onAnvilLand(level, belowLiftedPos);
                    }
                }
            }
        } else {
            winchBlockEntity.setLiftDown(false);
        }
    }

    public static void liftUp(WinchBlockEntity winchBlockEntity) {
        BlockPos winchPos = winchBlockEntity.getBlockPos();
        Level level = winchBlockEntity.getLevel();

        int distance = winchBlockEntity.getDistance();

        BlockPos lastRopePos = winchPos.below(distance);
        BlockState lastRopeState = level.getBlockState(lastRopePos);
        BlockPos liftedPos = lastRopePos.below();
        BlockState liftedState = level.getBlockState(liftedPos);

        ItemStack newRopeStack = lastRopeState.getBlock().asItem().getDefaultInstance();

        if (!newRopeStack.isEmpty() && !liftedState.hasBlockEntity() && distance > 1) {
            ItemStack remainder = winchBlockEntity.getItemHandler().insertItem(0, newRopeStack, false);

            if (remainder.isEmpty()) {
                level.removeBlock(liftedPos, true);
                level.setBlockAndUpdate(lastRopePos, liftedState);
                winchBlockEntity.setDistance(distance - 1);
            }
        }
    }
}
