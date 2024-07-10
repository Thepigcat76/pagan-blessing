package com.pigdad.paganbless.registries.screens;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.api.screen.PBAbstractContainerMenu;
import com.pigdad.paganbless.registries.PBMenuTypes;
import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class WinchMenu extends PBAbstractContainerMenu<WinchBlockEntity> {
    private int slotAmount;

    public WinchMenu(int containerId, Inventory inv, WinchBlockEntity blockEntity) {
        super(PBMenuTypes.WINCH_MENU.get(), containerId, inv, blockEntity);
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 80, 36));
    }

    public WinchMenu(int containerId, Inventory inv, FriendlyByteBuf byteBuf) {
        this(containerId, inv, (WinchBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    @Override
    protected Slot addSlot(Slot slot) {
        slotAmount++;
        return super.addSlot(slot);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots and the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int pIndex) {
        try {
            Slot sourceSlot = slots.get(pIndex);

            if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM

            ItemStack sourceStack = sourceSlot.getItem();
            ItemStack copyOfSourceStack = sourceStack.copy();

            // Check if the slot clicked is one of the vanilla container slots
            int endIndex = TE_INVENTORY_FIRST_SLOT_INDEX + this.slotAmount - VANILLA_SLOT_COUNT;
            PaganBless.LOGGER.debug("End index: {}", endIndex);
            if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
                // This is a vanilla container slot so merge the stack into the tile inventory
                PaganBless.LOGGER.debug("inv slot: {}", TE_INVENTORY_FIRST_SLOT_INDEX);
                if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, endIndex, false)) {
                    return ItemStack.EMPTY;  // EMPTY_ITEM
                }
            } else if (pIndex < endIndex) {
                // This is a TE slot so merge the stack into the players inventory
                if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                System.out.println("Invalid slotIndex:" + pIndex);
                return ItemStack.EMPTY;
            }
            // If stack size == 0 (the entire stack was moved) set slot contents to null
            if (sourceStack.getCount() == 0) {
                sourceSlot.set(ItemStack.EMPTY);
            } else {
                sourceSlot.setChanged();
            }
            sourceSlot.onTake(playerIn, sourceStack);
            return copyOfSourceStack;
        } catch (Exception e) {
            PaganBless.LOGGER.error("Encountered error: ", e);
        }
        return ItemStack.EMPTY;
    }
}
