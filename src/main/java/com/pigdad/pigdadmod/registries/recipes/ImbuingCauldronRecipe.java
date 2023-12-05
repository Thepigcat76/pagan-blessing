package com.pigdad.pigdadmod.registries.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.utils.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImbuingCauldronRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "cauldron_imbuing";
    private final List<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;

    public ImbuingCauldronRecipe(ResourceLocation id, ItemStack output, List<Ingredient> inputItems) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        if (inputItems.size() > 5) {
            throw new IllegalStateException("Amount of input items for " + id + " is too high. Maximum: 5, found: "+inputItems.size());
        }
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        List<ItemStack> containerItems = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            containerItems.add(container.getItem(i));
        }

        List<Boolean> checked = new ArrayList<>();

        for (Ingredient inputItem : inputItems) {
            for (ItemStack item : containerItems) {
                if (inputItem.test(item)) {
                    checked.add(true);
                    break;
                }
            }
        }

        // Check if all input items match
        return checked.stream().allMatch(Boolean::booleanValue) && checked.size() == inputItems.size();
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

    public static class Type implements RecipeType<ImbuingCauldronRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = NAME;
    }

    public static class Serializer implements RecipeSerializer<ImbuingCauldronRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(PigDadMod.MODID, NAME);

        @Override
        public @NotNull ImbuingCauldronRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            List<Ingredient> inputs = new ArrayList<>();
            RecipeUtils.parseInputs(inputs, json.get("ingredients"));

            return new ImbuingCauldronRecipe(id, output, inputs);
        }

        @Override
        public ImbuingCauldronRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            List<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.add(Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new ImbuingCauldronRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ImbuingCauldronRecipe recipe) {
            buf.writeInt(recipe.getInputItems().size());

            for (Ingredient ing : recipe.getInputItems()) {
                ing.toNetwork(buf);
            }

            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
