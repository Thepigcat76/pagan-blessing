package com.pigdad.paganbless.content.items;

import com.pigdad.paganbless.registries.PBBlocks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

public class ChaliceItem extends BlockItem {
    public ChaliceItem(Properties properties) {
        super(PBBlocks.CHALICE.get(), properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_42923_, Level p_42924_, LivingEntity p_42925_) {
        if (!p_42924_.isClientSide()) {
            p_42925_.removeEffectsCuredBy(EffectCures.MILK);
        }
        return p_42923_;
    }

    public int getUseDuration(ItemStack stack, LivingEntity p_345727_) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
