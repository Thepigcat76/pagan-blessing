package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.items.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PBItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PaganBless.MODID);
    public static final RegistryObject<Item> RUE = registerItem("rue",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BELLADONNA = registerItem("belladonna",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAG_TAPER = registerItem("hag_taper",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LAVENDER = registerItem("lavender",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MANDRAKE_ROOT = registerItem("mandrake_root",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MUGWORT = registerItem("mugwort",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WINTER_BERRIES = registerItem("winter_berries",
            () -> new ItemNameBlockItem(PBBlocks.WINTER_BERRY_BUSH.get(), new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.1F)
                            .build())));
    public static final RegistryObject<Item> CINNABAR = registerItem("cinnabar",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RUNIC_CHARGE = registerItem("runic_charge",
            () -> new RunicChargeItem(new Item.Properties()));
    public static final RegistryObject<Item> WAND = registerItem("wand",
            () -> new WandItem(new Item.Properties()));
    public static final RegistryObject<Item> CHALICE = registerItem("chalice",
            () -> new ChaliceItem(new Item.Properties()));
    public static final RegistryObject<Item> ATHAME = registerItem("athame",
            () -> new AthameItem(new Item.Properties()));
    public static final RegistryObject<Item> WICAN_WARD = registerItem("wican_ward",
            () -> new BlockItem(PBBlocks.WICAN_WARD.get(), new Item.Properties()));
    public static final RegistryObject<Item> ETERNAL_SNOWBALL = registerItem("eternal_snowball",
            () -> new EternalSnowBallItem(new Item.Properties()));
    public static final RegistryObject<Item> PENTACLE = registerItem("pentacle",
            () -> new PentacleItem(new Item.Properties().stacksTo(1)));

    private static RegistryObject<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }
}
