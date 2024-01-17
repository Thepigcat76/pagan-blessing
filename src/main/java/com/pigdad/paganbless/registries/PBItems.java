package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.items.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PBItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, PaganBless.MODID);
    public static Supplier<Item> PAGAN_GUIDE;
    public static final Supplier<Item> RUE = registerItem("rue",
            () -> new Item(new Item.Properties().stacksTo(1)));
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
                            .saturationMod(0.1F)
                            .build())));
    public static final Supplier<Item> GLAZED_BERRIES = registerItem("glazed_berries",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.4F)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED), 1.0F)
                            .build())));
    public static final Supplier<Item> CINNABAR = registerItem("cinnabar",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RUNIC_CHARGE = registerItem("runic_charge",
            () -> new RunicChargeItem(new Item.Properties().stacksTo(16)));
    public static final Supplier<Item> WAND = registerItem("wand",
            () -> new WandItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> CHALICE = registerItem("chalice",
            () -> new ChaliceItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> ATHAME = registerItem("athame",
            () -> new AthameItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> WICAN_WARD = registerItem("wican_ward",
            () -> new BlockItem(PBBlocks.WICAN_WARD.get(), new Item.Properties()));
    public static final Supplier<Item> ETERNAL_SNOWBALL = registerItem("eternal_snowball",
            () -> new EternalSnowBallItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> PENTACLE = registerItem("pentacle",
            () -> new PentacleItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> BLACK_THORN_STAFF = registerItem("black_thorn_staff",
            () -> new ToolTipItem(new Item.Properties().stacksTo(1), Component.translatable("desc.paganbless.black_thorn_staff")
                    .withStyle(ChatFormatting.GRAY)));
    public static final Supplier<Item> HERB_POUCH = registerItem("herb_pouch",
            () -> new HerbPouchItem(new Item.Properties().stacksTo(1)));

    /*
    static {
        try {
            if (ModList.get().isLoaded(Modonomicon.MOD_ID)) {
                PAGAN_GUIDE = registerItem("pagan_guide", () -> new PaganGuideItem(new Item.Properties().stacksTo(1)));
            }
        } catch (Exception ignored) {
            PaganBless.LOGGER.info("Failed to load modonomicon. Consider installing it");
        }
    }
     */

    private static Supplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }
}
