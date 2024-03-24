package com.pigdad.paganbless.mixins;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.recipes.AnvilSmashingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    @Inject(
            method = "onLand",
            at = @At("TAIL")
    )
    public void anvilLanding(Level level, BlockPos blockPos, BlockState blockState, BlockState oldBlockState, FallingBlockEntity fallingBlockEntity, CallbackInfo callbackInfoLevel) {
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
        PaganBless.LOGGER.debug("Entities: {}, amount: {}", itemEntities, itemEntities.size());
        SimpleContainer container = new SimpleContainer(itemEntities.size());
        for (int i = 0; i < itemEntities.size(); i++) {
            container.setItem(i, itemEntities.get(i).getItem());
        }
        Optional<RecipeHolder<AnvilSmashingRecipe>> optionalRecipe = getCurrentRecipe(level, container);
        optionalRecipe.ifPresent(recipe -> {
            PaganBless.LOGGER.debug("Found recipe");
            ItemStack resultItem = recipe.value().getResultItem(level.registryAccess());
            for (Ingredient ingredient : recipe.value().getIngredients()) {
                for (ItemEntity itemEntity : itemEntities) {
                    if (ingredient.getItems()[0].getItem().equals(itemEntity.getItem().getItem())) {
                        PaganBless.LOGGER.debug("Item: {}", ingredient.getItems()[0]);
                        itemEntity.getItem().setCount(itemEntity.getItem().getCount()-1);
                        break;
                    }
                }
            }
            level.addFreshEntity(new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), resultItem));
        });
    }

    private static Optional<RecipeHolder<AnvilSmashingRecipe>> getCurrentRecipe(Level level, SimpleContainer container) {
        return level.getRecipeManager().getRecipeFor(AnvilSmashingRecipe.Type.INSTANCE, container, level);
    }
}
