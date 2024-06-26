package com.pigdad.paganbless.api.io;


import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public record SidedItemHandler(IItemHandlerModifiable innerHandler,
                               IOActions action,
                               List<Integer> slots) implements IItemHandlerModifiable {
    public SidedItemHandler(IItemHandlerModifiable innerHandler, Pair<IOActions, int[]> actionSlotsPair) {
        this(innerHandler, actionSlotsPair != null ? actionSlotsPair.getFirst() : IOActions.NONE, actionSlotsPair != null ? Arrays.stream(actionSlotsPair.getSecond()).boxed().toList() : List.of());
    }

    @Override
    public void setStackInSlot(int i, @NotNull ItemStack itemStack) {
        innerHandler.setStackInSlot(i, itemStack);
    }

    @Override
    public int getSlots() {
        return innerHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return innerHandler.getStackInSlot(i);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate) {
        return action == IOActions.INSERT && slots.contains(slot) ? innerHandler.insertItem(slot, itemStack, simulate) : itemStack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return action == IOActions.EXTRACT && slots.contains(slot) ? innerHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int i) {
        return innerHandler.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack itemStack) {
        return action == IOActions.INSERT && slots.contains(slot) && innerHandler.isItemValid(slot, itemStack);
    }
}
