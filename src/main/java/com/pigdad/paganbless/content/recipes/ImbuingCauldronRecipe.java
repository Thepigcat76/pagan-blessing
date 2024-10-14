package com.pigdad.paganbless.content.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.utils.recipes.IngredientWithCount;
import com.pigdad.paganbless.utils.recipes.PBFluidRecipeInput;
import com.pigdad.paganbless.utils.recipes.RecipeUtils;
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
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record ImbuingCauldronRecipe(List<SizedIngredient> ingredients, ItemStack result, Optional<SizedFluidIngredient> fluidIngredient) implements Recipe<PBFluidRecipeInput> {
    public static final String NAME = "cauldron_imbuing";

    @Override
    public boolean matches(@NotNull PBFluidRecipeInput recipeInput, Level level) {
        if (level.isClientSide()) return false;

        List<ItemStack> inputItems = recipeInput.items().stream().filter(input -> !input.isEmpty()).toList();

        boolean fluidMatches = fluidIngredient.isEmpty() || fluidIngredient.get().test(recipeInput.fluidStack());
        boolean itemsMatches = RecipeUtils.compareItems(inputItems, ingredients);
        return itemsMatches && fluidMatches;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return RecipeUtils.listToNonNullList(RecipeUtils.iWCToIngredientsSaveCount(ingredients));
    }

    public @NotNull NonNullList<SizedIngredient> getIngredientsWithCount() {
        return RecipeUtils.listToNonNullList(ingredients);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull PBFluidRecipeInput recipeInput, HolderLookup.@NotNull Provider provider) {
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
                SizedIngredient.FLAT_CODEC.listOf().fieldOf("ingredients").forGetter(ImbuingCauldronRecipe::ingredients),
                ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter(ImbuingCauldronRecipe::result),
                SizedFluidIngredient.FLAT_CODEC.optionalFieldOf("fluid").forGetter(ImbuingCauldronRecipe::fluidIngredient)
        ).apply(builder, ImbuingCauldronRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, ImbuingCauldronRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
                ImbuingCauldronRecipe::ingredients,
                ItemStack.OPTIONAL_STREAM_CODEC,
                ImbuingCauldronRecipe::result,
                ByteBufCodecs.optional(SizedFluidIngredient.STREAM_CODEC),
                ImbuingCauldronRecipe::fluidIngredient,
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
