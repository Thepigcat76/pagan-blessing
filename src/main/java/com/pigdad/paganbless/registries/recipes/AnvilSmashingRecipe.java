package com.pigdad.paganbless.registries.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.utils.IngredientWithCount;
import com.pigdad.paganbless.utils.Utils;
import joptsimple.NonOptionArgumentSpec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AnvilSmashingRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "anvil_smashing";

    private final NonNullList<IngredientWithCount> ingredients;
    private final ItemStack output;

    // TODO: rename output to result
    public AnvilSmashingRecipe(NonNullList<IngredientWithCount> ingredients, ItemStack output) {
        this.ingredients = ingredients;
        this.output = output;
    }

    public AnvilSmashingRecipe(List<IngredientWithCount> ingredients, ItemStack output) {
        this.ingredients = NonNullList.create();
        this.ingredients.addAll(ingredients);
        this.output = output;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, Level level) {
        if (level.isClientSide())
            return false;

        Map<Item, Integer> ingredients = new HashMap<>();
        Map<Item, Integer> containerItems = new HashMap<>();

        for (IngredientWithCount ingredient : this.ingredients) {
            ingredients.put(ingredient.ingredient().getItems()[0].getItem(), ingredient.count());
        }

        for (int i = 0; i < container.getContainerSize(); i++) {
            containerItems.put(container.getItem(i).getItem(), container.getItem(i).getCount());
        }

        for (int i = 0; i < ingredients.size(); i++) {
            Item expected = ingredients.keySet().stream().toList().get(i);
            if (containerItems.containsKey(expected) && containerItems.get(expected) >= ingredients.get(expected)) {
                containerItems.remove(expected);
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients1 = NonNullList.create();
        for (int i = 0; i < ingredients.size(); i++) {
            IngredientWithCount ingredient = ingredients.get(i);
            ingredients1.add(i, ingredient.ingredient());
        }
        return ingredients1;
    }

    public @NotNull NonNullList<IngredientWithCount> getIngredientsWithCount() {
        return ingredients;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer simpleContainer, @NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return output.copy();
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
        private static final Codec<AnvilSmashingRecipe> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                IngredientWithCount.CODEC.listOf().fieldOf("ingredients").forGetter((recipe) -> recipe.ingredients.stream().toList()),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output)
        ).apply(builder, AnvilSmashingRecipe::new));

        private Serializer() {
        }

        @Override
        public @NotNull Codec<AnvilSmashingRecipe> codec() {
            return CODEC;
        }


        @Override
        public AnvilSmashingRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            return pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, CODEC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AnvilSmashingRecipe pRecipe) {
            pBuffer.writeWithCodec(NbtOps.INSTANCE, CODEC, pRecipe);
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
