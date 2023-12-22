package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.paganbless.registries.blockentities.PentacleBlockEntity;
import com.pigdad.paganbless.registries.blockentities.RuneSlabBlockEntity;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

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
    public static final RegistryObject<BlockEntityType<RuneSlabBlockEntity>> RUNE_SLAB =
            BLOCK_ENTITIES.register("rune_slab", () ->
                    BlockEntityType.Builder.of(RuneSlabBlockEntity::new,
                            getRuneSlabs()).build(null));

    private static Block[] getRuneSlabs() {
        List<Block> slabs = new ArrayList<>();
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (block instanceof RuneSlabBlock) {
                slabs.add(block);
            }
        }
        Block[] slabArray = new Block[slabs.size()];
        return slabs.toArray(slabArray);
    }
}
