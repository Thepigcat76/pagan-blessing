package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.data.AdvancedBundleContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class PBDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, PaganBless.MODID);

    public static final Supplier<DataComponentType<AdvancedBundleContents>> ADVANCED_BUNDLE_CONTENTS = registerDataComponentType("advanced_bundle_contents",
            () -> builder -> builder.persistent(AdvancedBundleContents.CODEC).networkSynchronized(AdvancedBundleContents.STREAM_CODEC));

    public static <T> Supplier<DataComponentType<T>> registerDataComponentType(
            String name, Supplier<UnaryOperator<DataComponentType.Builder<T>>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.get().apply(DataComponentType.builder()).build());
    }
}
