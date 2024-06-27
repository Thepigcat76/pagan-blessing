package com.pigdad.paganbless.api.screen;

import com.pigdad.paganbless.api.blocks.ContainerBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PBAbstractContainerMenu<T extends ContainerBlockEntity> extends AbstractContainerMenu {
    protected final @Nullable T blockEntity;
    protected final Player player;

    public PBAbstractContainerMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inv, T blockEntity) {
        super(menuType, containerId);
        checkContainerSize(inv, 1);
        this.blockEntity = blockEntity;
        this.player = inv.player;
    }

    public @Nullable T getBlockEntity() {
        return blockEntity;
    }

    // Quick move code is taken from COFHCore.
    // All credits for this method, and it's helper methods go to teamCOFH
    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        if (!supportsShiftClick(player, index)) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            stack = stackInSlot.copy();

            if (!performMerge(index, stackInSlot)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stackInSlot, stack);

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stackInSlot);
        }
        return stack;
    }

    protected int getMergeableSlotCount() {
        return blockEntity == null ? 0 : blockEntity.getItemHandler().getSlots();
    }

    protected boolean supportsShiftClick(Player player, int index) {
        return true;
    }

    protected boolean performMerge(int index, ItemStack stack) {
        int invBase = getMergeableSlotCount();
        int invFull = slots.size();
        int invHotbar = invFull - 9;
        int invPlayer = invHotbar - 27;

        if (index < invPlayer) {
            return moveItemStackTo(stack, invPlayer, invFull, false);
        } else {
            return moveItemStackTo(stack, 0, invBase, false);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.getType().isValid(blockEntity.getBlockState());
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 83 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 141));
        }
    }

    protected void addPlayerInventory(Inventory playerInventory, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, y + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory, int y) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, y));
        }
    }
}