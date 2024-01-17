package com.pigdad.paganbless.registries.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PaganGuideItem {
    public PaganGuideItem(Item.Properties pProperties) {
        /* super(pProperties); */
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        /*
        try {
            return super.use(pLevel, pPlayer, pUsedHand);
        } catch (Exception ignored) {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        }
         */
        return InteractionResultHolder.success(null);
    }
}
