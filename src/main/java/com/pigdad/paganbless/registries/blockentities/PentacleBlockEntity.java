package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PentacleBlockEntity extends BlockEntity {
    @Nullable
    private CompoundTag entityTag;
    private int timer = 0;
    public BaseSpawner spawner = new BaseSpawner() {
        public void broadcastEvent(Level p_155767_, BlockPos p_155768_, int p_155769_) {
            p_155767_.blockEvent(p_155768_, Blocks.SPAWNER, p_155769_, 0);
        }

        public void setNextSpawnData(@Nullable Level p_155771_, BlockPos p_155772_, SpawnData p_155773_) {
            super.setNextSpawnData(p_155771_, p_155772_, p_155773_);
            if (p_155771_ != null) {
                BlockState blockstate = p_155771_.getBlockState(p_155772_);
                p_155771_.sendBlockUpdated(p_155772_, blockstate, blockstate, 4);
            }

        }

        public @NotNull BlockEntity getSpawnerBlockEntity() {
            return PentacleBlockEntity.this;
        }
    };

    public PentacleBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.PENTACLE.get(), p_155229_, p_155230_);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.spawner.load(level, getBlockPos(), tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.spawner.save(tag);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        spawner.serverTick((ServerLevel) level, blockPos);
    }
}
