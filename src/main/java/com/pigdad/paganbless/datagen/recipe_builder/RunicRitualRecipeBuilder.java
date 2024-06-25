package com.pigdad.paganbless.datagen.recipe_builder;

import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RunicRitualRecipeBuilder implements RecipeBuilder {
    private final ItemStack result;
    private final Block runeSlabBlock;

    private RunicRitualRecipeBuilder(ItemStack result, Block runeSlabBlock) {
        this.result = result;
        this.runeSlabBlock = runeSlabBlock;
    }

    public static RunicRitualRecipeBuilder newRecipe(ItemStack result, Block runeSlabBlock) {
        return new RunicRitualRecipeBuilder(result, runeSlabBlock);
    }

    @Override
    public @NotNull RunicRitualRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    @Override
    public @NotNull RunicRitualRecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        recipeOutput.accept(resourceLocation, new RunicRitualRecipe(result, runeSlabBlock.asItem().getDefaultInstance()), null);
    }
}
