package com.pigdad.paganbless.utils;

import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Utils {
    // Get capability of a block entity
    public static <T, C> @Nullable T getCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), null);
    }

    public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
        Map<V, K> returnMap = new HashMap<>();
        map.forEach((key, value) -> returnMap.put(value, key));
        return returnMap;
    }

    public static List<RecipeHolder<RunicRitualRecipe>> getAllRitualRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(RunicRitualRecipe.Type.INSTANCE);
    }
}
