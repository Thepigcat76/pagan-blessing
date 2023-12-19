package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.entities.EternalSnowballEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PBEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PaganBless.MODID);

    public static final RegistryObject<EntityType<EternalSnowballEntity>> ETERNAL_SNOWBALL =
            ENTITY_TYPES.register("eternal_snowball",
                    () -> EntityType.Builder.<EternalSnowballEntity>of(EternalSnowballEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("eternal_snowball"));

}
