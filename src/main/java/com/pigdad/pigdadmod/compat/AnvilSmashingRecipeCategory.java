package com.pigdad.pigdadmod.compat;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.ModBlocks;
import com.pigdad.pigdadmod.registries.recipes.AnvilSmashingRecipe;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec2;

import java.util.List;

public class AnvilSmashingRecipeCategory implements IRecipeCategory<AnvilSmashingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PigDadMod.MODID, "anvil_smashing");
    public static final ResourceLocation TEXTURE = new ResourceLocation(PigDadMod.MODID,
            "textures/gui/gem_empowering_station_gui.png");

    public static final RecipeType<AnvilSmashingRecipe> ANVIL_SMASHING_RECIPE_TYPE =
            new RecipeType<>(UID, AnvilSmashingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AnvilSmashingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.ANVIL));
    }


    @Override
    public RecipeType<AnvilSmashingRecipe> getRecipeType() {
        return ANVIL_SMASHING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Anvil Smashing");
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
    public void setRecipe(IRecipeLayoutBuilder builder, AnvilSmashingRecipe recipe, IFocusGroup focuses) {
        List<Vec2> coordinates = List.of(
                new Vec2(80, 11),
                new Vec2(100, 11),
                new Vec2(120, 11),
                new Vec2(140, 11),
                new Vec2(160, 11)
        );

        for (int i = 0; i < recipe.getInputItems().size(); i++) {
            try {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates.get(i).x, (int) coordinates.get(i).y)
                        .addIngredients(recipe.getIngredients().get(i));
            } catch (Exception ignored) {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates.get(i).x, (int) coordinates.get(i).y)
                        .addIngredients(Ingredient.EMPTY);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 59).addItemStack(recipe.getResultItem(null));
    }
}
