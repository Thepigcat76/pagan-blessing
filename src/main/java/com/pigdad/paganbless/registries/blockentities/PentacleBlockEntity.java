package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PentacleBlockEntity extends BlockEntity {
    @Nullable
    private CompoundTag entityTag;
    private int timer = 0;
    private BaseSpawner spawner = new BaseSpawner() {
        @Override
        public void broadcastEvent(Level p_151322_, BlockPos p_151323_, int p_151324_) {

        }
    };

    public PentacleBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.PENTACLE.get(), p_155229_, p_155230_);
    }

    public void setEntityTag(CompoundTag entityTag) {
        entityTag.put("Pos", newDoubleList(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()));
        this.entityTag = entityTag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.entityTag = tag.getCompound("entity");
        this.timer = tag.getInt("timer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (entityTag != null) {
            tag.put("entity", entityTag);
            tag.putInt("timer", timer);
        }
    }

    public void spawnEntity() {
        if (entityTag != null) {
            try {
                level.addFreshEntity(EntityType.create(entityTag, level).get());
            } catch (Exception ignored) {
                PaganBless.LOGGER.warn("Failed to spawn entity");
            }
        } else {
            PaganBless.LOGGER.info("Entity tag is null");
        }
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        RandomSource randomSource = level.getRandom();
        timer++;
        if (timer > 600 + randomSource.nextInt(0, 300)) {
            timer = 0;
            for (int i = 0; i < randomSource.nextInt(1, 4); i++) {
                spawnEntity();
            }
        }
    }

    protected ListTag newDoubleList(double... p_20064_) {
        ListTag listtag = new ListTag();

        for (double d0 : p_20064_) {
            listtag.add(DoubleTag.valueOf(d0));
        }

        return listtag;
    }
}
