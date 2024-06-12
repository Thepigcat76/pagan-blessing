package com.pigdad.paganbless.registries.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

public class ChaliceItem extends MilkBucketItem {
    public ChaliceItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_42923_, Level p_42924_, LivingEntity p_42925_) {
        if (!p_42924_.isClientSide()) {
            p_42925_.removeEffectsCuredBy(EffectCures.MILK);
        }
        return p_42923_;
    }
}
