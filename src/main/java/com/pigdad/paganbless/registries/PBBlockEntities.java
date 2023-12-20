package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.paganbless.registries.blockentities.PentacleBlockEntity;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PBBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PaganBless.MODID);

    public static final RegistryObject<BlockEntityType<ImbuingCauldronBlockEntity>> IMBUING_CAULDRON =
            BLOCK_ENTITIES.register("imbuing_cauldron.json", () ->
                    BlockEntityType.Builder.of(ImbuingCauldronBlockEntity::new,
                            PBBlocks.IMBUING_CAULDRON.get()).build(null));
    public static final RegistryObject<BlockEntityType<PentacleBlockEntity>> PENTACLE =
            BLOCK_ENTITIES.register("pentacle", () ->
                    BlockEntityType.Builder.of(PentacleBlockEntity::new,
                            PBBlocks.PENTACLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<RunicCoreBlockEntity>> RUNIC_CORE =
            BLOCK_ENTITIES.register("runic_core", () ->
                    BlockEntityType.Builder.of(RunicCoreBlockEntity::new,
                            PBBlocks.RUNIC_CORE.get()).build(null));
}
