package com.pigdad.paganbless.registries.screens;

import com.pigdad.paganbless.api.screen.PBAbstractContainerMenu;
import com.pigdad.paganbless.registries.PBMenuTypes;
import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WinchMenu extends PBAbstractContainerMenu<WinchBlockEntity> {
    public WinchMenu(int containerId, Inventory inv, WinchBlockEntity blockEntity) {
        super(PBMenuTypes.WINCH_MENU.get(), containerId, inv, blockEntity);
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 80, 36));
    }

    public WinchMenu(int containerId, Inventory inv, FriendlyByteBuf byteBuf) {
        this(containerId, inv, (WinchBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }
}
