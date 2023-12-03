package com.pigdad.pigdadmod.registries.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pigdad.pigdadmod.PigDadMod;
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
import java.util.Arrays;
import java.util.List;

public class AnvilSmashingRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "anvil_smashing";
    private final List<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;

    public AnvilSmashingRecipe(ResourceLocation id, ItemStack output, List<Ingredient> inputItems) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        PigDadMod.LOGGER.info("Constructor for anvil recipe, size: " + inputItems.size());
        for (Ingredient ingredient : inputItems) {
            if (ingredient instanceof IngredientWithCount) {
                PigDadMod.LOGGER.info("Counted ingredient for constructor");
            }
            PigDadMod.LOGGER.info(Arrays.toString(ingredient.getItems()));
        }
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        PigDadMod.LOGGER.info("Inputs: " + Arrays.toString(inputItems.get(0).getItems()) + ", ingredients: " + container.getItem(0));

        for (int i = 0; i < inputItems.size(); i++) {
            if (!inputItems.get(i).test(container.getItem(i))) {
                PigDadMod.LOGGER.info("Test failed!!!");
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

    public List<Ingredient> getInputItems() {
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
                new ResourceLocation(PigDadMod.MODID, NAME);

        @Override
        public @NotNull AnvilSmashingRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            List<Ingredient> inputs = new ArrayList<>();
            RecipeUtils.parseInputs(inputs, json.get("ingredients"));
            for (Ingredient ingredient : inputs) {
                if (ingredient instanceof IngredientWithCount) {
                    PigDadMod.LOGGER.info("Ingredient with count: " + Arrays.toString(ingredient.getItems()));
                }
            }

            return new AnvilSmashingRecipe(id, output, inputs);
        }

        @Override
        public AnvilSmashingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            PigDadMod.LOGGER.info("From network");
            List<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.add(Ingredient.fromNetwork(buf));
            }

            for (Ingredient ingredient : inputs) {
                if (ingredient instanceof IngredientWithCount) {
                    PigDadMod.LOGGER.info("Instance of counted ingredient");
                }
            }

            ItemStack output = buf.readItem();
            return new AnvilSmashingRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AnvilSmashingRecipe recipe) {
            buf.writeInt(recipe.getInputItems().size());

            for (Ingredient ing : recipe.getInputItems()) {
                ing.toNetwork(buf);
            }

            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
