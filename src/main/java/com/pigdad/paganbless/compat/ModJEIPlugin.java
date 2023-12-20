package com.pigdad.paganbless.compat;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    public static RecipeType<ImbuingCauldronRecipe> IMBUING_TYPE =
            new RecipeType<>(ImbuingCauldronRecipeCategory.UID, ImbuingCauldronRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(PaganBless.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ImbuingCauldronRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new AnvilSmashingRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new RunicRitualRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ImbuingCauldronRecipe> imbuingRecipe = recipeManager.getAllRecipesFor(ImbuingCauldronRecipe.Type.INSTANCE);
        registration.addRecipes(ImbuingCauldronRecipeCategory.IMBUING_CAULDRON_TYPE, imbuingRecipe);

        List<AnvilSmashingRecipe> anvilSmashingRecipe = recipeManager.getAllRecipesFor(AnvilSmashingRecipe.Type.INSTANCE);
        registration.addRecipes(AnvilSmashingRecipeCategory.ANVIL_SMASHING_RECIPE_TYPE, anvilSmashingRecipe);

        List<RunicRitualRecipe> runicRitualRecipe = recipeManager.getAllRecipesFor(RunicRitualRecipe.Type.INSTANCE);
        registration.addRecipes(RunicRitualRecipeCategory.RUNIC_RITUAL_RECIPE_TYPE, runicRitualRecipe);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(PBBlocks.IMBUING_CAULDRON.get()), ImbuingCauldronRecipeCategory.IMBUING_CAULDRON_TYPE);
        registration.addRecipeCatalyst(new ItemStack(Items.ANVIL), AnvilSmashingRecipeCategory.ANVIL_SMASHING_RECIPE_TYPE);
    }
}
