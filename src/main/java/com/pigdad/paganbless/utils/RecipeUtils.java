package com.pigdad.paganbless.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/*
 * From CofhCore with modifications. Credits to the CoFH Team for this.
 */

public class RecipeUtils {
    @Nullable
    public static Fluid parseFluid(JsonElement jsonElement) {
        for (Map.Entry<ResourceKey<Fluid>,Fluid> fluid1 : ForgeRegistries.FLUIDS.getEntries()) {
            if (jsonElement.getAsString().equals(fluid1.getValue().getFluidType().toString())) {
                return fluid1.getValue();
            }
        }
        return null;
    }

    public static void parseInputs(NonNullList<Ingredient> ingredients, JsonElement element) {
        if (element.isJsonArray()) {
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                JsonElement arrayElement = element.getAsJsonArray().get(i);
                if (arrayElement.isJsonArray()) {
                    ingredients.add(i, parseIngredient(arrayElement.getAsJsonArray()));
                } else if (arrayElement.isJsonObject()) {
                    ingredients.add(i, parseIngredient(arrayElement.getAsJsonObject()));
                }
            }
        } else {
            ingredients.add(ingredients.size()-1, parseIngredient(element.getAsJsonObject()));
        }
    }

    public static Ingredient parseIngredient(JsonElement element) {

        if (element == null || element.isJsonNull()) {
            return Ingredient.of(ItemStack.EMPTY);
        }
        Ingredient ingredient;

        if (element.isJsonArray()) {
            try {
                ingredient = Ingredient.fromJson(element);
            } catch (Throwable t) {
                ingredient = Ingredient.of(ItemStack.EMPTY);
            }
        } else {
            JsonElement subElement = element.getAsJsonObject();
            try {
                JsonObject object = subElement.getAsJsonObject();
                ingredient = Ingredient.fromJson(subElement);
                int count;
                if (object.has("count")) {
                    count = object.get("count").getAsInt();
                } else {
                    count = 1;
                }
                if (count > 1) {
                    return new IngredientWithCount(ingredient, count);
                }
            } catch (Throwable t) {
                ingredient = Ingredient.of(ItemStack.EMPTY);
            }
        }
        return ingredient;
    }

    public static Item parseItem(JsonElement jsonElement) {
        try {
            String[] rawBlock = jsonElement.getAsString().split(":");
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation(rawBlock[0], rawBlock[1]));
        } catch (Exception ignored) {
            throw new NullPointerException("Runeblock is null");
        }
    }

    public static List<RunicRitualRecipe> getAllRitualRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(RunicRitualRecipe.Type.INSTANCE);
    }

    public static boolean compareItems(List<ItemStack> inputs, List<Ingredient> ingredients) {
        int elements = inputs.size();
        if (elements != ingredients.size()) {
            return false;
        } else {
            int[] ret = new int[elements];

            Arrays.fill(ret, -1);

            BitSet data = new BitSet((elements + 2) * elements);

            for (int x = 0; x < elements; ++x) {
                int matched = 0;
                int offset = (x + 2) * elements;
                Ingredient test = ingredients.get(x);

                for (int y = 0; y < elements; ++y) {
                    if (!data.get(y) && test.test(inputs.get(y))) {
                        data.set(offset + y);
                        ++matched;
                    }
                }

                if (matched == 0) {
                    return false;
                }

                if (matched == 1 && !claim(ret, data, x, elements)) {
                    return false;
                }
            }

            if (data.nextClearBit(0) >= elements) {
                return true;
            } else return backtrack(data, ret, 0, elements);
        }
    }

    private static boolean claim(int[] ret, BitSet data, int claimed, int elements) {
        Queue<Integer> pending = new LinkedList<>();
        pending.add(claimed);

        while (pending.peek() != null) {
            int test = pending.poll();
            int offset = (test + 2) * elements;
            int used = data.nextSetBit(offset) - offset;
            if (used >= elements || used < 0) {
                throw new IllegalStateException("What? We matched something, but it wasn't set in the range of this test! Test: " + test + " Used: " + used);
            }

            data.set(used);
            data.set(elements + test);
            ret[used] = test;

            for (int x = 0; x < elements; ++x) {
                offset = (x + 2) * elements;
                if (data.get(offset + used) && !data.get(elements + x)) {
                    data.clear(offset + used);
                    int count = 0;

                    for (int y = offset; y < offset + elements; ++y) {
                        if (data.get(y)) {
                            ++count;
                        }
                    }

                    if (count == 0) {
                        return false;
                    }

                    if (count == 1) {
                        pending.add(x);
                    }
                }
            }
        }

        return true;
    }

    private static boolean backtrack(BitSet data, int[] ret, int start, int elements) {
        int test = data.nextClearBit(elements + start) - elements;
        if (test >= elements) {
            return true;
        } else if (test < 0) {
            throw new IllegalStateException("This should never happen, negative test in backtrack!");
        } else {
            int offset = (test + 2) * elements;

            for (int x = 0; x < elements; ++x) {
                if (data.get(offset + x) && !data.get(x)) {
                    data.set(x);
                    if (backtrack(data, ret, test + 1, elements)) {
                        ret[x] = test;
                        return true;
                    }

                    data.clear(x);
                }
            }

            return false;
        }
    }
}
