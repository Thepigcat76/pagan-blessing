package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.data.AdvancedBundleContents;
import com.pigdad.paganbless.registries.items.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class PBItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, PaganBless.MODID);
    public static final Supplier<Item> PAGAN_GUIDE;
    public static final Supplier<Item> RUE = registerItem("rue",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> BELLADONNA = registerItem("belladonna",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> HAG_TAPER = registerItem("hag_taper",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> LAVENDER = registerItem("lavender",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> MANDRAKE_ROOT = registerItem("mandrake_root",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> MUGWORT = registerItem("mugwort",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WINTER_BERRIES = registerItem("winter_berries",
            () -> new ItemNameBlockItem(PBBlocks.WINTER_BERRY_BUSH.get(), new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.1F)
                            .build())));
    public static final Supplier<Item> JAR = registerItem("jar",
            () -> new JarItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> GLAZED_BERRIES = registerItem("glazed_berries",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.4F)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED), 1.0F)
                            .build())));
    public static final Supplier<Item> CINNABAR = registerItem("cinnabar",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RUNIC_CHARGE = registerItem("runic_charge",
            () -> new RunicChargeItem(new Item.Properties().stacksTo(16)));
    public static final Supplier<Item> WAND = registerItem("wand",
            () -> new WandItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final Supplier<Item> CHALICE = registerItem("chalice",
            () -> new ChaliceItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final Supplier<Item> ATHAME = registerItem("athame",
            () -> new AthameItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final Supplier<Item> WICAN_WARD = registerItem("wican_ward",
            () -> new BlockItem(PBBlocks.WICAN_WARD.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final Supplier<Item> ETERNAL_SNOWBALL = registerItem("eternal_snowball",
            () -> new EternalSnowBallItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final Supplier<Item> WAND_PROJECTILE = registerItem("wand_projectile",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PENTACLE = registerItem("pentacle",
            () -> new PentacleItem(PBBlocks.PENTACLE.get(), new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.ENTITY_DATA, CustomData.EMPTY)
                    .rarity(Rarity.RARE)));
    public static final Supplier<Item> BLACK_THORN_STAFF = registerItem("black_thorn_staff",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> HERB_POUCH = registerItem("herb_pouch",
            () -> new HerbPouchItem(new Item.Properties().stacksTo(1).component(PBDataComponents.ADVANCED_BUNDLE_CONTENTS, AdvancedBundleContents.EMPTY)));
    public static final Supplier<Item> BLACK_THORN_STICK = registerItem("black_thorn_stick",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> BOLINE = registerItem("boline",
            () -> new BolineItem(new Item.Properties().stacksTo(1)));

    static {
        PAGAN_GUIDE = null;
        /*
        if (ModList.get().isLoaded("modonomicon")) {
            PAGAN_GUIDE = ModonomiconCompat.registerItem();
        } else {
            PAGAN_GUIDE = null;
        }
         */
    }

    public static Supplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }
}
