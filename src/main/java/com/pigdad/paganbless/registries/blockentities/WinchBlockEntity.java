package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.api.blocks.ContainerBlockEntity;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blocks.RopeBlock;
import com.pigdad.paganbless.registries.blocks.WinchBlock;
import com.pigdad.paganbless.registries.screens.WinchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WinchBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private int timer;

    public WinchBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.WINCH.get(), p_155229_, p_155230_);
        addItemHandler(1, (slot, itemStack) -> Block.byItem(itemStack.getItem()) instanceof RopeBlock);
    }

    @Override
    public void tick() {
        BlockState blockState = getBlockState();
        if (blockState.getValue(WinchBlock.LIFT_DOWN) && this.getItemHandler().getStackInSlot(0).getCount() > 0) {
            boolean liftDown = WinchBlock.liftDown(level, worldPosition, blockState);
            if (liftDown) {
                this.getItemHandler().extractItem(0, 1, false);
            }
            level.setBlockAndUpdate(worldPosition, blockState.setValue(WinchBlock.LIFT_DOWN, liftDown));
        } else {
            level.setBlockAndUpdate(worldPosition, blockState.setValue(WinchBlock.LIFT_DOWN, false));
        }
    }

    @Override
    protected void saveData(CompoundTag tag) {
        tag.putInt("timer", this.timer);
    }

    @Override
    protected void loadData(CompoundTag tag) {
        this.timer = tag.getInt("timer");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Winch");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new WinchMenu(i, inventory, this);
    }
}
