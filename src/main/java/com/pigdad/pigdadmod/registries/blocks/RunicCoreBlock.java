package com.pigdad.pigdadmod.registries.blocks;

import com.mojang.datafixers.util.Pair;
import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.RuneType;
import com.pigdad.pigdadmod.registries.blockentities.RunicCoreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RunicCoreBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RunicCoreBlock(Properties p_49224_) {
        super(p_49224_);
        registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        return defaultBlockState().setValue(FACING, player.getDirection());
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(0, 0, 0, 16, 4, 16);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(ACTIVE, FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new RunicCoreBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        p_60506_.sendSystemMessage(Component.literal("Type: " + getRuneType(p_60504_, p_60505_)));
        return InteractionResult.SUCCESS;
    }

    // TODO: Add error message for player?
    @Nullable
    public static RuneType getRuneType(Level level, BlockPos corePos) {
        int validIndex = -1;
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

        List<BlockPos> finalPositions = new ArrayList<>();

        if (level.getBlockState(firstPos1).getBlock() instanceof RuneSlabBlock &&
                level.getBlockState(firstPos2).getBlock() instanceof RuneSlabBlock) {
            for (Vec3i offSetPos : otherPositions1) {
                if (!(level.getBlockState(corePos.offset(offSetPos)).getBlock() instanceof RuneSlabBlock)) {
                    return null;
                } else {
                    finalPositions.add(corePos.offset(offSetPos));
                }
            }
            finalPositions.add(firstPos1);
            finalPositions.add(firstPos2);
        } else if (level.getBlockState(secPos1).getBlock() instanceof RuneSlabBlock &&
                level.getBlockState(secPos2).getBlock() instanceof RuneSlabBlock) {
            for (Vec3i offSetPos : otherPositions2) {
                if (!(level.getBlockState(corePos.offset(offSetPos)).getBlock() instanceof RuneSlabBlock)) {
                    return null;
                } else {
                    finalPositions.add(corePos.offset(offSetPos));
                }
            }
            finalPositions.add(secPos1);
            finalPositions.add(secPos2);
        } else {
            return null;
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

        if (!runeStates.equals(expectedStates)) {
            return null;
        }

        RuneType runeType = null;

        for (BlockPos blockPos : finalPositions) {
            BlockState testBlock = level.getBlockState(blockPos);
            if (runeType == null) {
                if (testBlock.getBlock() instanceof RuneSlabBlock runeSlabBlock) runeType = runeSlabBlock.getRuneType();
            }

            if (testBlock.getBlock() instanceof RuneSlabBlock runeSlabBlock) {
                if (runeSlabBlock.getRuneType() != runeType)
                    return null;
            }
        }

        return runeType;
    }
}
