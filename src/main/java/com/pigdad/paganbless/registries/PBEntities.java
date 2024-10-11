package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.content.entities.EternalSnowballEntity;
import com.pigdad.paganbless.content.entities.WandProjectileEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class PBEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PaganBless.MODID);

    public static final Supplier<EntityType<EternalSnowballEntity>> ETERNAL_SNOWBALL =
            ENTITY_TYPES.register("eternal_snowball",
                    () -> EntityType.Builder.<EternalSnowballEntity>of(EternalSnowballEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("eternal_snowball"));
    public static final Supplier<EntityType<WandProjectileEntity>> WAND_PROJECTILE =
            ENTITY_TYPES.register("wand_projectile",
                    () -> EntityType.Builder.<WandProjectileEntity>of(WandProjectileEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("wand_projectile"));

}
