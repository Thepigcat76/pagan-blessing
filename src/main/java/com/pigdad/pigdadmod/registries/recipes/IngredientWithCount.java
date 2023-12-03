package com.pigdad.pigdadmod.registries.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IngredientWithCount extends AbstractIngredient {
    private final Ingredient ingredient;
    private final int count;

    public IngredientWithCount(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public ItemStack @NotNull [] getItems() {
        for (ItemStack stack : ingredient.getItems()) {
            stack.setCount(count);
        }
        return ingredient.getItems();
    }

    @Override
    public boolean test(@Nullable ItemStack itemStack) {
        return itemStack != null && ingredient.test(itemStack) && itemStack.getCount() >= count;
    }

    @Override
    public @NotNull IntList getStackingIds() {
        return ingredient.getStackingIds();
    }

    @Override
    public boolean isEmpty() {
        return ingredient.isEmpty();
    }

    @Override
    public boolean isSimple() {
        return ingredient.isSimple();
    }

    @Override
    public @NotNull IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull JsonElement toJson() {
        return ingredient.toJson();
    }

    public static class Serializer implements IIngredientSerializer<IngredientWithCount> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public @NotNull IngredientWithCount parse(@NotNull FriendlyByteBuf buffer) {
            return new IngredientWithCount(Ingredient.fromNetwork(buffer), buffer.readVarInt());
        }

        @Override
        public @NotNull IngredientWithCount parse(@NotNull JsonObject json) {
            throw new JsonSyntaxException("IngredientWithCount should not be parsed from JSON using the serializer, if you are a modder, use RecipeJsonUtils instead!");
        }

        @Override
        public void write(@NotNull FriendlyByteBuf buffer, IngredientWithCount ingredient) {
            ingredient.ingredient.toNetwork(buffer);
            buffer.writeVarInt(ingredient.count);
        }

    }
}
