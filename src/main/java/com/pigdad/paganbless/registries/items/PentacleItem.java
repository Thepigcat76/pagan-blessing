package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.PentacleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.swing.text.html.Option;
import java.util.Optional;

public class PentacleItem extends Item implements CaptureSacrificeItem {
    public PentacleItem(Properties p_40566_) {
        super(p_40566_);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        BlockPos blockPos = useOnContext.getClickedPos().above();

        CompoundTag tag = useOnContext.getItemInHand().getOrCreateTag().getCompound("entity");

        level.setBlockAndUpdate(blockPos, PBBlocks.PENTACLE.get().defaultBlockState());

        PentacleBlockEntity blockEntity = (PentacleBlockEntity) level.getBlockEntity(blockPos);

        if (!player.isCreative()) {
            useOnContext.getItemInHand().shrink(1);
        }

        Optional<EntityType<?>> entityType = EntityType.by(tag);

        if (blockEntity != null) {
            entityType.ifPresent(type -> blockEntity.spawner.setEntityId(type, level, level.getRandom(), blockPos));
        }

        // EntityType.create(tag, level);
        return InteractionResult.SUCCESS;
    }
}
