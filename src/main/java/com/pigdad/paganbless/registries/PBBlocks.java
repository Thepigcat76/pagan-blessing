package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.blocks.*;
import com.pigdad.paganbless.registries.items.RuneSlabItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PBBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, PaganBless.MODID);

    public static final Supplier<Block> IMBUING_CAULDRON = registerBlockAndItem("imbuing_cauldron",
            () -> new ImbuingCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).noOcclusion()));
    public static final Supplier<Block> CRANK = registerBlockAndItem("crank",
            () -> new CrankBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).noOcclusion()));
    public static final Supplier<Block> JAR = registerBlockAndItem("jar",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).noOcclusion()));
    public static final Supplier<Block> WINCH = registerBlockAndItem("winch",
            () -> new WinchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).noOcclusion()));
    public static final Supplier<Block> ROPE = registerBlockAndItem("rope",
            () -> new RopeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).noOcclusion().sound(SoundType.WOOL)));
    public static final Supplier<Block> RUNE_SLAB_AMETHYST = registerRuneSlab("rune_slab_amethyst");
    public static final Supplier<Block> RUNE_SLAB_CINNABAR = registerRuneSlab("rune_slab_cinnabar");
    public static final Supplier<Block> RUNE_SLAB_DIAMOND = registerRuneSlab("rune_slab_diamond");
    public static final Supplier<Block> RUNE_SLAB_EMERALD = registerRuneSlab("rune_slab_emerald");
    public static final Supplier<Block> RUNE_SLAB_QUARTZ = registerRuneSlab("rune_slab_quartz");
    public static final Supplier<Block> RUNE_SLAB_LAPIS = registerRuneSlab("rune_slab_lapis");
    public static final Supplier<Block> RUNE_SLAB_INERT = registerRuneSlab("rune_slab_inert");
    public static final Supplier<Block> RUE_PLANT = registerBlockAndItem("rue_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final Supplier<Block> BELLADONNA_PLANT = registerBlockAndItem("belladonna_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final Supplier<Block> HAGS_TAPER_PLANT = registerBlockAndItem("hags_taper_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final Supplier<Block> LAVENDER_PLANT = registerBlockAndItem("lavender_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final Supplier<Block> MANDRAKE_ROOT_PLANT = registerBlockAndItem("mandrake_root_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final Supplier<Block> MUGWORT_PLANT = registerBlockAndItem("mugwort_plant",
            () -> new HerbPlantBlock(5, BlockBehaviour.Properties.of()));
    public static final Supplier<Block> WINTER_BERRY_BUSH = registerBlock("winter_berry_bush",
            () -> new WinterBerryBushBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.SWEET_BERRY_BUSH)));
    public static final Supplier<Block> BLACK_THORN_LOG = registerBlockAndItem("black_thorn_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final Supplier<Block> STRIPPED_BLACK_THORN_LOG = registerBlockAndItem("stripped_black_thorn_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final Supplier<Block> BLACK_THORN_WOOD = registerBlockAndItem("black_thorn_wood",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final Supplier<Block> STRIPPED_BLACK_THORN_WOOD = registerBlockAndItem("stripped_black_thorn_wood",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final Supplier<Block> BLACK_THORN_PLANKS = registerBlockAndItem("black_thorn_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final Supplier<Block> BLACK_THORN_LEAVES = registerBlockAndItem("black_thorn_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)) {
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
    public static final Supplier<Block> BLACK_THORN_SAPLING = registerBlockAndItem("black_thorn_sapling",
            () -> new SaplingBlock(PBTreeGrowers.RUBBER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final Supplier<Block> PENTACLE = registerBlock("pentacle",
            () -> new PentacleBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .instabreak()));
    public static final Supplier<Block> WICAN_WARD = registerBlock("wican_ward",
            () -> new WicanWardBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()));
    public static final Supplier<Block> RUNIC_CORE = registerBlockAndItem("runic_core",
            () -> new RunicCoreBlock(BlockBehaviour.Properties.of().noOcclusion()));

    private static Supplier<Block> registerBlockAndItem(String name, Supplier<Block> block) {
        Supplier<Block> toReturn = BLOCKS.register(name, block);
        registerItemFromBlock(name, toReturn);
        return toReturn;
    }

    private static Supplier<Block> registerBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> void registerItemFromBlock(String name, Supplier<T> block) {
        PBItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static Supplier<Block> registerRuneSlab(String name) {
        Supplier<Block> toReturn = BLOCKS.register(name, () -> new RuneSlabBlock(BlockBehaviour.Properties.of()));
        PBItems.ITEMS.register(name, () -> new RuneSlabItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }
}
