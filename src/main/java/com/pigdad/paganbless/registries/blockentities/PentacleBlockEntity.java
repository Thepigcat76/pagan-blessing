package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.items.CaptureSacrificeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PentacleBlockEntity extends BlockEntity {
    private int timer = 0;
    private EntityType<?> entityType;

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

        @Override
        public void setEntityId(EntityType<?> p_253682_, @org.jetbrains.annotations.Nullable Level p_254041_, RandomSource p_254221_, BlockPos p_254050_) {
            super.setEntityId(p_253682_, p_254041_, p_254221_, p_254050_);

            PentacleBlockEntity.this.setEntityType(p_253682_);
        }
    };

    public void setEntityType(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public PentacleBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.PENTACLE.get(), p_155229_, p_155230_);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.spawner.load(level, getBlockPos(), tag);
        this.entityType = EntityType.by(tag.getCompound("entity")).get();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.spawner.save(tag);
        tag.put("entity", new CompoundTag());
        tag.getCompound("entity").putString("id", EntityType.getKey(this.entityType).toString());
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        spawner.serverTick((ServerLevel) level, blockPos);
    }

    @Override
    public void saveToItem(ItemStack p_187477_) {
        ((CaptureSacrificeItem) p_187477_.getItem()).setEntity(this.getEntityType(), p_187477_);
    }
}
