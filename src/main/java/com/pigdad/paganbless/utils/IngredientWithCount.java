package com.pigdad.paganbless.utils;

/*
 * From CofhCore with modifications. Credits to the CoFH Team for this.
 */

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class IngredientWithCount extends Ingredient {
    private final Ingredient ingredient;
    private final int count;

    public IngredientWithCount(Ingredient ingredient, int count) {
        super(Stream.of(ingredient.getValues()));
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
}
