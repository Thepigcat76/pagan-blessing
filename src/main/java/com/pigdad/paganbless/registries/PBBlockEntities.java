package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blockentities.*;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class PBBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PaganBless.MODID);

    public static final Supplier<BlockEntityType<ImbuingCauldronBlockEntity>> IMBUING_CAULDRON =
            BLOCK_ENTITIES.register("imbuing_cauldron", () ->
                    BlockEntityType.Builder.of(ImbuingCauldronBlockEntity::new,
                            PBBlocks.IMBUING_CAULDRON.get()).build(null));
    public static final Supplier<BlockEntityType<PentacleBlockEntity>> PENTACLE =
            BLOCK_ENTITIES.register("pentacle", () ->
                    BlockEntityType.Builder.of(PentacleBlockEntity::new,
                            PBBlocks.PENTACLE.get()).build(null));
    public static final Supplier<BlockEntityType<RunicCoreBlockEntity>> RUNIC_CORE =
            BLOCK_ENTITIES.register("runic_core", () ->
                    BlockEntityType.Builder.of(RunicCoreBlockEntity::new,
                            PBBlocks.RUNIC_CORE.get()).build(null));
    public static final Supplier<BlockEntityType<RuneSlabBlockEntity>> RUNE_SLAB =
            BLOCK_ENTITIES.register("rune_slab", () ->
                    BlockEntityType.Builder.of(RuneSlabBlockEntity::new,
                            getRuneSlabs()).build(null));
    public static final Supplier<BlockEntityType<CrankBlockEntity>> CRANK =
            BLOCK_ENTITIES.register("crank", () ->
                    BlockEntityType.Builder.of(CrankBlockEntity::new,
                            PBBlocks.CRANK.get()).build(null));
    public static final Supplier<BlockEntityType<JarBlockEntity>> JAR =
            BLOCK_ENTITIES.register("jar", () ->
                    BlockEntityType.Builder.of(JarBlockEntity::new,
                            PBBlocks.JAR.get()).build(null));

    private static Block[] getRuneSlabs() {
        List<Block> slabs = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK.stream().toList()) {
            if (block instanceof RuneSlabBlock) {
                slabs.add(block);
            }
        }
        Block[] slabArray = new Block[slabs.size()];
        return slabs.toArray(slabArray);
    }
}
