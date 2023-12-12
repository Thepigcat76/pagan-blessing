package com.pigdad.pigdadmod.registries.entities;

import com.pigdad.pigdadmod.PigDadMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PigDadMod.MODID);

    public static final RegistryObject<EntityType<EternalSnowballEntity>> ETERNAL_SNOWBALL =
            ENTITY_TYPES.register("eternal_snowball",
                    () -> EntityType.Builder.<EternalSnowballEntity>of(EternalSnowballEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("eternal_snowball"));

}
