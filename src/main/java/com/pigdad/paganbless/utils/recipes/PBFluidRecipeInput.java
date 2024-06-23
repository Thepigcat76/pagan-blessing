package com.pigdad.paganbless.utils.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record PBFluidRecipeInput(List<ItemStack> items, FluidStack fluidStack) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public int size() {
        return items.size();
    }
}
