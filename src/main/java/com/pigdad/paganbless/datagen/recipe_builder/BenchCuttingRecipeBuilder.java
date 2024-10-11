package com.pigdad.paganbless.datagen.recipe_builder;

import com.pigdad.paganbless.content.recipes.BenchCuttingRecipe;
import com.pigdad.paganbless.utils.recipes.IngredientWithCount;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record BenchCuttingRecipeBuilder(IngredientWithCount ingredient, int cuts, boolean tryDamage, Ingredient toolItem, ItemStack result) implements RecipeBuilder {
    public static BenchCuttingRecipeBuilder newRecipe(ItemStack result, IngredientWithCount ingredient, Ingredient toolItem, int cuts, boolean tryDamage) {
        return new BenchCuttingRecipeBuilder(ingredient, cuts, tryDamage, toolItem, result);
    }

    @Override
    public @NotNull BenchCuttingRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    @Override
    public @NotNull BenchCuttingRecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        recipeOutput.accept(resourceLocation, new BenchCuttingRecipe(ingredient, cuts, tryDamage, toolItem, result), null);
    }
}
