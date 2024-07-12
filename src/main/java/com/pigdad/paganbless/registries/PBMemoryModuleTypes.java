package com.pigdad.paganbless.registries;

import com.mojang.serialization.Codec;
import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public final class PBMemoryModuleTypes {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, PaganBless.MODID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> IS_GOING_TO_RC = register("is_going_to_rc", Unit.CODEC);

    private static <U> DeferredHolder<MemoryModuleType<?>, MemoryModuleType<U>> register(String identifier, Codec<U> codec) {
        return MEMORY_MODULE_TYPES.register(identifier, () -> new MemoryModuleType<>(Optional.of(codec)));
    }
}
