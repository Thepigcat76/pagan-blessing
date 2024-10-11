package com.pigdad.paganbless.content.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigdad.paganbless.utils.recipes.PBRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public record RunicRitualRecipe(ItemStack result, ItemStack runeBlock) implements Recipe<PBRecipeInput> {
    public static final String NAME = "runic_ritual";

    @Override
    public boolean matches(PBRecipeInput recipeInput, Level level) {
        return true;
    }

    public boolean matchesRunes(Item block, Level level) {
        if (level.isClientSide()) return false;

        return runeBlock.is(block);
    }

    public Block getRuneBlock() {
        return Block.byItem(runeBlock.getItem());
    }

    @Override
    public @NotNull ItemStack assemble(PBRecipeInput recipeInput, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RunicRitualRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RunicRitualRecipe.Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<RunicRitualRecipe> {
        public static final RunicRitualRecipe.Serializer INSTANCE = new RunicRitualRecipe.Serializer();
        private static final MapCodec<RunicRitualRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter(RunicRitualRecipe::result),
                ItemStack.SINGLE_ITEM_CODEC.fieldOf("runeBlock").forGetter(RunicRitualRecipe::runeBlock)
        ).apply(builder, RunicRitualRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, RunicRitualRecipe> STREAM_CODEC = StreamCodec.composite(
                ItemStack.OPTIONAL_STREAM_CODEC,
                RunicRitualRecipe::result,
                ItemStack.OPTIONAL_STREAM_CODEC,
                RunicRitualRecipe::runeBlock,
                RunicRitualRecipe::new
        );

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<RunicRitualRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, RunicRitualRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Type implements RecipeType<RunicRitualRecipe> {
        public static final RunicRitualRecipe.Type INSTANCE = new RunicRitualRecipe.Type();

        private Type() {
        }

        @Override
        public String toString() {
            return RunicRitualRecipe.NAME;
        }
    }
}
