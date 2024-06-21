package com.pigdad.paganbless.registries.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.utils.IngredientWithCount;
import com.pigdad.paganbless.utils.PBRecipeInput;
import com.pigdad.paganbless.utils.RecipeUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ImbuingCauldronRecipe(List<IngredientWithCount> ingredients, ItemStack result, FluidStack fluidStack) implements Recipe<PBRecipeInput> {
    public static final String NAME = "cauldron_imbuing";

    @Override
    public boolean matches(@NotNull PBRecipeInput recipeInput, Level level) {
        if (level.isClientSide()) return false;

        List<ItemStack> inputItems = recipeInput.items().stream().filter(input -> !input.isEmpty()).toList();

        return RecipeUtils.compareItems(inputItems, ingredients);
    }

    public boolean matchesFluid(FluidStack fluidStack, Level level) {
        if (level.isClientSide()) return false;

        return (fluidStack.getAmount() >= this.fluidStack.getAmount() && fluidStack.getFluid().isSame(this.fluidStack.getFluid()));
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return RecipeUtils.listToNonNullList(RecipeUtils.iWCToIngredientsSaveCount(ingredients));
    }

    public @NotNull NonNullList<IngredientWithCount> getIngredientsWithCount() {
        return RecipeUtils.listToNonNullList(ingredients);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull PBRecipeInput recipeInput, HolderLookup.@NotNull Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@Nullable Provider provider) {
        return result.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ImbuingCauldronRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ImbuingCauldronRecipe.Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<ImbuingCauldronRecipe> {
        public static final ImbuingCauldronRecipe.Serializer INSTANCE = new ImbuingCauldronRecipe.Serializer();
        private static final MapCodec<ImbuingCauldronRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                IngredientWithCount.CODEC.listOf().fieldOf("ingredients").forGetter(ImbuingCauldronRecipe::ingredients),
                ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter(ImbuingCauldronRecipe::result),
                FluidStack.OPTIONAL_CODEC.fieldOf("fluid").orElse(FluidStack.EMPTY).forGetter(ImbuingCauldronRecipe::fluidStack)
        ).apply(builder, ImbuingCauldronRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, ImbuingCauldronRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_CODEC.apply(ByteBufCodecs.list()),
                ImbuingCauldronRecipe::ingredients,
                ItemStack.OPTIONAL_STREAM_CODEC,
                ImbuingCauldronRecipe::result,
                FluidStack.OPTIONAL_STREAM_CODEC,
                ImbuingCauldronRecipe::fluidStack,
                ImbuingCauldronRecipe::new
        );

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<ImbuingCauldronRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ImbuingCauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Type implements RecipeType<ImbuingCauldronRecipe> {
        public static final ImbuingCauldronRecipe.Type INSTANCE = new ImbuingCauldronRecipe.Type();

        private Type() {
        }

        @Override
        public String toString() {
            return ImbuingCauldronRecipe.NAME;
        }
    }
}
