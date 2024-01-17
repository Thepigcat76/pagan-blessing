package com.pigdad.paganbless.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pigdad.paganbless.registries.recipes.RunicRitualRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/*
 * From CofhCore with modifications. Credits to the CoFH Team for this.
 */

public class RecipeUtils {
    @Nullable
    public static Fluid parseFluid(JsonElement jsonElement) {
        for (Map.Entry<ResourceKey<Fluid>, Fluid> fluid1 : BuiltInRegistries.FLUID.entrySet()) {
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
            ingredients.add(ingredients.size() - 1, parseIngredient(element.getAsJsonObject()));
        }
    }

    public static Ingredient parseIngredient(JsonElement element) {

        if (element == null || element.isJsonNull()) {
            return Ingredient.of(ItemStack.EMPTY);
        }
        Ingredient ingredient;

        if (element.isJsonArray()) {
            try {
                ingredient = Ingredient.fromJson(element, true);
            } catch (Throwable t) {
                ingredient = Ingredient.of(ItemStack.EMPTY);
            }
        } else {
            JsonElement subElement = element.getAsJsonObject();
            try {
                JsonObject object = subElement.getAsJsonObject();
                ingredient = Ingredient.fromJson(subElement, true);
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
            return BuiltInRegistries.ITEM.get(new ResourceLocation(jsonElement.getAsString()));
        } catch (Exception ignored) {
            throw new NullPointerException("Runeblock is null");
        }
    }

    public static List<RecipeHolder<RunicRitualRecipe>> getAllRitualRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(RunicRitualRecipe.Type.INSTANCE);
    }
}
