package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blocks.CrankBlock;
import com.pigdad.paganbless.registries.blocks.WinchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// NOTE: Code related to rotation is from the Create mod, licensed under the MIT license
// Credits for the decent parts in this code go to them
public class CrankBlockEntity extends BlockEntity {
    public float independentAngle;
    public float chasingVelocity;
    public int inUse;
    public int speed;
    public boolean dropping;

    public CrankBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PBBlockEntities.CRANK.get(), pPos, pBlockState);
        this.speed = 0;
    }

    public void turn() {
        this.inUse = 10;
        this.speed = 1200;
    }

    public void drop() {
        this.speed = -4000;
        this.dropping = true;
    }

    public void tick() {
        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;

        if (inUse > 0 && !dropping) {
            inUse--;

            if (inUse == 0) {
                this.speed = 0;
            }
        } else if (dropping) {
            if (!level.getBlockState(CrankBlock.getWinchPos(getBlockState(), getBlockPos())).getValue(WinchBlock.LIFT_DOWN)) {
                this.speed = 0;
                this.dropping = false;
            }
        }
    }

    private float getSpeed() {
        return speed;
    }

    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }
}
