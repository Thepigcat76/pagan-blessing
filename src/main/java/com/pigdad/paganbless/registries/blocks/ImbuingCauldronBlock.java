package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class ImbuingCauldronBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(1, 2.5, 1, 15, 7.5, 15),
            Block.box(3, 0, 3, 13, 2, 13),
            Block.box(2, 7.5, 2, 14, 9.5, 14),
            Block.box(2, 1.5, 2, 14, 2.5, 14)
    ).reduce(Shapes::or).get();

    public ImbuingCauldronBlock(Properties p_49224_) {
        super(p_49224_);
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
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, PBBlockEntities.IMBUING_CAULDRON.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ImbuingCauldronBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        ImbuingCauldronBlockEntity blockEntity = (ImbuingCauldronBlockEntity) level.getBlockEntity(blockPos);

        if (blockEntity.isActive()) {
            if (randomSource.nextFloat() < 0.11F) {
                for (int i = 0; i < randomSource.nextInt(2) + 2; ++i) {
                    CampfireBlock.makeParticles(level, blockPos, false, false);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IFluidHandlerItem fluidHandlerItem = player.getItemInHand(interactionHand).getCapability(Capabilities.FluidHandler.ITEM);
        IItemHandler itemHandler = Utils.getCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
        IFluidHandler fluidHandler = Utils.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity);
        if (!level.isClientSide()) {
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

            if (fluidHandlerItem != null && fluidHandlerItem.getFluidInTank(0).getAmount() > 0) {
                int filled = fluidHandler.fill(fluidHandlerItem.getFluidInTank(0).copy(), IFluidHandler.FluidAction.EXECUTE);
                fluidHandlerItem.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                if (player.getItemInHand(interactionHand).getItem() instanceof BucketItem) {
                    player.getItemInHand(interactionHand).setCount(0);
                    ItemHandlerHelper.giveItemToPlayer(player, Items.BUCKET.getDefaultInstance());
                }
            } else if (fluidHandlerItem != null && fluidHandlerItem.getFluidInTank(0).getAmount() == 0) {
                if (player.getItemInHand(interactionHand).is(Items.BUCKET)) {
                    player.getItemInHand(interactionHand).shrink(1);
                    ItemHandlerHelper.giveItemToPlayer(player, fluidHandler.getFluidInTank(0).getFluid().getBucket().getDefaultInstance());
                    fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                } else {
                    FluidStack fluidStack = fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    fluidHandlerItem.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
        return InteractionResult.SUCCESS;
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
        for (int i = itemHandler.getSlots()-1; i >= 0 ; i--) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
}
