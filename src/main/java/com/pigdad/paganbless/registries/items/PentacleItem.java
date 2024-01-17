package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.PentacleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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

        blockEntity.spawner.setEntityId(EntityType.by(tag).get(), level, level.getRandom(), blockPos);

        // EntityType.create(tag, level);
        return InteractionResult.SUCCESS;
    }
}
