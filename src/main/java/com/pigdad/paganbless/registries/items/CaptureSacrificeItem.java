package com.pigdad.paganbless.registries.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Item that when crafted through runic ritual captures the sacrificed entity
 */
public interface CaptureSacrificeItem {
    default void setEntity(Entity entity, ItemStack itemStack) {
        if (entity instanceof LivingEntity livingEntity){
            CompoundTag tag = itemStack.getOrCreateTag();
            tag.put("entity", entity.serializeNBT());
            tag.getCompound("entity").putFloat("Health", livingEntity.getMaxHealth());
            tag.getCompound("entity").remove("HandItems");
            tag.getCompound("entity").remove("ArmorItems");
        }
    }
}
