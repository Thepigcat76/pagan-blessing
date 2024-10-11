package com.pigdad.paganbless.utils.recipes;

import com.pigdad.paganbless.content.recipes.AnvilSmashingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AnvilRecipeUtils {
    public static void onAnvilLand(Level level, BlockPos blockPos) {
        List<Entity> entities = level.getEntities(EntityType.ITEM.create(level), new AABB(blockPos));
        List<ItemEntity> itemEntities = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof ItemEntity itemEntity) {
                itemEntities.add(itemEntity);
            }
        }
        craftItem(level, itemEntities, blockPos);
    }

    private static void craftItem(Level level, List<ItemEntity> itemEntities, BlockPos blockPos) {
        List<ItemStack> itemStacks = new ArrayList<>(itemEntities.size());
        for (ItemEntity entity : itemEntities) {
            itemStacks.add(entity.getItem());
        }
        PBRecipeInput recipeInput = new PBRecipeInput(itemStacks);
        Optional<RecipeHolder<AnvilSmashingRecipe>> optionalRecipe = getCurrentRecipe(level, recipeInput);
        optionalRecipe.ifPresent(anvilSmashingRecipe -> {
            ItemStack resultItem = anvilSmashingRecipe.value().result().copy();
            for (ItemEntity itemEntity : itemEntities) {
                for (Ingredient ingredient : anvilSmashingRecipe.value().getIngredients()) {
                    if (ingredient.test(itemEntity.getItem())){
                        int count = ingredient.getItems()[0].getCount();
                        itemEntity.getItem().shrink(count);
                    }
                }
            }
            Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), resultItem);
        });
    }

    private static Optional<RecipeHolder<AnvilSmashingRecipe>> getCurrentRecipe(Level level, PBRecipeInput container) {
        return level.getRecipeManager().getRecipeFor(AnvilSmashingRecipe.Type.INSTANCE, container, level);
    }
}
