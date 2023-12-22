package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blocks.*;
import com.pigdad.paganbless.registries.items.RuneSlabItem;
import com.pigdad.paganbless.registries.worldgen.BlackThornTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PBBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PaganBless.MODID);
    public static final RegistryObject<Block> IMBUING_CAULDRON = registerBlockAndItem("imbuing_cauldron",
            () -> new ImbuingCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).noOcclusion()));
    public static final RegistryObject<Block> RUNE_SLAB_AMETHYST = registerRuneSlab("rune_slab_amethyst");
    public static final RegistryObject<Block> RUNE_SLAB_CINNABAR = registerRuneSlab("rune_slab_cinnabar");
    public static final RegistryObject<Block> RUNE_SLAB_DIAMOND = registerRuneSlab("rune_slab_diamond");
    public static final RegistryObject<Block> RUNE_SLAB_EMERALD = registerRuneSlab("rune_slab_emerald");
    public static final RegistryObject<Block> RUNE_SLAB_QUARTZ = registerRuneSlab("rune_slab_quartz");
    public static final RegistryObject<Block> RUNE_SLAB_LAPIS = registerRuneSlab("rune_slab_lapis");
    public static final RegistryObject<Block> RUNE_SLAB_INERT = registerRuneSlab("rune_slab_inert");
    public static final RegistryObject<Block> RUE_PLANT = registerBlockAndItem("rue_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> BELLADONNA_PLANT = registerBlockAndItem("belladonna_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> HAGS_TAPER_PLANT = registerBlockAndItem("hags_taper_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> LAVENDER_PLANT = registerBlockAndItem("lavender_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> MANDRAKE_ROOT_PLANT = registerBlockAndItem("mandrake_root_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> MUGWORT_PLANT = registerBlockAndItem("mugwort_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> WINTER_BERRY_BUSH = registerBlock("winter_berry_bush",
            () -> new WinterBerryBushBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.SWEET_BERRY_BUSH)));
    public static final RegistryObject<Block> BLACK_THORN_LOG = registerBlockAndItem("black_thorn_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> BLACK_THORN_LEAVES = registerBlockAndItem("black_thorn_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });
    public static final RegistryObject<Block> BLACK_THORN_SAPLING = registerBlockAndItem("black_thorn_sapling",
            () -> new SaplingBlock(new BlackThornTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> PENTACLE = registerBlock("pentacle",
            () -> new PentacleBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .instabreak()));
    public static final RegistryObject<Block> WICAN_WARD = registerBlock("wican_ward",
            () -> new PentacleBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()));
    public static final RegistryObject<Block> RUNIC_CORE = registerBlockAndItem("runic_core",
            () -> new RunicCoreBlock(BlockBehaviour.Properties.of().noOcclusion()));

    private static RegistryObject<Block> registerBlockAndItem(String name, Supplier<Block> block) {
        RegistryObject<Block> toReturn = BLOCKS.register(name, block);
        registerItemFromBlock(name, toReturn);
        return toReturn;
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> void registerItemFromBlock(String name, Supplier<T> block) {
        PBItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Block> registerRuneSlab(String name) {
        RegistryObject<Block> toReturn = BLOCKS.register(name, () -> new RuneSlabBlock(BlockBehaviour.Properties.of()));
        PBItems.ITEMS.register(name, () -> new RuneSlabItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    private static RotatedPillarBlock log(MapColor p_285370_, MapColor p_285126_) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of()
                .mapColor((p_152624_) -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? p_285370_ : p_285126_)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava());
    }

    private static LeavesBlock leaves(SoundType p_152615_) {
        return new LeavesBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(p_152615_)
                .noOcclusion()
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY));
    }
}
