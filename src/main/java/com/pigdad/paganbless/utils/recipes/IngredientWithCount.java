package com.pigdad.paganbless.utils.recipes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

// TODO: Move this to a record codec builder if something breaks
public record IngredientWithCount(Ingredient ingredient, int count) {
    public static final IngredientWithCount EMPTY = new IngredientWithCount(Ingredient.EMPTY, -1);
    // Note: for implementation reasons count has to be above ingredient, otherwise we will get a JSON Null issue thingy
    private static final Codec<Pair<Integer, Ingredient>> PAIR_CODEC = Codec.pair(
            Codec.INT.optionalFieldOf("count", 1).codec(),
            Ingredient.CODEC
    );

    public static final Codec<IngredientWithCount> CODEC = PAIR_CODEC.xmap(pair -> new IngredientWithCount(pair.getSecond(), pair.getFirst()),
            iwc -> new Pair<>(iwc.count, iwc.ingredient));

    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientWithCount> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            IngredientWithCount::ingredient,
            ByteBufCodecs.INT,
            IngredientWithCount::count,
            IngredientWithCount::new
    );

    public boolean test(ItemStack itemStack) {
        return ingredient.test(itemStack) && itemStack.getCount() >= count;
    }

    public static IngredientWithCount fromItemStack(ItemStack itemStack) {
        return new IngredientWithCount(Ingredient.of(itemStack), itemStack.getCount());
    }

    public static IngredientWithCount fromItemTag(TagKey<Item> itemTagKey) {
        return new IngredientWithCount(Ingredient.of(itemTagKey), 1);
    }

    public static IngredientWithCount fromItemLike(ItemLike itemLike) {
        return new IngredientWithCount(Ingredient.of(itemLike), 1);
    }
}
