package com.pigdad.paganbless.registries.items;

import com.klikli_dev.modonomicon.data.BookDataManager;
import com.klikli_dev.modonomicon.item.ModonomiconItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PaganGuideItem extends ModonomiconItem {
    public PaganGuideItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        try {
            return super.use(pLevel, pPlayer, pUsedHand);
        } catch (Exception ignored) {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level p_41448_, Player p_41449_) {
        ResourceLocation id = BookDataManager.get().getBook(new ResourceLocation("paganbless:pagan_guide")).getId();
        itemStack.getOrCreateTag().putString("modonomicon:book_id", id.toString());
    }
}
