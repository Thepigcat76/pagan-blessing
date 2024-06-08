package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBTags;
import com.pigdad.paganbless.registries.blockentities.RuneSlabBlockEntity;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RunicChargeItem extends Item {
    public RunicChargeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(PBBlocks.RUNE_SLAB_INERT.get())) {
            RuneSlabBlock.RuneState runeState = blockState.getValue(RuneSlabBlock.RUNE_STATE);
            BlockEntity blockEntity;
            if (blockState.getValue(RuneSlabBlock.IS_TOP)) {
                blockEntity = level.getBlockEntity(blockPos.below());
            } else {
                blockEntity = level.getBlockEntity(blockPos);
            }
            RuneSlabBlockEntity runeSlabBlockEntity = (RuneSlabBlockEntity) blockEntity;
            String prevBlock = runeSlabBlockEntity.getPrevBlock();
            if (prevBlock != null) {
                ResourceLocation defaultStateLocation = new ResourceLocation(prevBlock);
                BlockState defaultState = BuiltInRegistries.BLOCK.get(defaultStateLocation).defaultBlockState();

                if (defaultState.isEmpty()) return InteractionResult.FAIL;

                if (!defaultState.is(PBTags.BlockTags.RUNIC_CHARGE_NO_REFILL)) {
                    Player player = ctx.getPlayer();
                    if (blockState.getValue(RuneSlabBlock.IS_TOP)) {
                        level.setBlockAndUpdate(blockPos, defaultState
                                .setValue(RuneSlabBlock.IS_TOP, true)
                                .setValue(RuneSlabBlock.RUNE_STATE, runeState));
                        level.setBlockAndUpdate(blockPos.below(), defaultState
                                .setValue(RuneSlabBlock.IS_TOP, false)
                                .setValue(RuneSlabBlock.RUNE_STATE, runeState));
                    } else {
                        level.setBlockAndUpdate(blockPos.above(), defaultState
                                .setValue(RuneSlabBlock.IS_TOP, true)
                                .setValue(RuneSlabBlock.RUNE_STATE, runeState));
                        level.setBlockAndUpdate(blockPos, defaultState
                                .setValue(RuneSlabBlock.IS_TOP, false)
                                .setValue(RuneSlabBlock.RUNE_STATE, runeState));
                    }
                    level.playSound(null, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.isCreative()) {
                        player.getItemInHand(ctx.getHand()).shrink(1);
                        ItemHandlerHelper.giveItemToPlayer(player, Items.GLASS_BOTTLE.getDefaultInstance());
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }
}
