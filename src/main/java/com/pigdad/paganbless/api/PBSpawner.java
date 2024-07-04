package com.pigdad.paganbless.api;

import com.pigdad.paganbless.PBConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import org.jetbrains.annotations.Nullable;

public abstract class PBSpawner extends BaseSpawner {
    public PBSpawner() {
        this.minSpawnDelay = PBConfig.pentacleMinDelay;
        this.maxSpawnDelay = PBConfig.pentacleMaxDelay;
        this.spawnCount = PBConfig.pentacleSpawnAmount;
    }
}
