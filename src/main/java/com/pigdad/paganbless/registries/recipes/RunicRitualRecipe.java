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
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunicRitualRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "runic_ritual";

    private final ItemStack output;
    private final ItemStack runeBlock;

    // TODO: rename output to result
    public RunicRitualRecipe(ItemStack runeBlock, ItemStack output) {
        this.runeBlock = runeBlock;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleContainer p_44002_, Level level) {
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
    public @NotNull ItemStack assemble(@NotNull SimpleContainer simpleContainer, @NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
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
        private static final Codec<RunicRitualRecipe> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                ItemStack.SINGLE_ITEM_CODEC.fieldOf("runeBlock").forGetter(recipe -> recipe.runeBlock),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("output").forGetter((recipe) -> recipe.output)
        ).apply(builder, RunicRitualRecipe::new));

        private Serializer() {
        }

        @Override
        public @NotNull Codec<RunicRitualRecipe> codec() {
            return CODEC;
        }


        @Override
        public RunicRitualRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            return pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, CODEC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, RunicRitualRecipe pRecipe) {
            pBuffer.writeWithCodec(NbtOps.INSTANCE, CODEC, pRecipe);
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
