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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec2;

import java.util.List;

public class ImbuingCauldronRecipeCategory implements IRecipeCategory<ImbuingCauldronRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PigDadMod.MODID, "cauldron_imbuing");
    public static final ResourceLocation TEXTURE = new ResourceLocation(PigDadMod.MODID,
            "textures/gui/imbuing_cauldron.png");

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
        return Component.literal("Cauldron Imbuing");
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
        List<Vec2> coordinates = List.of(
                new Vec2(80, 5),
                new Vec2(100, 11),
                new Vec2(120, 11),
                new Vec2(140, 11),
                new Vec2(160, 11)
        );

        for (int i = 0; i < 5; i++) {
            try {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates.get(i).x, (int) coordinates.get(i).y)
                        .addIngredients(recipe.getIngredients().get(i));
            } catch (Exception ignored) {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates.get(i).x, (int) coordinates.get(i).y)
                        .addIngredients(Ingredient.EMPTY);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 36).addItemStack(recipe.getResultItem(null));
    }
}
