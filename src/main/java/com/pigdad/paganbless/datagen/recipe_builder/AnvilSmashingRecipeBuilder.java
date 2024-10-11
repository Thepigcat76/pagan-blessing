package com.pigdad.paganbless.datagen.recipe_builder;

import com.pigdad.paganbless.content.recipes.AnvilSmashingRecipe;
import com.pigdad.paganbless.utils.recipes.IngredientWithCount;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AnvilSmashingRecipeBuilder implements RecipeBuilder {
    private final ItemStack result;
    private final List<IngredientWithCount> ingredients;

    private AnvilSmashingRecipeBuilder(ItemStack result, List<IngredientWithCount> ingredients) {
        this.result = result;
        this.ingredients = ingredients;
    }

    public static AnvilSmashingRecipeBuilder newRecipe(ItemStack result, IngredientWithCount... ingredients) {
        return new AnvilSmashingRecipeBuilder(result, List.of(ingredients));
    }

    @Override
    public @NotNull AnvilSmashingRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    @Override
    public @NotNull AnvilSmashingRecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        recipeOutput.accept(resourceLocation, new AnvilSmashingRecipe(this.ingredients, this.result), null);
    }
}
