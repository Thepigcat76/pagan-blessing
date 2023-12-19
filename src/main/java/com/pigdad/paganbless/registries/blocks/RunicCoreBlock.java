package com.pigdad.paganbless.registries.blocks;

import com.mojang.datafixers.util.Pair;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.RuneType;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        // TODO: Give more precise feedback
        if ((!player.getItemInHand(p_60507_).is(Items.FLINT_AND_STEEL) && !player.getItemInHand(p_60507_).is(Items.FIRE_CHARGE)) && getRuneType(p_60504_, p_60505_) != null) {
            player.sendSystemMessage(Component.literal("Ritual is valid"));
        } else if ((!player.getItemInHand(p_60507_).is(Items.FLINT_AND_STEEL) && !player.getItemInHand(p_60507_).is(Items.FIRE_CHARGE))) {
            player.sendSystemMessage(Component.literal("Ritual is incomplete"));
        } else if (player.getItemInHand(p_60507_).is(Items.FLINT_AND_STEEL) || player.getItemInHand(p_60507_).is(Items.FIRE_CHARGE)) {
            p_60504_.setBlockAndUpdate(p_60505_, p_60503_.setValue(ACTIVE, true));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState p_220827_, Level p_220828_, BlockPos p_220829_, RandomSource p_220830_) {
        if (p_220827_.getValue(ACTIVE)) {
            p_220828_.addParticle(ParticleTypes.SMALL_FLAME, p_220829_.getX(), p_220829_.getY()+1, p_220829_.getZ(), 1.0, 1.0, 1.0);
        }
    }

    @Nullable
    public static Pair<RuneType, Set<BlockPos>> getRuneType(Level level, BlockPos corePos) {
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

        return Pair.of(runeType, finalPositions);
    }

    public static void resetPillars(Level level, Set<BlockPos> positions) {
        for (BlockPos blockPos : positions) {
            BlockState blockState = level.getBlockState(blockPos);
            RuneSlabBlock.RuneState runeState = blockState.getValue(RuneSlabBlock.RUNE_STATE);
            level.setBlockAndUpdate(blockPos, PBBlocks.RUNE_SLAB_INERT.get().defaultBlockState()
                    .setValue(RuneSlabBlock.RUNE_STATE, runeState)
                    .setValue(RuneSlabBlock.IS_TOP, false));

            level.setBlockAndUpdate(blockPos.above(), PBBlocks.RUNE_SLAB_INERT.get().defaultBlockState()
                    .setValue(RuneSlabBlock.RUNE_STATE, runeState)
                    .setValue(RuneSlabBlock.IS_TOP, true));
        }
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable BlockGetter p_49817_, List<Component> p_49818_, TooltipFlag p_49819_) {
        p_49818_.add(Component.translatable("desc.paganbless.runic_core").withStyle(ChatFormatting.GRAY));
    }
}