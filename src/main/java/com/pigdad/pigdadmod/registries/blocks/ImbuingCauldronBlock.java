package com.pigdad.pigdadmod.registries.blocks;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.ModBlockEntities;
import com.pigdad.pigdadmod.registries.ModBlocks;
import com.pigdad.pigdadmod.registries.blockentities.ImbuingCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
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

        return createTickerHelper(blockEntityType, ModBlockEntities.IMBUING_CAULDRON.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ImbuingCauldronBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!level.isClientSide()) {
            if (!player.getItemInHand(interactionHand).isEmpty()) {
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .ifPresent(iItemHandler -> {
                            for (int i = 0; i < iItemHandler.getSlots(); i++) {
                                if (iItemHandler.getStackInSlot(i).isEmpty() ||
                                        (player.getItemInHand(interactionHand).is(iItemHandler.getStackInSlot(i).getItem()))
                                                && iItemHandler.getStackInSlot(i).getCount() + player.getItemInHand(interactionHand).getCount() < iItemHandler.getSlotLimit(i)) {
                                    iItemHandler.insertItem(i, player.getItemInHand(interactionHand).copy(), false);
                                    player.getItemInHand(interactionHand).shrink(player.getItemInHand(interactionHand).getCount());
                                    break;
                                }
                            }
                        });
            } else {
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .ifPresent(iItemHandler -> {
                            for (int i = 0; i < iItemHandler.getSlots(); i++) {
                                if (!iItemHandler.getStackInSlot(i).isEmpty()) {
                                    ItemHandlerHelper.giveItemToPlayer(player, iItemHandler.getStackInSlot(i).copy());
                                    iItemHandler.extractItem(i, 64, false);
                                    break;
                                }
                            }
                        });
            }
            blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER)
                    .ifPresent(iFluidHandler ->
                            iFluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE));
        }
        return InteractionResult.SUCCESS;
    }
}
