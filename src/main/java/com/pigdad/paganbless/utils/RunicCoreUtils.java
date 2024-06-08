package com.pigdad.paganbless.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.RuneSlabBlockEntity;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public final class RunicCoreUtils {
    /**
     * @return first pair entry returns block positions of rune slabs, second returns error message or null
     */
    public static @Nullable Either<Set<BlockPos>, String> getRuneType(Level level, BlockPos corePos) {
        // ritual shape
        //   x
        // y   y
        //
        // y   y
        //   x

        // these are relative positions (x from the diagram)
        List<Pair<Vec3i, Vec3i>> possibleFirstPositions = List.of(
                Pair.of(new Vec3i(0, 0, 2),
                        new Vec3i(0, 0, -2)),
                Pair.of(new Vec3i(2, 0, 0),
                        new Vec3i(-2, 0, 0))
        );

        // y from the diagram assuming the possible first position is of index 0
        List<Vec3i> otherPositions1 = List.of(
                new Vec3i(2, 0, 1),
                new Vec3i(2, 0, -1),
                new Vec3i(-2, 0, 1),
                new Vec3i(-2, 0, -1)
        );

        // y from the diagram assuming the possible first position is of index 1
        List<Vec3i> otherPositions2 = List.of(
                new Vec3i(1, 0, 2),
                new Vec3i(-1, 0, 2),
                new Vec3i(1, 0, -2),
                new Vec3i(-1, 0, -2)
        );

        BlockPos firstPos1 = corePos.offset(possibleFirstPositions.get(0).getFirst());
        BlockPos firstPos2 = corePos.offset(possibleFirstPositions.get(0).getSecond());
        BlockPos secPos1 = corePos.offset(possibleFirstPositions.get(1).getFirst());
        BlockPos secPos2 = corePos.offset(possibleFirstPositions.get(1).getSecond());

        Set<BlockPos> finalPositions = new HashSet<>();

        // check if all the blocks are rune slabs and determine which layout to use
        if (level.getBlockState(firstPos1).getBlock() instanceof RuneSlabBlock &&
                level.getBlockState(firstPos2).getBlock() instanceof RuneSlabBlock) {
            Either<Set<BlockPos>, String> offSetPos = getSetStringPair(level, corePos, otherPositions1, firstPos1, firstPos2, finalPositions);
            if (offSetPos != null) return offSetPos;
        } else if (level.getBlockState(secPos1).getBlock() instanceof RuneSlabBlock &&
                level.getBlockState(secPos2).getBlock() instanceof RuneSlabBlock) {
            Either<Set<BlockPos>, String> offSetPos = getSetStringPair(level, corePos, otherPositions2, secPos1, secPos2, finalPositions);
            if (offSetPos != null) return offSetPos;
        } else {
            return errorFromString(String.format("Neither the block at %d, %d, %d nor block at %d, %d, %d are rune slabs",
                    corePos.offset(firstPos1).getX(),
                    corePos.offset(firstPos1).getY(),
                    corePos.offset(firstPos1).getZ(),
                    corePos.offset(secPos1).getX(),
                    corePos.offset(secPos1).getY(),
                    corePos.offset(secPos1).getZ())
            );
        }

        List<RuneSlabBlock.RuneState> expectedStates = Stream.of(
                RuneSlabBlock.RuneState.VARIANT0,
                RuneSlabBlock.RuneState.VARIANT1,
                RuneSlabBlock.RuneState.VARIANT2,
                RuneSlabBlock.RuneState.VARIANT3,
                RuneSlabBlock.RuneState.VARIANT4,
                RuneSlabBlock.RuneState.VARIANT5
        ).sorted().toList();

        List<RuneSlabBlock.RuneState> runeStates = new ArrayList<>();

        for (BlockPos blockPos : finalPositions) {
            runeStates.add(level.getBlockState(blockPos).getValue(RuneSlabBlock.RUNE_STATE));
        }

        runeStates = runeStates.stream().sorted().toList();

        // check if all blockstates are correct
        if (!runeStates.equals(expectedStates)) {
            return errorFromString("A rune state is not correct. When performing the runic ritual, every block needs a different state. States can be changed with a blackthorn staff");
        }

        Block runeType = level.getBlockState(finalPositions.stream().toList().get(0)).getBlock();

        // check if all blocks have the same rune state
        for (BlockPos blockPos : finalPositions) {
            BlockState testBlock = level.getBlockState(blockPos);

            if (testBlock.is(PBBlocks.RUNE_SLAB_INERT.get())) {
                return errorFromString(String.format("The rune slab at %d, %d, %d is inert", blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            }

            if (!testBlock.is(runeType)) {
                return errorFromString(String.format("The block at %d, %d, %d is not a %s", blockPos.getX(), blockPos.getY(), blockPos.getZ(), runeType.getName()));
            }
        }

        return Either.left(finalPositions);
    }

    private static @Nullable Either<Set<BlockPos>, String> getSetStringPair(Level level, BlockPos corePos, List<Vec3i> otherPositions1, BlockPos firstPos1, BlockPos firstPos2, Set<BlockPos> finalPositions) {
        for (Vec3i offSetPos : otherPositions1) {
            if (!(level.getBlockState(corePos.offset(offSetPos)).getBlock() instanceof RuneSlabBlock)) {
                return Either.right(String.format("Block at %d, %d, %d is not a rune slab but a %s",
                        corePos.offset(offSetPos).getX(),
                        corePos.offset(offSetPos).getY(),
                        corePos.offset(offSetPos).getZ(),
                        level.getBlockState(corePos.offset(offSetPos)).getBlock().getName())
                );
            } else {
                finalPositions.add(corePos.offset(offSetPos));
            }
        }
        finalPositions.add(firstPos1);
        finalPositions.add(firstPos2);
        return null;
    }

    private static Either<@Nullable Set<BlockPos>, String> errorFromString(String errorMessage) {
        return Either.right(errorMessage);
    }

    public static void resetPillars(Level level, Set<BlockPos> positions) {
        for (BlockPos blockPos : positions) {
            BlockState blockState = level.getBlockState(blockPos);
            RuneSlabBlock.RuneState runeState = blockState.getValue(RuneSlabBlock.RUNE_STATE);
            level.setBlockAndUpdate(blockPos, PBBlocks.RUNE_SLAB_INERT.get().defaultBlockState()
                    .setValue(RuneSlabBlock.RUNE_STATE, runeState)
                    .setValue(RuneSlabBlock.IS_TOP, false));

            ((RuneSlabBlockEntity) level.getBlockEntity(blockPos)).setPrevBlock(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).toString());

            level.setBlockAndUpdate(blockPos.above(), PBBlocks.RUNE_SLAB_INERT.get().defaultBlockState()
                    .setValue(RuneSlabBlock.RUNE_STATE, runeState)
                    .setValue(RuneSlabBlock.IS_TOP, true));
        }
    }
}
