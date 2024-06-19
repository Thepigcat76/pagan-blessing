package com.pigdad.paganbless.utils.rendering;

import net.neoforged.neoforge.fluids.FluidStack;

public final class ModelRenderer {
    public static int getStage(FluidStack stack, int stages, double scale) {
        return getStage(stages, scale);
    }

    public static int getStage(int stages, double scale) {
        return Math.min(stages - 1, (int) (scale * (stages - 1)));
    }
}
