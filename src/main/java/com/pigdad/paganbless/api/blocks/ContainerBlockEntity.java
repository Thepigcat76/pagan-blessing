package com.pigdad.paganbless.api.blocks;

import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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