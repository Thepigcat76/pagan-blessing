package com.pigdad.pigdadmod.registries.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pigdad.pigdadmod.PigDadMod;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class RecipeUtils {

    public static void parseInputs(List<Ingredient> ingredients, JsonElement element) {
        PigDadMod.LOGGER.info("Parsing input");
        if (element.isJsonArray()) {
            for (JsonElement arrayElement : element.getAsJsonArray()) {
                if (arrayElement.isJsonArray()) {
                    ingredients.add(parseIngredient(arrayElement.getAsJsonArray()));
                } else if (arrayElement.isJsonObject()) {
                    ingredients.add(parseIngredient(arrayElement.getAsJsonObject()));
                }
            }
        } else {
            ingredients.add(parseIngredient(element.getAsJsonObject()));
        }
    }

    public static Ingredient parseIngredient(JsonElement element) {

        if (element == null || element.isJsonNull()) {
            return Ingredient.of(ItemStack.EMPTY);
        }
        Ingredient ingredient;

        if (element.isJsonArray()) {
            PigDadMod.LOGGER.info("element is array");
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
                PigDadMod.LOGGER.info("Testing for count");
                if (object.has("count")) {
                    count = object.get("count").getAsInt();
                    PigDadMod.LOGGER.info("Count: "+count);
                } else {
                    count = 1;
                }
                if (count > 1) {
                    PigDadMod.LOGGER.info("Returning ingredient with count");
                    return new IngredientWithCount(ingredient, count);
                }
            } catch (Throwable t) {
                ingredient = Ingredient.of(ItemStack.EMPTY);
            }
        }
        return ingredient;
    }
}
