package com.pigdad.paganbless.compat;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.recipes.AnvilSmashingRecipe;
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

public class AnvilSmashingCategory  implements IRecipeCategory<AnvilSmashingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PaganBless.MODID, "anvil_smashing");
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaganBless.MODID,
            "textures/gui/anvil_smashing.png");

    public static final RecipeType<AnvilSmashingRecipe> ANVIL_SMASHING_RECIPE_TYPE =
            new RecipeType<>(UID, AnvilSmashingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AnvilSmashingCategory(IGuiHelper helper) {
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
        Vec2[] coordinates = {
                new Vec2(80, 6),
                new Vec2(53, 9),
                new Vec2(106, 9),
        };

        for (int i = 0; i < 3; i++) {
            try {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates[i].x, (int) coordinates[i].y)
                        .addIngredients(recipe.getIngredients().get(i));
            } catch (Exception ignored) {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates[i].x, (int) coordinates[i].y)
                        .addIngredients(Ingredient.EMPTY);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 60).addItemStack(recipe.getResultItem(null));
    }
}