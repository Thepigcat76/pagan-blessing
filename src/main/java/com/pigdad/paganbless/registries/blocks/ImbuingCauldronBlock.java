package com.pigdad.paganbless.registries.blocks;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.ImbuingCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;
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
                for(int i = 0; i < randomSource.nextInt(2) + 2; ++i) {
                    CampfireBlock.makeParticles(level, blockPos, false, false);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        LazyOptional<IFluidHandlerItem> fluidHandlerItem = player.getItemInHand(interactionHand).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        if (!level.isClientSide()) {
            // items except for fluid containers and air
            if (!player.getItemInHand(interactionHand).isEmpty() && !fluidHandlerItem.isPresent()) {
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .ifPresent(iItemHandler -> {
                            for (int i = 0; i < iItemHandler.getSlots() - 1; i++) {
                                if (iItemHandler.getStackInSlot(i).isEmpty() ||
                                        (player.getItemInHand(interactionHand).is(iItemHandler.getStackInSlot(i).getItem()))
                                                && iItemHandler.getStackInSlot(i).getCount() + player.getItemInHand(interactionHand).getCount() < iItemHandler.getSlotLimit(i)) {
                                    iItemHandler.insertItem(i, player.getItemInHand(interactionHand).copy(), false);
                                    player.getItemInHand(interactionHand).shrink(player.getItemInHand(interactionHand).getCount());
                                    break;
                                }
                            }
                        });
            } else if (!player.getItemInHand(interactionHand).isEmpty() && fluidHandlerItem.isPresent()) {
                IFluidHandlerItem fluidItem = fluidHandlerItem.orElseThrow(NullPointerException::new);
                if (fluidItem.getFluidInTank(0).getAmount() <= 0) {
                    blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                            .ifPresent(iItemHandler -> {
                                for (int i = 0; i < iItemHandler.getSlots() - 1; i++) {
                                    if (iItemHandler.getStackInSlot(i).isEmpty() ||
                                            (player.getItemInHand(interactionHand).is(iItemHandler.getStackInSlot(i).getItem()))
                                                    && iItemHandler.getStackInSlot(i).getCount() + player.getItemInHand(interactionHand).getCount() < iItemHandler.getSlotLimit(i)) {
                                        iItemHandler.insertItem(i, player.getItemInHand(interactionHand).copy(), false);
                                        player.getItemInHand(interactionHand).shrink(player.getItemInHand(interactionHand).getCount());
                                        break;
                                    }
                                }
                            });
                }
                // no item in hand
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
            if (fluidHandlerItem.isPresent()) {
                IFluidHandlerItem fluidItem = fluidHandlerItem.orElseThrow(NullPointerException::new);
                if (fluidItem.getFluidInTank(0).getAmount() > 0) {
                    blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER)
                            .ifPresent(iFluidHandler -> {
                                if (iFluidHandler.getFluidInTank(0).getAmount() + fluidItem.getFluidInTank(0).getAmount() <= iFluidHandler.getTankCapacity(0)) {
                                    if (iFluidHandler.getFluidInTank(0).isEmpty()
                                            || iFluidHandler.getFluidInTank(0).getFluid().isSame(fluidItem.getFluidInTank(0).getFluid())) {
                                        iFluidHandler.fill(fluidItem.getFluidInTank(0).copy(), IFluidHandler.FluidAction.EXECUTE);
                                        fluidItem.drain(fluidItem.getFluidInTank(0).copy().getAmount(), IFluidHandler.FluidAction.EXECUTE);
                                        player.getInventory().removeItem(player.getItemInHand(interactionHand));
                                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.BUCKET));
                                    }
                                }
                            });
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
