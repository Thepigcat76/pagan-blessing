package com.pigdad.paganbless.registries.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AthameItem extends SwordItem {
    public AthameItem(Properties p_43272_) {
        super(Tiers.DIAMOND, p_43272_);
    }

    @Override
    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity livingEntity, LivingEntity player) {
        if (livingEntity.getType().is(EntityTypeTags.UNDEAD)) {
            livingEntity.hurt(livingEntity.level().damageSources().playerAttack((Player) player), 11F);
        }
        return super.hurtEnemy(p_43278_, livingEntity, player);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> tooltip, TooltipFlag p_41424_) {
        tooltip.add(Component.translatable("desc.paganbless.athame").withStyle(ChatFormatting.GRAY));
    }
}
