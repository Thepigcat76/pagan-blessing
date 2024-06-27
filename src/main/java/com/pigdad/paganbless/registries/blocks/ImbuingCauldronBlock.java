package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class ImbuingCauldronBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = com.pigdad.paganbless.utils.BlockStateProperties.ACTIVE;
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(1, 2.5, 1, 15, 7.5, 15),
            Block.box(3, 0, 3, 13, 2, 13),
            Block.box(2, 7.5, 2, 14, 9.5, 14),
            Block.box(2, 1.5, 2, 14, 2.5, 14)
    ).reduce(Shapes::or).get();

    public ImbuingCauldronBlock(Properties p_49224_) {
        super(p_49224_);
        registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ImbuingCauldronBlock::new);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ImbuingCauldronBlockEntity) {
                ((ImbuingCauldronBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(ACTIVE);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, PBBlockEntities.IMBUING_CAULDRON.get(),
                (pLevel, pPos, pState, pBlockEntity) -> {
                    if (pLevel.isClientSide()) pBlockEntity.clientTick();
                    else pBlockEntity.serverTick();
                }
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ImbuingCauldronBlockEntity(p_153215_, p_153216_);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack p_316304_, BlockState p_316362_, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult p_316140_) {
        ImbuingCauldronBlockEntity blockEntity = (ImbuingCauldronBlockEntity) level.getBlockEntity(blockPos);
        IFluidHandlerItem fluidHandlerItem = player.getItemInHand(interactionHand).getCapability(Capabilities.FluidHandler.ITEM);
        IItemHandler itemHandler = Utils.getCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
        IFluidHandler fluidHandler = Utils.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity);
        if (player.isShiftKeyDown()) {
            blockEntity.turn();
            if (blockEntity.getFluidTank().getFluidAmount() >= 800) {
                if (blockEntity.getFluidTank().getFluidInTank(0).is(Fluids.WATER)) {
                    level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.PLAYER_SWIM, SoundSource.PLAYERS, 0.2F, 1.0F);
                }
            } else {
                level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS, 0.2F, 1.0F);
            }
            blockEntity.setProgress(blockEntity.getProgress() + 5);
            return ItemInteractionResult.SUCCESS;
        }

        if (!player.isShiftKeyDown()) {
            return insertAndExtract(player, level, interactionHand, itemHandler, fluidHandler, fluidHandlerItem);
        }
        return ItemInteractionResult.FAIL;
    }

    private static ItemInteractionResult insertAndExtract(Player player, Level level, InteractionHand interactionHand, IItemHandler itemHandler, IFluidHandler fluidHandler, IFluidHandler fluidHandlerItem) {
        if (!player.getItemInHand(interactionHand).isEmpty() && fluidHandlerItem == null) {
            int insertIndex = getFirstForInsert(itemHandler, player.getItemInHand(interactionHand));
            if (insertIndex != -1) {
                itemHandler.insertItem(insertIndex, player.getItemInHand(interactionHand).copy(), false);
                player.getItemInHand(interactionHand).setCount(0);
            }
        } else if (player.getItemInHand(interactionHand).isEmpty()) {
            int extractIndex = getFirstForExtract(itemHandler);
            if (extractIndex != -1) {
                ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(extractIndex).copy());
                itemHandler.extractItem(extractIndex, itemHandler.getStackInSlot(extractIndex).getCount(), false);
            }
        }

        FluidStack fluidInTank = fluidHandler.getFluidInTank(0);

        if (fluidHandlerItem != null && fluidHandlerItem.getFluidInTank(0).getAmount() > 0) {
            int filled = fluidHandler.fill(fluidHandlerItem.getFluidInTank(0).copy(), IFluidHandler.FluidAction.EXECUTE);
            fluidHandlerItem.drain(filled, IFluidHandler.FluidAction.EXECUTE);
            if (player.getItemInHand(interactionHand).getItem() instanceof BucketItem bucketItem) {
                player.getItemInHand(interactionHand).shrink(1);
                Utils.giveItemToPlayerNoSound(player, Items.BUCKET.getDefaultInstance(), -1);
                if (bucketItem.content.isSame(Fluids.WATER)) {
                    level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 0.8F, 1.0F);
                } else if (bucketItem.content.isSame(Fluids.LAVA)) {
                    level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.PLAYERS, 0.8F, 1.0F);
                }
            }
            return ItemInteractionResult.SUCCESS;
        } else {
            if (fluidHandlerItem != null && fluidHandlerItem.getFluidInTank(0).getAmount() == 0 && !fluidInTank.isEmpty()) {
                if (player.getItemInHand(interactionHand).is(Items.BUCKET)) {
                    player.getItemInHand(interactionHand).shrink(1);
                    Utils.giveItemToPlayerNoSound(player, fluidInTank.getFluid().getBucket().getDefaultInstance(), -1);
                    if (fluidInTank.is(Fluids.WATER)) {
                        level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 0.8F, 1.0F);
                    } else if (fluidInTank.is(Fluids.LAVA)) {
                        level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS, 0.8F, 1.0F);
                    }
                    fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                } else {
                    FluidStack fluidStack = fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    fluidHandlerItem.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private static int getFirstForInsert(IItemHandler itemHandler, ItemStack toInsert) {
        for (int i = 0; i < itemHandler.getSlots() - 1; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty() || (itemHandler.getStackInSlot(i).is(toInsert.getItem()) && itemHandler.getStackInSlot(i).getCount() + toInsert.getCount() <= toInsert.getMaxStackSize())) {
                return i;
            }
        }
        return -1;
    }

    private static int getFirstForExtract(IItemHandler itemHandler) {
        for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
}
