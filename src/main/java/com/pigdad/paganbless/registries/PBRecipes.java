package com.pigdad.paganbless.registries;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.recipes.AnvilSmashingRecipe;
import com.pigdad.paganbless.registries.recipes.BenchCuttingRecipe;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class PBRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, PaganBless.MODID);

    static {
        SERIALIZERS.register(AnvilSmashingRecipe.NAME, () -> AnvilSmashingRecipe.Serializer.INSTANCE);
        SERIALIZERS.register(ImbuingCauldronRecipe.NAME, () -> ImbuingCauldronRecipe.Serializer.INSTANCE);
        SERIALIZERS.register(RunicRitualRecipe.NAME, () -> RunicRitualRecipe.Serializer.INSTANCE);
        SERIALIZERS.register(BenchCuttingRecipe.NAME, () -> BenchCuttingRecipe.Serializer.INSTANCE);
    }
}
