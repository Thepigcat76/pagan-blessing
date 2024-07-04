package com.pigdad.paganbless.compat.jei;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.recipes.BenchCuttingRecipe;
import com.pigdad.paganbless.utils.recipes.RecipeUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HerbalistBenchCategory implements IRecipeCategory<BenchCuttingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(PaganBless.MODID, "bench_cutting");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PaganBless.MODID,
            "textures/gui/herbalist_bench.png");

    public static final RecipeType<BenchCuttingRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, BenchCuttingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public HerbalistBenchCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(PBBlocks.HERBALIST_BENCH.get()));
    }

    @Override
    public @NotNull RecipeType<BenchCuttingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Bench Cutting");
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
    public void setRecipe(IRecipeLayoutBuilder builder, BenchCuttingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 35, 33).addIngredients(RecipeUtils.iWCToIngredientSaveCount(recipe.ingredient()));
        builder.addSlot(RecipeIngredientRole.INPUT, 79, 56).addIngredients(recipe.toolItem());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 33).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(BenchCuttingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Component experienceString = Component.translatable("jei_category.paganbless.bench_cutting.cuts", recipe.cuts());
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        int stringWidth = fontRenderer.width(experienceString);
        guiGraphics.drawString(fontRenderer, experienceString, this.getWidth() - stringWidth - 10, this.getHeight() - 20, 0xFFFFFF, false);
    }
}
