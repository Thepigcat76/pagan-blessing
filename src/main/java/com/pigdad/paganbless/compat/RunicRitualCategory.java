package com.pigdad.paganbless.compat;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class RunicRitualCategory  implements IRecipeCategory<RunicRitualRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PaganBless.MODID, "runic_ritual");
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaganBless.MODID,
            "textures/gui/runic_ritual.png");

    public static final RecipeType<RunicRitualRecipe> RUNIC_RITUAL_RECIPE_TYPE =
            new RecipeType<>(UID, RunicRitualRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public RunicRitualCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(PBBlocks.RUNIC_CORE.get()));
    }

    @Override
    public RecipeType<RunicRitualRecipe> getRecipeType() {
        return RUNIC_RITUAL_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Runic Ritual");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RunicRitualRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 104, 8)
                .addIngredients(Ingredient.of(recipe.getRuneBlock()));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 8)
                .addIngredients(Ingredient.of(recipe.getRuneBlock()));
        builder.addSlot(RecipeIngredientRole.INPUT, 123, 33)
                .addIngredients(Ingredient.of(recipe.getRuneBlock()));
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 33)
                .addIngredients(Ingredient.of(recipe.getRuneBlock()));
        builder.addSlot(RecipeIngredientRole.INPUT, 105, 57)
                .addIngredients(Ingredient.of(recipe.getRuneBlock()));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 57)
                .addIngredients(Ingredient.of(recipe.getRuneBlock()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 37).addItemStack(recipe.getResultItem(null));
    }
}
