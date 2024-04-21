package com.pigdad.paganbless.registries.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Item that when crafted through runic ritual captures the sacrificed entity
 */
public interface CaptureSacrificeItem {
    default void setEntity(Entity entity, ItemStack itemStack) {
        setEntity(entity.getType(), itemStack);
    }

    default void setEntity(EntityType<?> entity, ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.put("entity", new CompoundTag());
        tag.getCompound("entity").putString("id", EntityType.getKey(entity).toString());
    }
}
