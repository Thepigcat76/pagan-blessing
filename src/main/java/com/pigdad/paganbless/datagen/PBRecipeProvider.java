package com.pigdad.paganbless.datagen;

import com.pigdad.paganbless.datagen.recipe_builder.AnvilSmashingRecipeBuilder;
import com.pigdad.paganbless.datagen.recipe_builder.BenchCuttingRecipeBuilder;
import com.pigdad.paganbless.datagen.recipe_builder.ImbuingCauldronRecipeBuilder;
import com.pigdad.paganbless.datagen.recipe_builder.RunicRitualRecipeBuilder;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.content.items.PentacleItem;
import com.pigdad.paganbless.utils.recipes.IngredientWithCount;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

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
        BenchCuttingRecipeBuilder.newRecipe(new ItemStack(PBItems.BLACK_THORN_STICK.get(), 3), SizedIngredient.of(PBBlocks.BLACK_THORN_LOG.get(), 1),
                        Ingredient.of(ItemTags.AXES), 5, true)
                .save(recipeOutput);
        BenchCuttingRecipeBuilder.newRecipe(new ItemStack(PBItems.CHOPPED_RUE.get()), SizedIngredient.of(PBBlocks.DRIED_HANGING_RUE.get(), 1),
                        Ingredient.of(PBItems.BOLINE.get()), 3, true)
                .save(recipeOutput);
        BenchCuttingRecipeBuilder.newRecipe(new ItemStack(PBItems.CHOPPED_LAVENDER.get()), SizedIngredient.of(PBBlocks.DRIED_HANGING_LAVENDER.get(), 1),
                        Ingredient.of(PBItems.BOLINE.get()), 3, true)
                .save(recipeOutput);
        BenchCuttingRecipeBuilder.newRecipe(new ItemStack(PBBlocks.EMPTY_INCENSE.get()), SizedIngredient.of(Items.SKELETON_SKULL, 1),
                        Ingredient.of(PBItems.BOLINE.get()), 9, true)
                .save(recipeOutput);
    }

    private void anvilSmashingRecipes(RecipeOutput recipeOutput) {
        AnvilSmashingRecipeBuilder.newRecipe(new ItemStack(Items.REDSTONE, 15),
                        SizedIngredient.of(PBTags.ItemTags.GEMS_CINNABAR, 4))
                .save(recipeOutput);
        AnvilSmashingRecipeBuilder.newRecipe(PBBlocks.RUNIC_CORE.get().asItem().getDefaultInstance(),
                        SizedIngredient.of(PBBlocks.BLACK_THORN_LOG.get(), 1),
                        SizedIngredient.of(Items.SKELETON_SKULL, 1),
                        SizedIngredient.of(ItemTags.CANDLES, 3))
                .save(recipeOutput);
        AnvilSmashingRecipeBuilder.newRecipe(PBItems.MECHANICAL_COMPONENT.get().getDefaultInstance(),
                        SizedIngredient.of(PBItems.ESSENCE_OF_THE_FOREST.get(), 1),
                        SizedIngredient.of(Tags.Items.INGOTS_IRON, 2),
                        SizedIngredient.of(PBItems.BLACK_THORN_STICK.get(), 2))
                .save(recipeOutput);

        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_CINNABAR.get(), PBTags.ItemTags.GEMS_CINNABAR, 2);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_DIAMOND.get(), Tags.Items.GEMS_DIAMOND, 1);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_EMERALD.get(), Tags.Items.GEMS_EMERALD, 1);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_LAPIS.get(), Tags.Items.GEMS_LAPIS, 5);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_QUARTZ.get(), Tags.Items.GEMS_QUARTZ, 3);
        runeSlabRecipe(recipeOutput, PBBlocks.RUNE_SLAB_AMETHYST.get(), Tags.Items.GEMS_AMETHYST, 2);
    }

    private void runeSlabRecipe(RecipeOutput recipeOutput, Block runeSlabBlock, TagKey<Item> gemItem, int gemCount) {
        AnvilSmashingRecipeBuilder.newRecipe(runeSlabBlock.asItem().getDefaultInstance(),
                        SizedIngredient.of(gemItem, gemCount),
                        SizedIngredient.of(PBBlocks.RUNE_SLAB_INERT.get(), 1))
                .save(recipeOutput);
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
        ImbuingCauldronRecipeBuilder.newRecipe(new ItemStack(PBItems.GLAZED_BERRIES.get(), 2))
                .ingredients(new ItemStack(PBItems.WINTER_BERRIES.get(), 2), new ItemStack(Items.SUGAR, 3))
                .save(recipeOutput);
        ImbuingCauldronRecipeBuilder.newRecipe(new ItemStack(PBBlocks.RUNE_SLAB_INERT.get()), SizedFluidIngredient.of(FluidTags.LAVA, 1000))
                .ingredients(SizedIngredient.of(Tags.Items.COBBLESTONES_DEEPSLATE, 3))
                .ingredients(SizedIngredient.of(PBTags.ItemTags.HERBS, 2))
                .save(recipeOutput);
        ImbuingCauldronRecipeBuilder.newRecipe(new ItemStack(PBItems.RUNIC_CHARGE.get()), SizedFluidIngredient.of(FluidTags.WATER, 500))
                .ingredients(Items.GLASS_BOTTLE)
                .ingredients(SizedIngredient.of(PBTags.ItemTags.HERBS, 2))
                .ingredients(SizedIngredient.of(Tags.Items.GEMS_AMETHYST, 1))
                .ingredients(SizedIngredient.of(PBItems.CINNABAR.get(), 1))
                .ingredients(SizedIngredient.of(PBItems.ESSENCE_OF_THE_FOREST.get(), 2))
                .save(recipeOutput);
    }

    private void otherRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.IMBUING_CAULDRON.get())
                .pattern("#S#")
                .pattern("###")
                .define('S', PBItems.BLACK_THORN_STICK.get())
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
                .pattern("M  ")
                .define('M', PBItems.MECHANICAL_COMPONENT.get())
                .define('S', PBItems.BLACK_THORN_STICK.get())
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .unlockedBy("has_black_thorn_stick", has(PBItems.BLACK_THORN_STICK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.WINCH.get())
                .pattern("#M#")
                .pattern("#S#")
                .pattern("###")
                .define('M', PBItems.MECHANICAL_COMPONENT.get())
                .define('S', PBItems.BLACK_THORN_STICK.get())
                .define('#', PBBlocks.BLACK_THORN_PLANKS.get())
                .unlockedBy("has_black_thorn_stick", has(PBItems.BLACK_THORN_STICK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.JAR.get())
                .pattern("#L#")
                .pattern("# #")
                .pattern("###")
                .define('L', PBBlocks.BLACK_THORN_LOG.get())
                .define('#', Tags.Items.GLASS_PANES)
                .unlockedBy("has_black_thorn_log", has(PBBlocks.BLACK_THORN_LOG.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, PBItems.BLACK_THORN_STAFF.get())
                .pattern("  #")
                .pattern(" S ")
                .pattern("S  ")
                .define('S', PBItems.BLACK_THORN_STICK.get())
                .define('#', PBItems.ESSENCE_OF_THE_FOREST.get())
                .unlockedBy("has_black_thorn_stick", has(PBItems.BLACK_THORN_STICK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.ROPE.get(), 2)
                .pattern("S")
                .pattern("H")
                .pattern("S")
                .define('S', Tags.Items.STRINGS)
                .define('H', PBTags.ItemTags.ROPE_HERBS)
                .unlockedBy("has_herbs", has(PBTags.ItemTags.ROPE_HERBS))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PBItems.HERB_POUCH.get())
                .pattern("#S#")
                .pattern("#H#")
                .pattern("###")
                .define('S', Tags.Items.STRINGS)
                .define('H', PBTags.ItemTags.HERBS)
                .define('#', ItemTags.WOOL)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, PBItems.BOLINE.get())
                .pattern("I")
                .pattern("S")
                .pattern("B")
                .define('S', PBItems.BLACK_THORN_STICK.get())
                .define('B', Tags.Items.BONES)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.HANGING_LAVENDER.get())
                .pattern(" R ")
                .pattern("HHH")
                .define('R', PBBlocks.ROPE.get())
                .define('H', PBItems.LAVENDER.get())
                .unlockedBy("has_rope", has(PBBlocks.ROPE.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBBlocks.HANGING_RUE.get())
                .pattern(" R ")
                .pattern("HHH")
                .define('R', PBBlocks.ROPE.get())
                .define('H', PBItems.RUE.get())
                .unlockedBy("has_rope", has(PBBlocks.ROPE.get()))
                .save(recipeOutput);

        if (PBItems.PAGAN_GUIDE != null) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PBItems.PAGAN_GUIDE.get())
                    .requires(PBTags.ItemTags.HERBS)
                    .requires(Items.BOOK)
                    .group("books")
                    .unlockedBy("has_book", has(Items.BOOK))
                    .save(recipeOutput);
        }
    }

    private static void blackThornWoodRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_PLANKS.get(), 4)
                .requires(PBBlocks.BLACK_THORN_LOG.get())
                .group("planks")
                .unlockedBy("has_log", has(PBBlocks.BLACK_THORN_LOG.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PBBlocks.BLACK_THORN_SLAB.get(), 6)
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
