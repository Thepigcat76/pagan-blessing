package com.pigdad.pigdadmod.compat;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.ModBlocks;
import com.pigdad.pigdadmod.registries.ModItems;
import com.pigdad.pigdadmod.registries.recipes.AnvilSmashingRecipe;
import com.pigdad.pigdadmod.registries.recipes.ImbuingCauldronRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    public static RecipeType<ImbuingCauldronRecipe> IMBUING_TYPE =
            new RecipeType<>(ImbuingCauldronRecipeCategory.UID, ImbuingCauldronRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(PigDadMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ImbuingCauldronRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ImbuingCauldronRecipe> imbuingRecipe = recipeManager.getAllRecipesFor(ImbuingCauldronRecipe.Type.INSTANCE);
        registration.addRecipes(ImbuingCauldronRecipeCategory.IMBUING_CAULDRON_TYPE, imbuingRecipe);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.IMBUING_CAULDRON.get()), IMBUING_TYPE);
    }
}
