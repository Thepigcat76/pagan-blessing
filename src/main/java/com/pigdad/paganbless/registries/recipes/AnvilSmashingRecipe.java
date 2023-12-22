package com.pigdad.paganbless.registries.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.utils.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
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
    private final ResourceLocation id;

    public AnvilSmashingRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> inputItems) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
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
    public @NotNull ResourceLocation getId() {
        return id;
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
        public static final String ID = NAME;
    }

    public static class Serializer implements RecipeSerializer<AnvilSmashingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(PaganBless.MODID, NAME);

        @Override
        public @NotNull AnvilSmashingRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.create();
            RecipeUtils.parseInputs(inputs, json.get("ingredients"));

            return new AnvilSmashingRecipe(id, output, inputs);
        }

        @Override
        public AnvilSmashingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int inputSize = buf.readInt();
            List<Ingredient> inputs = new ArrayList<>();

            for (int i = 0; i < inputSize; i++) {
                inputs.add(Ingredient.fromNetwork(buf));
            }

            NonNullList<Ingredient> ingredients = NonNullList.create();
            ingredients.addAll(inputs);

            ItemStack output = buf.readItem();
            return new AnvilSmashingRecipe(id, output, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AnvilSmashingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }

            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
