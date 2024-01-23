package com.pigdad.paganbless.registries.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.utils.IngredientWithCount;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImbuingCauldronRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "cauldron_imbuing";

    private final NonNullList<IngredientWithCount> ingredients;
    private final ItemStack output;
    private final FluidStack fluid;

    // TODO: rename output to result
    public ImbuingCauldronRecipe(NonNullList<IngredientWithCount> ingredients, ItemStack output, FluidStack fluid) {
        this.ingredients = ingredients;
        this.output = output;
        this.fluid = fluid;
    }

    public ImbuingCauldronRecipe(List<IngredientWithCount> ingredients, ItemStack output, FluidStack fluid) {
        this.ingredients = NonNullList.create();
        this.ingredients.addAll(ingredients);
        this.output = output;
        this.fluid = fluid;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;

        List<ItemStack> containerItems = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            containerItems.add(container.getItem(i));
        }

        List<Boolean> checked = new ArrayList<>();

        for (IngredientWithCount inputItem : ingredients) {
            for (ItemStack item : containerItems) {
                if (inputItem.ingredient().test(item) && item.getCount() >= inputItem.count()) {
                    checked.add(true);
                    break;
                }
            }
        }

        // Check if all input items match
        return checked.stream().allMatch(Boolean::booleanValue) && checked.size() == ingredients.size();
    }

    public boolean matchesFluid(FluidStack fluidStack, Level level) {
        if (level.isClientSide()) return false;

        return (fluidStack.getAmount() >= this.fluid.getAmount() && fluidStack.getFluid().isSame(this.fluid.getFluid()));
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

    public FluidStack getFluid() {
        return fluid;
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
        return ImbuingCauldronRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ImbuingCauldronRecipe.Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<ImbuingCauldronRecipe> {
        public static final ImbuingCauldronRecipe.Serializer INSTANCE = new ImbuingCauldronRecipe.Serializer();
        private static final Codec<ImbuingCauldronRecipe> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                IngredientWithCount.CODEC.listOf().fieldOf("ingredients").forGetter((recipe) -> recipe.ingredients.stream().toList()),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                FluidStack.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid)
        ).apply(builder, ImbuingCauldronRecipe::new));

        private Serializer() {
        }

        @Override
        public @NotNull Codec<ImbuingCauldronRecipe> codec() {
            return CODEC;
        }


        @Override
        public ImbuingCauldronRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            return pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, CODEC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ImbuingCauldronRecipe pRecipe) {
            pBuffer.writeWithCodec(NbtOps.INSTANCE, CODEC, pRecipe);
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
