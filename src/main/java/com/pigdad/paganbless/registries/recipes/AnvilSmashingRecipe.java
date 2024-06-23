package com.pigdad.paganbless.registries.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.utils.recipes.IngredientWithCount;
import com.pigdad.paganbless.utils.recipes.PBRecipeInput;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record AnvilSmashingRecipe(NonNullList<IngredientWithCount> ingredients, ItemStack result) implements Recipe<PBRecipeInput> {
    public static final String NAME = "anvil_smashing";

    public AnvilSmashingRecipe(List<IngredientWithCount> ingredients, ItemStack result) {
        this(RecipeUtils.listToNonNullList(ingredients), result);
    }

    @Override
    public boolean matches(@NotNull PBRecipeInput recipeInput, Level level) {
        if (level.isClientSide()) return false;

        List<ItemStack> inputItems = recipeInput.items();

        return RecipeUtils.compareItems(inputItems, ingredients);
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients1 = NonNullList.create();
        for (int i = 0; i < ingredients.size(); i++) {
            IngredientWithCount ingredient = ingredients.get(i);

            ingredients1.add(i, ingredient.ingredient());
            ItemStack[] items = ingredient.ingredient().getItems();

            for (int j = 0; j < items.length; j++) {
                ItemStack itemStack = items[j];
                itemStack.setCount(ingredient.count());
                ingredients1.get(i).getItems()[j] = itemStack;
            }
        }
        return ingredients1;
    }

    public @NotNull NonNullList<IngredientWithCount> getIngredientsWithCount() {
        return ingredients;
    }

    @Override
    public @NotNull ItemStack assemble(PBRecipeInput recipeInput, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<AnvilSmashingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final MapCodec<AnvilSmashingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                IngredientWithCount.CODEC.listOf().fieldOf("ingredients").forGetter(AnvilSmashingRecipe::ingredients),
                ItemStack.CODEC.fieldOf("result").forGetter(AnvilSmashingRecipe::result)
        ).apply(builder, AnvilSmashingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, AnvilSmashingRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_CODEC.apply(ByteBufCodecs.list()),
                AnvilSmashingRecipe::ingredients,
                ItemStack.STREAM_CODEC,
                AnvilSmashingRecipe::result,
                AnvilSmashingRecipe::new
        );

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<AnvilSmashingRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, AnvilSmashingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Type implements RecipeType<AnvilSmashingRecipe> {
        public static final Type INSTANCE = new Type();

        private Type() {
        }

        @Override
        public String toString() {
            return AnvilSmashingRecipe.NAME;
        }
    }
}
