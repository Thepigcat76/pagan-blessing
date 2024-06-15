package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.registries.blockentities.PentacleBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PentacleItem extends BlockItem implements CaptureSacrificeItem {
    public PentacleItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        boolean result = super.placeBlock(pContext, pState);
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos().above();

        CompoundTag tag = pContext.getItemInHand().get(DataComponents.ENTITY_DATA).copyTag();

        PentacleBlockEntity blockEntity = (PentacleBlockEntity) level.getBlockEntity(blockPos);

        EntityType<?> pType = EntityType.by(tag).get();
        if (!PBConfig.entityTypes.contains(pType)) {
            blockEntity.spawner.setEntityId(pType, level, level.getRandom(), blockPos);
        }
        return result;
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
