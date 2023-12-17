package com.pigdad.pigdadmod.registries.items;

import com.pigdad.pigdadmod.PigDadMod;
import com.pigdad.pigdadmod.registries.ModBlocks;
import com.pigdad.pigdadmod.registries.ModEntities;
import com.pigdad.pigdadmod.registries.blockentities.PentacleBlockEntity;
import com.pigdad.pigdadmod.registries.entities.EternalSnowballEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

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

        level.setBlockAndUpdate(blockPos, ModBlocks.PENTACLE.get().defaultBlockState());

        PentacleBlockEntity blockEntity = (PentacleBlockEntity) level.getBlockEntity(blockPos);

        if (!player.isCreative()) {
            useOnContext.getItemInHand().shrink(1);
        }

        blockEntity.setEntityTag(tag);

        // EntityType.create(tag, level);
        return InteractionResult.SUCCESS;
    }
}
