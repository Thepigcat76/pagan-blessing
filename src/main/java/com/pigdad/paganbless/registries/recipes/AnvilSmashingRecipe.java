package com.pigdad.paganbless.registries.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AnvilSmashingRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "anvil_smashing";
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;

    public AnvilSmashingRecipe(NonNullList<Ingredient> inputItems, ItemStack output) {
        this.inputItems = inputItems;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        for (int i = 0; i < inputItems.size(); i++) {
            if (!inputItems.get(i).test(container.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<AnvilSmashingRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<AnvilSmashingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final Codec<AnvilSmashingRecipe> CODEC = RecordCodecBuilder.create((p_300831_) -> p_300831_.group(
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap((p_301021_) -> {
                    Ingredient[] aingredient = p_301021_.toArray(Ingredient[]::new);
                    return DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                }, DataResult::success).forGetter(AnvilSmashingRecipe::getIngredients),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("output").forGetter((p_300827_) -> p_300827_.getResultItem(null))
        ).apply(p_300831_, AnvilSmashingRecipe::new));

        @Override
        public @NotNull Codec<AnvilSmashingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull AnvilSmashingRecipe fromNetwork(FriendlyByteBuf buf) {
            int inputSize = buf.readInt();
            List<Ingredient> inputs = new ArrayList<>();

            for (int i = 0; i < inputSize; i++) {
                inputs.add(Ingredient.fromNetwork(buf));
            }

            NonNullList<Ingredient> ingredients = NonNullList.create();
            ingredients.addAll(inputs);

            ItemStack output = buf.readItem();
            return new AnvilSmashingRecipe(ingredients, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AnvilSmashingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }

            buf.writeItem(recipe.getResultItem(null));
        }
    }
}
