package com.pigdad.paganbless.compat.jei;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.registries.blocks.BaseHangingHerbBlock;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import com.pigdad.paganbless.registries.recipes.AnvilSmashingRecipe;
import com.pigdad.paganbless.registries.recipes.BenchCuttingRecipe;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@JeiPlugin
public class PBJeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        PaganBless.LOGGER.debug("Loaded!!");
        return ResourceLocation.fromNamespaceAndPath(PaganBless.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ImbuingCauldronCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new AnvilSmashingCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new RunicRitualCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new HerbalistBenchCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ImbuingCauldronRecipe> imbuingRecipe = recipeManager.getAllRecipesFor(ImbuingCauldronRecipe.Type.INSTANCE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(ImbuingCauldronCategory.RECIPE_TYPE, imbuingRecipe);

        List<AnvilSmashingRecipe> anvilSmashingRecipe = recipeManager.getAllRecipesFor(AnvilSmashingRecipe.Type.INSTANCE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(AnvilSmashingCategory.RECIPE_TYPE, anvilSmashingRecipe);

        List<RunicRitualRecipe> runicRitualRecipe = recipeManager.getAllRecipesFor(RunicRitualRecipe.Type.INSTANCE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(RunicRitualCategory.RECIPE_TYPE, runicRitualRecipe);

        List<BenchCuttingRecipe> herbalistBenchRecipes = recipeManager.getAllRecipesFor(BenchCuttingRecipe.Type.INSTANCE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(HerbalistBenchCategory.RECIPE_TYPE, herbalistBenchRecipes);

        registration.addIngredientInfo(gatherHangingHerbs(), VanillaTypes.ITEM_STACK, Component.translatable("jei_info.paganbless.dried_herbs_info"));
        registration.addIngredientInfo(new ItemStack(Items.SKELETON_SKULL), VanillaTypes.ITEM_STACK, Component.translatable("jei_info.paganbless.skeleton_skull"));
        registration.addIngredientInfo(new ItemStack(PBItems.CINNABAR.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_info.paganbless.cinnabar"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(PBBlocks.HERBALIST_BENCH.get()), HerbalistBenchCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(PBBlocks.IMBUING_CAULDRON.get()), ImbuingCauldronCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(PBBlocks.RUNIC_CORE.get()), RunicRitualCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(Items.ANVIL), AnvilSmashingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(PBBlocks.WINCH.get()), AnvilSmashingCategory.RECIPE_TYPE);

        for (Item item : BuiltInRegistries.ITEM) {
            if (Block.byItem(item) instanceof RuneSlabBlock runeSlabBlock && !runeSlabBlock.equals(PBBlocks.RUNE_SLAB_INERT.get())) {
                registration.addRecipeCatalyst(new ItemStack(item), RunicRitualCategory.RECIPE_TYPE);
            }
        }
    }

    private static List<ItemStack> gatherHangingHerbs() {
        return BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof BaseHangingHerbBlock).map(block -> block.asItem().getDefaultInstance()).toList();
    }
}