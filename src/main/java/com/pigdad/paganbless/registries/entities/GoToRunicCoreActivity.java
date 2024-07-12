package com.pigdad.paganbless.registries.entities;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Zombie;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GoToRunicCoreActivity extends Behavior<LivingEntity> {
    public GoToRunicCoreActivity() {
        super(Map.of(PBMemoryModuleTypes.IS_GOING_TO_RC.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean hasRequiredMemories(LivingEntity owner) {
        return true;
    }

    @Override
    protected void start(ServerLevel level, LivingEntity entity, long gameTime) {
        PaganBless.LOGGER.debug("Starting!!!");
    }

    @Override
    protected void tick(ServerLevel level, LivingEntity owner, long gameTime) {
        PaganBless.LOGGER.debug("Running!!!");
    }

    @Override
    protected void stop(ServerLevel level, LivingEntity entity, long gameTime) {
        PaganBless.LOGGER.debug("Stopping!!!");
    }
}
