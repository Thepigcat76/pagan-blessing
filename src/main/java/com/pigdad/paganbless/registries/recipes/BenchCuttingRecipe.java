package com.pigdad.paganbless.registries.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.utils.IngredientWithCount;
import com.pigdad.paganbless.utils.PBRecipeInput;
import com.pigdad.paganbless.utils.RecipeUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record BenchCuttingRecipe(IngredientWithCount ingredient, int cuts, boolean tryDamage, Ingredient toolItem, ItemStack resultStack) implements Recipe<PBRecipeInput> {
    public static final String NAME = "bench_cutting";

    @Override
    public boolean matches(PBRecipeInput pbRecipeInput, Level level) {
        return ingredient.test(pbRecipeInput.getItem(0)) && toolItem.test(pbRecipeInput.getItem(1));
    }

    @Override
    public ItemStack assemble(PBRecipeInput pbRecipeInput, HolderLookup.Provider provider) {
        return resultStack.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return resultStack.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return RecipeUtils.listToNonNullList(RecipeUtils.iWCToIngredients(NonNullList.of(ingredient)));
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<BenchCuttingRecipe> {
        public static final BenchCuttingRecipe.Serializer INSTANCE = new BenchCuttingRecipe.Serializer();
        private static final MapCodec<BenchCuttingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                IngredientWithCount.CODEC.fieldOf("ingredient").forGetter(BenchCuttingRecipe::ingredient),
                ExtraCodecs.POSITIVE_INT.fieldOf("cuts").forGetter(BenchCuttingRecipe::cuts),
                Codec.BOOL.fieldOf("try_damage").forGetter(BenchCuttingRecipe::tryDamage),
                Ingredient.CODEC_NONEMPTY.fieldOf("tool").forGetter(BenchCuttingRecipe::toolItem),
                ItemStack.CODEC.fieldOf("result").forGetter(BenchCuttingRecipe::resultStack)
        ).apply(builder, BenchCuttingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, BenchCuttingRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_CODEC,
                BenchCuttingRecipe::ingredient,
                ByteBufCodecs.INT,
                BenchCuttingRecipe::cuts,
                ByteBufCodecs.BOOL,
                BenchCuttingRecipe::tryDamage,
                Ingredient.CONTENTS_STREAM_CODEC,
                BenchCuttingRecipe::toolItem,
                ItemStack.STREAM_CODEC,
                BenchCuttingRecipe::resultStack,
                BenchCuttingRecipe::new
        );

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<BenchCuttingRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, BenchCuttingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Type implements RecipeType<BenchCuttingRecipe> {
        public static final BenchCuttingRecipe.Type INSTANCE = new BenchCuttingRecipe.Type();

        private Type() {
        }

        @Override
        public String toString() {
            return BenchCuttingRecipe.NAME;
        }
    }
}
