package com.pigdad.paganbless.datagen;

import com.pigdad.paganbless.datagen.builder.AnvilSmashingRecipeBuilder;
import com.pigdad.paganbless.datagen.builder.BenchCuttingRecipeBuilder;
import com.pigdad.paganbless.datagen.builder.ImbuingCauldronRecipeBuilder;
import com.pigdad.paganbless.datagen.builder.RunicRitualRecipeBuilder;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.registries.items.PentacleItem;
import com.pigdad.paganbless.registries.recipes.AnvilSmashingRecipe;
import com.pigdad.paganbless.utils.recipes.IngredientWithCount;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.CompletableFuture;

public class PBRecipeProvider extends RecipeProvider {
    public PBRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        blackThornWoodRecipes(recipeOutput);
        otherRecipes(recipeOutput);

        imbuingCauldronRecipes(recipeOutput);
        runicRitualRecipes(recipeOutput);
        anvilSmashingRecipes(recipeOutput);
        benchCuttingRecipes(recipeOutput);
    }

    private void benchCuttingRecipes(RecipeOutput recipeOutput) {
        BenchCuttingRecipeBuilder.newRecipe(new ItemStack(PBItems.BLACK_THORN_STICK.get(), 3), IngredientWithCount.fromItemLike(PBBlocks.BLACK_THORN_LOG.get()),
                        Ingredient.of(ItemTags.AXES), 5, true)
                .save(recipeOutput);
        BenchCuttingRecipeBuilder.newRecipe(new ItemStack(PBItems.CHOPPED_RUE.get()), IngredientWithCount.fromItemLike(PBBlocks.DRIED_HANGING_RUE.get()),
                        Ingredient.of(PBItems.BOLINE.get()), 3, true)
                .save(recipeOutput);
        BenchCuttingRecipeBuilder.newRecipe(new ItemStack(PBItems.CHOPPED_LAVENDER.get()), IngredientWithCount.fromItemLike(PBBlocks.DRIED_HANGING_LAVENDER.get()),
                        Ingredient.of(PBItems.BOLINE.get()), 3, true)
                .save(recipeOutput);
    }

    private void anvilSmashingRecipes(RecipeOutput recipeOutput) {
        AnvilSmashingRecipeBuilder.newRecipe(new ItemStack(Items.REDSTONE, 15), new IngredientWithCount(Ingredient.of(PBTags.ItemTags.GEMS_CINNABAR), 4))
                .save(recipeOutput);
        AnvilSmashingRecipeBuilder.newRecipe(PBItems.JAR.get().getDefaultInstance(),
                        IngredientWithCount.fromItemLike(PBBlocks.BLACK_THORN_LOG.get()),
                        new IngredientWithCount(Ingredient.of(Tags.Items.GLASS_PANES), 5))
                .save(recipeOutput);
        AnvilSmashingRecipeBuilder.newRecipe(PBBlocks.RUNIC_CORE.get().asItem().getDefaultInstance(),
                        IngredientWithCount.fromItemLike(PBBlocks.BLACK_THORN_LOG.get()),
                        IngredientWithCount.fromItemLike(Items.SKELETON_SKULL),
                        new IngredientWithCount(Ingredient.of(ItemTags.CANDLES), 3))
                .save(recipeOutput);

        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_CINNABAR.get(), PBTags.ItemTags.GEMS_CINNABAR, 2);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_DIAMOND.get(), Tags.Items.GEMS_DIAMOND);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_EMERALD.get(), Tags.Items.GEMS_EMERALD);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_LAPIS.get(), Tags.Items.GEMS_LAPIS, 5);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_QUARTZ.get(), Tags.Items.GEMS_QUARTZ, 3);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_AMETHYST.get(), Tags.Items.GEMS_AMETHYST, 2);
    }

    private void runeSlabRecipe(RecipeOutput recipeOutput, Block runeSlabBlock, TagKey<Item> gemItem, int gemCount) {
        AnvilSmashingRecipeBuilder.newRecipe(runeSlabBlock.asItem().getDefaultInstance(),
                        new IngredientWithCount(Ingredient.of(gemItem), gemCount),
                        new IngredientWithCount(Ingredient.of(PBBlocks.RUNE_SLAB_INERT.get()), 1))
                .save(recipeOutput);
    }

    private void runeSlabRecipe(RecipeOutput recipeOutput, Block runeSlabBlock, TagKey<Item> gemItem) {
        runeSlabRecipe(recipeOutput, runeSlabBlock, gemItem, 1);
    }

    private void runicRitualRecipes(RecipeOutput recipeOutput) {
        RunicRitualRecipeBuilder.newRecipe(PentacleItem.getPentacleDefaultStack(), PBBlocks.RUNE_SLAB_CINNABAR.get())
                .save(recipeOutput);
        RunicRitualRecipeBuilder.newRecipe(PBItems.WICAN_WARD.get().getDefaultInstance(), PBBlocks.RUNE_SLAB_AMETHYST.get())
                .save(recipeOutput);
        RunicRitualRecipeBuilder.newRecipe(PBItems.ATHAME.get().getDefaultInstance(), PBBlocks.RUNE_SLAB_DIAMOND.get())
                .save(recipeOutput);
        RunicRitualRecipeBuilder.newRecipe(PBItems.WAND.get().getDefaultInstance(), PBBlocks.RUNE_SLAB_EMERALD.get())
                .save(recipeOutput);
        RunicRitualRecipeBuilder.newRecipe(PBItems.ETERNAL_SNOWBALL.get().getDefaultInstance(), PBBlocks.RUNE_SLAB_QUARTZ.get())
                .save(recipeOutput);
        RunicRitualRecipeBuilder.newRecipe(PBItems.CHALICE.get().getDefaultInstance(), PBBlocks.RUNE_SLAB_LAPIS.get())
                .save(recipeOutput);
    }

    private void imbuingCauldronRecipes(RecipeOutput recipeOutput) {
        ImbuingCauldronRecipeBuilder.newRecipe(new ItemStack(PBItems.GLAZED_BERRIES.get(), 2), new FluidStack(Fluids.EMPTY, 0))
                .ingredients(new ItemStack(PBItems.WINTER_BERRIES.get(), 2), new ItemStack(Items.SUGAR, 3))
                .save(recipeOutput);
        ImbuingCauldronRecipeBuilder.newRecipe(new ItemStack(PBBlocks.RUNE_SLAB_INERT.get()), new FluidStack(Fluids.LAVA, 1000))
                .ingredients(new IngredientWithCount(Ingredient.of(Tags.Items.COBBLESTONES_DEEPSLATE), 3))
                .ingredients(new IngredientWithCount(Ingredient.of(PBTags.ItemTags.HERBS), 2))
                .save(recipeOutput);
        ImbuingCauldronRecipeBuilder.newRecipe(new ItemStack(PBItems.RUNIC_CHARGE.get()), new FluidStack(Fluids.WATER, 500))
                .ingredients(Items.GLASS_BOTTLE)
                .ingredients(new IngredientWithCount(Ingredient.of(PBTags.ItemTags.HERBS), 2))
                .ingredients(new IngredientWithCount(Ingredient.of(Tags.Items.GEMS_AMETHYST), 1))
                .ingredients(new IngredientWithCount(Ingredient.of(Tags.Items.DUSTS_GLOWSTONE), 2))
                .save(recipeOutput);
    }

    private void otherRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.IMBUING_CAULDRON.get())
                .pattern("H H")
                .pattern("#S#")
                .pattern("###")
                .define('H', PBTags.ItemTags.HERBS)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('#', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.HERBALIST_BENCH.get())
                .pattern("P J")
                .pattern("###")
                .pattern("###")
                .define('J', PBBlocks.JAR.get())
                .define('P', PBBlocks.BLACK_THORN_PRESSURE_PLATE.get())
                .define('#', Tags.Items.COBBLESTONES_DEEPSLATE)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.CRANK.get())
                .pattern("  S")
                .pattern("###")
                .pattern("I  ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', PBItems.BLACK_THORN_STICK.get())
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .unlockedBy("has_black_thorn_stick", has(PBItems.BLACK_THORN_STICK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.WINCH.get())
                .pattern("#I#")
                .pattern("#S#")
                .pattern("###")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', PBItems.BLACK_THORN_STICK.get())
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .unlockedBy("has_black_thorn_stick", has(PBItems.BLACK_THORN_STICK.get()))
                .save(recipeOutput);
    }

    private static void blackThornWoodRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_PLANKS.get(), 4)
                .requires(PBBlocks.BLACK_THORN_LOG.get())
                .group("planks")
                .unlockedBy("has_log", has(PBBlocks.BLACK_THORN_LOG.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_SLAB.get(), 3)
                .pattern("###")
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .group("slabs")
                .unlockedBy("has_planks", has(PBBlocks.BLACK_THORN_PLANKS.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .group("stairs")
                .unlockedBy("has_planks", has(PBBlocks.BLACK_THORN_PLANKS.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, PBBlocks.BLACK_THORN_BUTTON.get())
                .requires(PBBlocks.BLACK_THORN_PLANKS.get())
                .group("buttons")
                .unlockedBy("has_planks", has(PBBlocks.BLACK_THORN_PLANKS.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, PBBlocks.BLACK_THORN_PRESSURE_PLATE.get())
                .pattern("##")
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .group("pressure_plates")
                .unlockedBy("has_planks", has(PBBlocks.BLACK_THORN_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', PBBlocks.BLACK_THORN_LOG.get())
                .group("barks")
                .unlockedBy("has_log", has(PBBlocks.BLACK_THORN_LOG.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.STRIPPED_BLACK_THORN_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', PBBlocks.STRIPPED_BLACK_THORN_LOG.get())
                .group("barks")
                .unlockedBy("has_stripped_log", has(PBBlocks.STRIPPED_BLACK_THORN_LOG.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_FENCE.get(), 3)
                .pattern("#S#")
                .pattern("#S#")
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .group("fences")
                .unlockedBy("has_planks", has(PBBlocks.BLACK_THORN_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_FENCE_GATE.get())
                .pattern("S#S")
                .pattern("S#S")
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .group("fence_gates")
                .unlockedBy("has_planks", has(PBBlocks.BLACK_THORN_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_TRAPDOOR.get(), 2)
                .pattern("###")
                .pattern("###")
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .group("trapdoors")
                .unlockedBy("has_planks", has(PBBlocks.BLACK_THORN_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_DOOR.get(), 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .group("doors")
                .unlockedBy("has_planks", has(PBBlocks.BLACK_THORN_PLANKS.get()))
                .save(recipeOutput);
    }
}
