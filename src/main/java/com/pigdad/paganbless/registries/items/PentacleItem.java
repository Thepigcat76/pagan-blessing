package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.PentacleBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PentacleItem extends Item implements CaptureSacrificeItem {
    public PentacleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        BlockPos blockPos = useOnContext.getClickedPos().above();

        CompoundTag tag = useOnContext.getItemInHand().get(DataComponents.ENTITY_DATA).copyTag();

        level.setBlockAndUpdate(blockPos, PBBlocks.PENTACLE.get().defaultBlockState());

        PentacleBlockEntity blockEntity = (PentacleBlockEntity) level.getBlockEntity(blockPos);

        if (!player.isCreative()) {
            useOnContext.getItemInHand().shrink(1);
        }

        EntityType<?> pType = EntityType.by(tag).get();
        if (!PBConfig.entityTypes.contains(pType)) {
            blockEntity.spawner.setEntityId(pType, level, level.getRandom(), blockPos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (!pStack.get(DataComponents.ENTITY_DATA).isEmpty()) {
            pTooltipComponents.add(Component.literal("[")
                    .append(EntityType.by(pStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).copyTag()).get().getDescription())
                    .append("]")
                    .withStyle(ChatFormatting.RED));
        } else {
            pTooltipComponents.add(Component.translatable("desc.paganbless.decorative").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
