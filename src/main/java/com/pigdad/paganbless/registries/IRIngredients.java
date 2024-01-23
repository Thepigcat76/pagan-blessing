package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class IRIngredients {
    public static final DeferredRegister<IngredientType<?>> INGREDIENTS = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, PaganBless.MODID);

    // public static final Supplier<IngredientType<?>> INGREDIENT_WITH_COUNT = INGREDIENTS.register("with_count", () -> new IngredientType<IngredientWithCount>(null));
}
