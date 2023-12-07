package com.pigdad.pigdadmod.registries;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.items.RuneSlabItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PigDadMod.MODID);

    public static final RegistryObject<Item> RUNE_SLAB = registerItem("rune_slab",
            () -> new RuneSlabItem(ModBlocks.RUNE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUE = registerItem("rue",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WINTER_BERRIES = registerItem("winter_berries",
            () -> new ItemNameBlockItem(ModBlocks.WINTER_BERRY_BUSH.get(), new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.1F)
                            .build())));

    private static RegistryObject<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }
}
