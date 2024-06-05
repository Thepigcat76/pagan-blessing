package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blocks.WinchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class WinchBlockEntity extends ContainerBlockEntity {
    private int timer;

    public WinchBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.WINCH.get(), p_155229_, p_155230_);
        addItemHandler(1);
    }

    @Override
    public void tick() {
        BlockState blockState = getBlockState();
        if (blockState.getValue(WinchBlock.LIFT_DOWN)) {
            level.setBlockAndUpdate(worldPosition, blockState.setValue(WinchBlock.LIFT_DOWN, WinchBlock.liftDown(level, worldPosition, blockState)));
        }
    }

    @Override
    protected void saveOther(CompoundTag tag) {
        tag.putInt("timer", this.timer);
    }

    @Override
    protected void loadOther(CompoundTag tag) {
        super.loadOther(tag);
        this.timer = tag.getInt("timer");
    }
}
