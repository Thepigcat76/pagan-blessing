package com.pigdad.paganbless.registries.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.utils.IngredientWithCount;
import com.pigdad.paganbless.utils.RecipeUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record ImbuingCauldronRecipe(List<IngredientWithCount> ingredients, ItemStack result, FluidStack fluidStack) implements Recipe<SimpleContainer> {
    public static final String NAME = "cauldron_imbuing";

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

        return (fluidStack.getAmount() >= this.fluidStack.getAmount() && fluidStack.getFluid().isSame(this.fluidStack.getFluid()));
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
        return RecipeUtils.listToNonNullList(ingredients);
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, HolderLookup.Provider provider) {
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
                FluidStack.OPTIONAL_CODEC.fieldOf("fluid").forGetter(ImbuingCauldronRecipe::fluidStack)
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
