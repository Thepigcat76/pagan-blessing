package com.pigdad.paganbless.registries.blocks;

import com.mojang.datafixers.util.Pair;
import com.pigdad.paganbless.data.RunicCoreSavedData;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.blockentities.RuneSlabBlockEntity;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.registries.ForgeRegistries;
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
        if (!p_60504_.isClientSide()) {
            // TODO: Give more precise feedback
            if (!player.getItemInHand(p_60507_).is(PBItems.BLACK_THORN_STAFF.get()) && getRuneType(p_60504_, p_60505_).getSecond() == null) {
                player.sendSystemMessage(Component.literal("Ritual is valid"));
            } else if (!player.getItemInHand(p_60507_).is(PBItems.BLACK_THORN_STAFF.get())) {
                player.sendSystemMessage(Component.literal("Ritual is incomplete"));
                player.sendSystemMessage(Component.literal(getRuneType(p_60504_, p_60505_).getSecond()));
                if (!p_60503_.getValue(ACTIVE)) {
                    player.sendSystemMessage(Component.literal("Runic core is not activated"));
                }
            } else if (player.getItemInHand(p_60507_).is(PBItems.BLACK_THORN_STAFF.get())) {
                p_60504_.setBlockAndUpdate(p_60505_, p_60503_.setValue(ACTIVE, true));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if (pLevel instanceof ServerLevel serverLevel) {
            RunicCoreSavedData savedData = serverLevel.getDataStorage().computeIfAbsent();
            savedData.addBlockPos(pPos);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        if (pLevel instanceof ServerLevel serverLevel) {
            RunicCoreSavedData savedData = Utils.getRCData(serverLevel);
            savedData.removeBlockPos(pPos);
        }
    }

    @Override
    public void animateTick(BlockState p_220827_, Level level, BlockPos pos, RandomSource p_220830_) {
        double d0 = (double)pos.getX() + 0.5f;
        double d1 = (double)pos.getY() + 1.0f;
        double d2 = (double)pos.getZ() + 0.5f;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
    }

    /**
     * @return first pair entry returns block positions of rune slabs, second returns error message or null
     */
    public static @Nullable Pair<@Nullable Set<BlockPos>, @Nullable String> getRuneType(Level level, BlockPos corePos) {
        if (level.isClientSide()) return null;

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
            for (Vec3i offSetPos : otherPositions1) {
                if (!(level.getBlockState(corePos.offset(offSetPos)).getBlock() instanceof RuneSlabBlock)) {
                    return errorFromString(String.format("Block at %d, %d, %d is not a rune slab but a %s",
                            corePos.offset(offSetPos).getX(),
                            corePos.offset(offSetPos).getY(),
                            corePos.offset(offSetPos).getZ(),
                            level.getBlockState(corePos.offset(offSetPos)).getBlock().getName()));
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
                    return errorFromString(String.format("Block at %d, %d, %d is not a rune slab but a %s",
                            corePos.offset(offSetPos).getX(),
                            corePos.offset(offSetPos).getY(),
                            corePos.offset(offSetPos).getZ(),
                            level.getBlockState(corePos.offset(offSetPos)).getBlock().getName()));
                } else {
                    finalPositions.add(corePos.offset(offSetPos));
                }
            }
            finalPositions.add(secPos1);
            finalPositions.add(secPos2);
        } else {
            return errorFromString(String.format("Neither the block at %d, %d, %d nor block at %d, %d, %d are rune slabs",
                    firstPos1.getX(),
                    firstPos1.getY(),
                    firstPos1.getZ(),
                    secPos1.getX(),
                    secPos1.getY(),
                    secPos1.getZ()));
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
            return errorFromString("A rune state is not correct. When performing the runic ritual, every block needs a different state. States can be changed with a pickaxe");
        }

        Block runeType = level.getBlockState(finalPositions.stream().toList().get(0)).getBlock();

        // check if all blocks have the same rune state
        for (BlockPos blockPos : finalPositions) {
            BlockState testBlock = level.getBlockState(blockPos);

            if (!testBlock.getBlock().equals(runeType)) {
                return errorFromString(String.format("The block at %d, %d, %d is not a %s", blockPos.getX(), blockPos.getY(), blockPos.getZ(), runeType.getName()));
            }
        }

        return Pair.of(finalPositions, null);
    }

    private static Pair<@Nullable Set<BlockPos>, String> errorFromString(String errorMessage) {
        return Pair.of(null, errorMessage);
    }

    public static void resetPillars(Level level, Set<BlockPos> positions) {
        for (BlockPos blockPos : positions) {
            BlockState blockState = level.getBlockState(blockPos);
            RuneSlabBlock.RuneState runeState = blockState.getValue(RuneSlabBlock.RUNE_STATE);
            level.setBlockAndUpdate(blockPos, PBBlocks.RUNE_SLAB_INERT.get().defaultBlockState()
                    .setValue(RuneSlabBlock.RUNE_STATE, runeState)
                    .setValue(RuneSlabBlock.IS_TOP, false));

            ((RuneSlabBlockEntity) level.getBlockEntity(blockPos)).setPrevBlock(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).toString());

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
