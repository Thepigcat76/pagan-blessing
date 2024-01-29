package com.pigdad.paganbless.registries.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.EffectCures;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.translatable("desc.paganbless.chalice").withStyle(ChatFormatting.GRAY));
    }
}
