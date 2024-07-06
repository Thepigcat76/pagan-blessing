package com.pigdad.paganbless.utils;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import com.pigdad.paganbless.registries.blocks.RopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WinchUtils {
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

        ItemStack item = winchBlockEntity.getItemHandler().extractItem(0, 1, false);

        int distance = winchBlockEntity.getDistance();

        BlockPos lastRopePos = winchPos.below(distance);
        BlockPos liftedPos = lastRopePos.below();
        BlockPos belowLiftedPos = liftedPos.below();

        PaganBless.LOGGER.debug("Last rope pos: {}", lastRopePos);
        BlockState state = level.getBlockState(liftedPos);
        BlockState belowLiftedState = level.getBlockState(belowLiftedPos);
        PaganBless.LOGGER.debug("Below lifted: {}", belowLiftedState);

        if (state.hasBlockEntity()) return;

        BlockState state1 = Block.byItem(item.getItem()).defaultBlockState();
        if (!state1.isEmpty() && belowLiftedState.canBeReplaced()) {
            level.setBlockAndUpdate(belowLiftedPos, state);
            level.setBlockAndUpdate(liftedPos, state1.setValue(RopeBlock.FACING, Direction.DOWN));
            winchBlockEntity.setDistance(distance + 1);
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
