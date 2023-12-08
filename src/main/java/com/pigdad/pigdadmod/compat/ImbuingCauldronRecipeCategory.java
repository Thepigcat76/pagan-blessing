package com.pigdad.pigdadmod.compat;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.ModBlocks;
import com.pigdad.pigdadmod.registries.recipes.ImbuingCauldronRecipe;
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

public class ImbuingCauldronRecipeCategory implements IRecipeCategory<ImbuingCauldronRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PigDadMod.MODID, "cauldron_imbuing");
    public static final ResourceLocation TEXTURE = new ResourceLocation(PigDadMod.MODID,
            "textures/gui/gem_empowering_station_gui.png");

    public static final RecipeType<ImbuingCauldronRecipe> IMBUING_CAULDRON_TYPE =
            new RecipeType<>(UID, ImbuingCauldronRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ImbuingCauldronRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.IMBUING_CAULDRON.get()));
    }


    @Override
    public RecipeType<ImbuingCauldronRecipe> getRecipeType() {
        return IMBUING_CAULDRON_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Gem Infusing Station");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ImbuingCauldronRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 11).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 59).addItemStack(recipe.getResultItem(null));
    }
}
