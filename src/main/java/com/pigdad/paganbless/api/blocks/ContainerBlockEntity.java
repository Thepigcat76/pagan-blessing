package com.pigdad.paganbless.api.blocks;

import com.mojang.datafixers.util.Pair;
import com.pigdad.paganbless.api.io.IOActions;
import com.pigdad.paganbless.api.io.SidedItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class ContainerBlockEntity extends BlockEntity {
    private ItemStackHandler itemHandler;
    private FluidTank fluidTank;

    public ContainerBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(getItemHandlerStacks());
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ItemStack[] getItemHandlerStacks() {
        ItemStack[] itemStacks = new ItemStack[itemHandler.getSlots()];
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemStacks[i] = itemHandler.getStackInSlot(i);
        }
        return itemStacks;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    protected final void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (getFluidTank() != null) getFluidTank().writeToNBT(provider, tag);
        if (getItemHandler() != null) tag.put("itemhandler", getItemHandler().serializeNBT(provider));
        saveData(tag);
    }

    @Override
    public final void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (getFluidTank() != null) getFluidTank().readFromNBT(provider, tag);
        if (getItemHandler() != null) getItemHandler().deserializeNBT(provider, tag.getCompound("itemhandler"));
        loadData(tag);
    }

    protected void loadData(CompoundTag tag) {
    }

    protected void saveData(CompoundTag tag) {
    }

    protected final void addItemHandler(int slots) {
        addItemHandler(slots, (slot, itemStack) -> true);
    }

    protected final void addFluidTank(int capacityInMb) {
        addFluidTank(capacityInMb, fluidStack -> true);
    }

    /**
     * Get the input/output config for the blockenitity.
     * If directions are not defined in the map, they are assumed to be {@link  IOActions#NONE} and do not affect any slot.
     *
     * @return Map of directions that each map to a pair that defines the IOAction as well as the slots that are affected
     */
    public abstract Map<Direction, Pair<IOActions, int[]>> getItemIO();

    /**
     * Get the input/output config for the blockenitity.
     * If directions are not defined in the map, they are assumed to be {@link  IOActions#NONE} and do not affect any slot.
     *
     * @return Map of directions that each map to a pair that defines the IOAction as well as the tanks that are affected
     */
    public abstract Map<Direction, Pair<IOActions, int[]>> getFluidIO();

    public IItemHandlerModifiable getItemHandlerOnSide(Direction direction) {
        if (direction == null) {
            return itemHandler;
        }

        Map<Direction, Pair<IOActions, int[]>> ioPorts = getItemIO();
        if (ioPorts.containsKey(direction)) {

            if (direction == Direction.UP || direction == Direction.DOWN) {
                return new SidedItemHandler(itemHandler, ioPorts.get(direction));
            }

            if (!this.getBlockState().hasProperty(RotatableEntityBlock.FACING)) return null;

            Direction localDir = this.getBlockState().getValue(RotatableEntityBlock.FACING);

            return switch (localDir) {
                case NORTH -> new SidedItemHandler(itemHandler, ioPorts.get(direction.getOpposite()));
                case EAST -> new SidedItemHandler(itemHandler, ioPorts.get(direction.getClockWise()));
                case SOUTH -> new SidedItemHandler(itemHandler, ioPorts.get(direction));
                case WEST -> new SidedItemHandler(itemHandler, ioPorts.get(direction.getCounterClockWise()));
                default -> null;
            };
        }

        return null;
    }

//    public SidedItemHandler getFluidTankOnSide(Direction direction) {
//        if (side == null) {
//            return lazyFluidHandler.cast();
//        }
//
//        Map<Direction, Pair<IOAction, List<Integer>>> ioPorts = getFluidIO();
//        if (ioPorts.containsKey(side)) {
//            Direction localDir = this.getBlockState().getValue(ContainerBlock.FACING);
//
//            if (side == Direction.UP || side == Direction.DOWN) {
//                return LazyOptional.of(() -> new SidedFluidHandler(fluidTank, ioPorts.get(side))).cast();
//            }
//
//            return switch (localDir) {
//                case NORTH ->
//                        LazyOptional.of(() -> new SidedFluidHandler(fluidTank, ioPorts.get(side.getOpposite()))).cast();
//                case EAST ->
//                        LazyOptional.of(() -> new SidedFluidHandler(fluidTank, ioPorts.get(side.getClockWise()))).cast();
//                case SOUTH -> LazyOptional.of(() -> new SidedFluidHandler(fluidTank, ioPorts.get(side))).cast();
//                case WEST ->
//                        LazyOptional.of(() -> new SidedFluidHandler(fluidTank, ioPorts.get(side.getCounterClockWise()))).cast();
//                default -> LazyOptional.empty();
//            };
//    }

    protected final void addItemHandler(int slots, ValidationFunctions.ItemValid validation) {
        this.itemHandler = new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                onItemsChanged(slot);
                update();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return validation.itemValid(slot, stack);
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };
    }

    private void update() {
        if (!level.isClientSide())
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    protected final void addFluidTank(int capacityInMb, ValidationFunctions.FluidValid validation) {
        this.fluidTank = new FluidTank(capacityInMb) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                onFluidsChanged();
                update();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return validation.fluidValid(stack);
            }
        };
    }

    protected void onItemsChanged(int slot) {
    }

    protected void onFluidsChanged() {
    }

    public void tick() {
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
        super.onDataPacket(net, pkt, provider);
    }

    public final static class ValidationFunctions {
        @FunctionalInterface
        public interface ItemValid {
            boolean itemValid(int slot, ItemStack itemStack);
        }

        @FunctionalInterface
        public interface FluidValid {
            boolean fluidValid(FluidStack fluidStack);
        }
    }
}