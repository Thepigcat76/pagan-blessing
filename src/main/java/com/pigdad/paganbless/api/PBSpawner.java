package com.pigdad.paganbless.api;

import com.pigdad.paganbless.PBConfig;
import net.minecraft.world.level.BaseSpawner;

public abstract class PBSpawner extends BaseSpawner {
    public PBSpawner() {
        this.minSpawnDelay = PBConfig.pentacleMinDelay;
        this.maxSpawnDelay = PBConfig.pentacleMaxDelay;
        this.spawnCount = PBConfig.pentacleSpawnAmount;
    }
}
