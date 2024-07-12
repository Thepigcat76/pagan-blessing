package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class PBActivities {
    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(BuiltInRegistries.ACTIVITY, PaganBless.MODID);

    public static final DeferredHolder<Activity, Activity> GO_TO_RUNIC_CORE_ACTIVITY = ACTIVITIES.register("go_to_runic_core_activity",
            () -> new Activity("go_to_runic_core_activity"));
}
