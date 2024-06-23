package com.pigdad.paganbless.utils.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record PBRecipeInput(List<ItemStack> items) implements RecipeInput {
    public PBRecipeInput(ItemStack ...items) {
        this(List.of(items));
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public int size() {
        return items.size();
    }
}
