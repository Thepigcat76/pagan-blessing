package com.pigdad.paganbless.content.blockentities;

import com.mojang.datafixers.util.Pair;
import com.pigdad.paganbless.api.blocks.ContainerBlockEntity;
import com.pigdad.paganbless.api.io.IOActions;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.content.blocks.RopeBlock;
import com.pigdad.paganbless.content.screens.WinchMenu;
import com.pigdad.paganbless.utils.WinchUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

import java.util.Map;

public class WinchBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private int distance;
    private boolean liftDown;

    public WinchBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.WINCH.get(), p_155229_, p_155230_);
        addItemHandler(1, (slot, itemStack) -> Block.byItem(itemStack.getItem()) instanceof RopeBlock);
    }

    public boolean isLiftDown() {
        return liftDown;
    }

    public void setLiftDown(boolean liftDown) {
        this.liftDown = liftDown;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void tick() {
        if (isLiftDown()) {
            WinchUtils.liftDown(this);
        }
    }

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getItemIO() {
        return Map.of(
                Direction.UP, Pair.of(IOActions.INSERT, new int[]{0}),
                Direction.DOWN, Pair.of(IOActions.EXTRACT, new int[]{0})
        );
    }

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getFluidIO() {
        return Map.of();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Winch");
    }

    @Override
    protected void saveData(CompoundTag tag) {
        tag.putInt("distance", distance);
        tag.putBoolean("lift_down", liftDown);
    }

    @Override
    protected void loadData(CompoundTag tag) {
        this.distance = tag.getInt("distance");
        this.liftDown = tag.getBoolean("lift_down");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new WinchMenu(i, inventory, this);
    }
}
