package com.pigdad.paganbless.compat;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import com.pigdad.paganbless.registries.recipes.AnvilSmashingRecipe;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class PBJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(PaganBless.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ImbuingCauldronCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new AnvilSmashingCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new RunicRitualCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ImbuingCauldronRecipe> imbuingRecipe = recipeManager.getAllRecipesFor(ImbuingCauldronRecipe.Type.INSTANCE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(ImbuingCauldronCategory.IMBUING_CAULDRON_TYPE, imbuingRecipe);

        List<AnvilSmashingRecipe> anvilSmashingRecipe = recipeManager.getAllRecipesFor(AnvilSmashingRecipe.Type.INSTANCE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(AnvilSmashingCategory.ANVIL_SMASHING_RECIPE_TYPE, anvilSmashingRecipe);

        List<RunicRitualRecipe> runicRitualRecipe = recipeManager.getAllRecipesFor(RunicRitualRecipe.Type.INSTANCE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(RunicRitualCategory.RUNIC_RITUAL_RECIPE_TYPE, runicRitualRecipe);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(PBBlocks.IMBUING_CAULDRON.get()), ImbuingCauldronCategory.IMBUING_CAULDRON_TYPE);
        registration.addRecipeCatalyst(new ItemStack(Items.ANVIL), AnvilSmashingCategory.ANVIL_SMASHING_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(PBBlocks.RUNIC_CORE.get()), RunicRitualCategory.RUNIC_RITUAL_RECIPE_TYPE);

        for (Item item : BuiltInRegistries.ITEM) {
            if (Block.byItem(item) instanceof RuneSlabBlock runeSlabBlock && !runeSlabBlock.equals(PBBlocks.RUNE_SLAB_INERT.get())) {
                registration.addRecipeCatalyst(new ItemStack(item), RunicRitualCategory.RUNIC_RITUAL_RECIPE_TYPE);
            }
        }
    }
}