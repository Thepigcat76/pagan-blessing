package com.pigdad.paganbless.compat;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.ImbuingCauldronBlockEntity;
import com.pigdad.paganbless.registries.recipes.ImbuingCauldronRecipe;
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
    public static final ResourceLocation UID = new ResourceLocation(PaganBless.MODID, "cauldron_imbuing");
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaganBless.MODID,
            "textures/gui/imbuing_cauldron.png");

    public static final RecipeType<ImbuingCauldronRecipe> IMBUING_CAULDRON_TYPE =
            new RecipeType<>(UID, ImbuingCauldronRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ImbuingCauldronRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(PBBlocks.IMBUING_CAULDRON.get()));
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
                new Vec2(76, 5),
                new Vec2(118, 21),
                new Vec2(101, 60),
                new Vec2(50, 60),
                new Vec2(33, 21)
        );

        float scale = 34f / ImbuingCauldronBlockEntity.getCapacity();
        int scaledAmount = (int) (recipe.getFluidStack().getAmount() * scale);

        if (!recipe.getFluidStack().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 148, 34 + (34 - scaledAmount))
                    .addFluidStack(recipe.getFluidStack().getFluid(), recipe.getFluidStack().getAmount())
                    .setFluidRenderer(recipe.getFluidStack().getAmount(), true, 16, scaledAmount);
        }

        for (int i = 0; i < 5; i++) {
            try {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates.get(i).x, (int) coordinates.get(i).y)
                        .addIngredients(recipe.getIngredients().get(i));
            } catch (Exception ignored) {
                builder.addSlot(RecipeIngredientRole.INPUT, (int) coordinates.get(i).x, (int) coordinates.get(i).y)
                        .addIngredients(Ingredient.EMPTY);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 38).addItemStack(recipe.getResultItem(null));
    }
}
