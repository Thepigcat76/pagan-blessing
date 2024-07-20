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
        SimpleContainer container = new SimpleContainer(itemEntities.size());
        for (int i = 0; i < itemEntities.size(); i++) {
            container.setItem(i, itemEntities.get(i).getItem());
        }
        Optional<AnvilSmashingRecipe> optionalRecipe = getCurrentRecipe(level, container);
        optionalRecipe.ifPresent(anvilSmashingRecipe -> {
            ItemStack resultItem = anvilSmashingRecipe.getResultItem(level.registryAccess());
            for (ItemEntity itemEntity : itemEntities) {
                for (Ingredient ingredient : anvilSmashingRecipe.getIngredients()) {
                    if (ingredient.test(itemEntity.getItem())){
                        int count = ingredient.getItems()[0].getCount();
                        itemEntity.getItem().shrink(count);
                    }
                }
            }
            level.addFreshEntity(new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), resultItem));
        });
    }

    private static Optional<AnvilSmashingRecipe> getCurrentRecipe(Level level, SimpleContainer container) {
        return level.getRecipeManager().getRecipeFor(AnvilSmashingRecipe.Type.INSTANCE, container, level);
    }
}
