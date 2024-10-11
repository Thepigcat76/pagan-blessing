package com.pigdad.paganbless.content.blocks;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.data.saved_data.RunicCoreSavedData;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.content.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.utils.RunicCoreUtils;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@SuppressWarnings("deprecation")
public class RunicCoreBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = com.pigdad.paganbless.utils.BlockStateProperties.ACTIVE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RunicCoreBlock(Properties p_49224_) {
        super(p_49224_);
        registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(RunicCoreBlock::new);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getPlayer().getDirection());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
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
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_316140_) {
        if (itemStack.is(PBTags.ItemTags.FIRE_LIGHTER) && !blockState.getValue(ACTIVE)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(ACTIVE, true));
            level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1f, 1f);
            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            return ItemInteractionResult.SUCCESS;
        } else if (itemStack.is(ItemTags.SHOVELS) && blockState.getValue(ACTIVE)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(ACTIVE, false));
            level.playSound(player, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            return ItemInteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (blockEntity instanceof RunicCoreBlockEntity runicCoreBlockEntity) {
            ItemStackHandler stackHandler = runicCoreBlockEntity.getItemHandler();
            if (stackHandler.getStackInSlot(0).isEmpty()) {
                Either<Set<BlockPos>, Component> runePosResult = RunicCoreUtils.tryGetRunePositions(level, blockPos);
                if (!level.isClientSide()) {
                    if (runePosResult.right().isEmpty() && blockState.getValue(ACTIVE)) {
                        player.sendSystemMessage(Component.translatable("ritual_feedback.paganbless.valid"));
                    } else {
                        if (!blockState.getValue(ACTIVE)) {
                            player.sendSystemMessage(Component.translatable("ritual_feedback.paganbless.unlit"));
                        }
                        player.sendSystemMessage(Component.translatable("ritual_feedback.paganbless.incomplete"));
                        player.sendSystemMessage(runePosResult.right().get());
                    }
                }
            } else {
                ItemHandlerHelper.giveItemToPlayer(player, stackHandler.getStackInSlot(0));
                stackHandler.setStackInSlot(0, ItemStack.EMPTY);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, PBBlockEntities.RUNIC_CORE.get(), (level, pos, state, entity) -> {
            if (level.isClientSide())
                entity.clientTick();
            entity.commonTick();
        });
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if (pLevel instanceof ServerLevel serverLevel) {
            RunicCoreSavedData savedData = Utils.getRCData(serverLevel);
            savedData.addBlockPos(pPos);
        }
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        if (pLevel instanceof ServerLevel serverLevel) {
            RunicCoreSavedData savedData = Utils.getRCData(serverLevel);
            savedData.removeBlockPos(pPos);
        }
    }
}
