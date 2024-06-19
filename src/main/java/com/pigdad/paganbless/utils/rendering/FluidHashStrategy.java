package com.pigdad.paganbless.utils.rendering;

import it.unimi.dsi.fastutil.Hash;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidHashStrategy implements Hash.Strategy<FluidStack> {
    public static final FluidHashStrategy INSTANCE = new FluidHashStrategy();

    private FluidHashStrategy() {
    }

    @Override
    public int hashCode(FluidStack stack) {
        return FluidStack.hashFluidAndComponents(stack);
    }

    @Override
    public boolean equals(FluidStack a, FluidStack b) {
        if (a == b) {
            return true;
        }
        return a != null && b != null && a.isEmpty() == b.isEmpty() && FluidStack.isSameFluidSameComponents(a, b);
    }
}