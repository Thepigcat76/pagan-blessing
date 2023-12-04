package com.pigdad.pigdadmod.registries;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.blockentities.ImbuingCauldronBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PigDadMod.MODID);

    public static final RegistryObject<BlockEntityType<ImbuingCauldronBlockEntity>> IMBUING_CAULDRON =
            BLOCK_ENTITIES.register("imbuing_cauldron", () ->
                    BlockEntityType.Builder.of(ImbuingCauldronBlockEntity::new,
                            ModBlocks.IMBUING_CAULDRON.get()).build(null));
}
