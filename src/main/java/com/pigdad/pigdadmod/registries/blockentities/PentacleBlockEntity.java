package com.pigdad.pigdadmod.registries.blockentities;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal;
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
    @Nullable private CompoundTag entityTag;
    private int timer = 0;

    public PentacleBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.PENTACLE.get(), p_155229_, p_155230_);
    }

    public void setEntityTag(CompoundTag entityTag) {
        entityTag.put("Pos", newDoubleList(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()));
        this.entityTag = entityTag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.entityTag = tag.getCompound("entity");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (entityTag != null) {
            tag.put("entity", entityTag);
        }
    }

    public void spawnEntity() {
        if (entityTag != null){
            try {
                level.addFreshEntity(EntityType.create(entityTag, level).get());
            } catch (Exception ignored) {
                PigDadMod.LOGGER.warn("Failed to spawn entity");
            }
        } else {
            PigDadMod.LOGGER.info("Entity tag is null");
        }
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {

    }

    protected ListTag newDoubleList(double... p_20064_) {
        ListTag listtag = new ListTag();

        for(double d0 : p_20064_) {
            listtag.add(DoubleTag.valueOf(d0));
        }

        return listtag;
    }
}
