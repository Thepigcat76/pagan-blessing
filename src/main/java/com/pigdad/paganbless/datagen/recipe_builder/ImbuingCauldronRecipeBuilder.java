package com.pigdad.paganbless.datagen.recipe_builder;

import com.pigdad.paganbless.content.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.utils.recipes.IngredientWithCount;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImbuingCauldronRecipeBuilder implements RecipeBuilder {
    @NotNull private final List<IngredientWithCount> ingredients;
    @NotNull private final FluidStack fluid;
    @NotNull private final ItemStack result;

    private ImbuingCauldronRecipeBuilder(ItemStack result, FluidStack fluidStack) {
        this.ingredients = new ArrayList<>();
        this.fluid = fluidStack;
        this.result = result;
    }

    public static ImbuingCauldronRecipeBuilder newRecipe(ItemStack result, FluidStack fluidStack) {
        return new ImbuingCauldronRecipeBuilder(result, fluidStack);
    }

    @Override
    public @NotNull ImbuingCauldronRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    public ImbuingCauldronRecipeBuilder ingredients(IngredientWithCount... ingredients) {
        this.ingredients.addAll(Arrays.stream(ingredients).toList());
        return this;
    }
    public ImbuingCauldronRecipeBuilder ingredients(ItemStack... items) {
        this.ingredients.addAll(Arrays.stream(items).map(IngredientWithCount::fromItemStack).toList());
        return this;
    }

    public ImbuingCauldronRecipeBuilder ingredients(ItemLike... items) {
        this.ingredients.addAll(Arrays.stream(items).map(IngredientWithCount::fromItemLike).toList());
        return this;
    }

    @SafeVarargs
    public final ImbuingCauldronRecipeBuilder ingredients(TagKey<Item>... items) {
        this.ingredients.addAll(Arrays.stream(items).map(IngredientWithCount::fromItemTag).toList());
        return this;
    }

    @Override
    public @NotNull ImbuingCauldronRecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        ImbuingCauldronRecipe recipe = new ImbuingCauldronRecipe(this.ingredients, this.result, this.fluid);
        recipeOutput.accept(resourceLocation, recipe, null);
    }
}
