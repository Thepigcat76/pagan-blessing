package com.pigdad.paganbless.content.items;

import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.content.blockentities.PentacleBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

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

        Optional<EntityType<?>> pType = EntityType.by(tag);
        pType.ifPresent(entityType -> {
            if (!PBConfig.entityTypes.contains(entityType)) {
                blockEntity.spawner.setEntityId(entityType, level, level.getRandom(), blockPos);
            }
        });
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

    public static @NotNull ItemStack getPentacleDefaultStack() {
        PentacleItem item = (PentacleItem) PBItems.PENTACLE.get();
        ItemStack itemStack = item.getDefaultInstance();
        ((CaptureSacrificeItem) itemStack.getItem()).setEntity(EntityType.PIG, itemStack);
        return itemStack;
    }
}
