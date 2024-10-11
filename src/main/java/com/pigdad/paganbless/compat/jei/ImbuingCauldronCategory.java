package com.pigdad.paganbless.compat.jei;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.content.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.paganbless.content.recipes.ImbuingCauldronRecipe;
import com.pigdad.paganbless.utils.recipes.RecipeUtils;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImbuingCauldronCategory  implements IRecipeCategory<ImbuingCauldronRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(PaganBless.MODID, "cauldron_imbuing");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PaganBless.MODID,
            "textures/gui/imbuing_cauldron.png");

    public static final RecipeType<ImbuingCauldronRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, ImbuingCauldronRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ImbuingCauldronCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(PBBlocks.IMBUING_CAULDRON.get()));
    }


    @Override
    public @NotNull RecipeType<ImbuingCauldronRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Cauldron Imbuing");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ImbuingCauldronRecipe recipe, IFocusGroup focuses) {
        List<Vec2> coordinates = List.of(
                new Vec2(76, 6),
                new Vec2(119, 20),
                new Vec2(102, 59),
                new Vec2(49, 59),
                new Vec2(32, 20)
        );

        float scale = 34f / ImbuingCauldronBlockEntity.getCapacity();
        int scaledAmount = (int) (recipe.fluidStack().getAmount() * scale);

        if (!recipe.fluidStack().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 148, 34 + (34 - scaledAmount))
                    .addFluidStack(recipe.fluidStack().getFluid(), recipe.fluidStack().getAmount())
                    .setFluidRenderer(recipe.fluidStack().getAmount(), true, 16, scaledAmount);
        }

        for (int i = 0; i < 5; i++) {
            try {
                Ingredient ingredient = RecipeUtils.iWCToIngredientsSaveCount(recipe.ingredients()).get(i);
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates.get(i).x, (int) coordinates.get(i).y)
                        .addIngredients(ingredient);
            } catch (Exception ignored) {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates.get(i).x, (int) coordinates.get(i).y)
                        .addIngredients(Ingredient.EMPTY);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 38).addItemStack(recipe.getResultItem(null));
    }
}