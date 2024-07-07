package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.registries.blockentities.WinchBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class RopeItem extends BlockItem {
    public RopeItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (blockEntity instanceof WinchBlockEntity winchBlockEntity) {
            ItemStack remainder = winchBlockEntity.getItemHandler().insertItem(0, context.getItemInHand(), false);
            context.getPlayer().setItemInHand(context.getHand(), remainder);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
