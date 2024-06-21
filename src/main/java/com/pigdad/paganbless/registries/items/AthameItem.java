package com.pigdad.paganbless.registries.items;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class AthameItem extends SwordItem {
    public AthameItem(Properties properties) {
        super(Tiers.DIAMOND, properties.attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3, -2.2F)));
    }

    @Override
    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity livingEntity, LivingEntity player) {
        if (livingEntity.getType().is(EntityTypeTags.UNDEAD)) {
            livingEntity.hurt(livingEntity.level().damageSources().playerAttack((Player) player), 11F);
        }
        return super.hurtEnemy(p_43278_, livingEntity, player);
    }
}
