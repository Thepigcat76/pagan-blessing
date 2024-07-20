package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.RuneSlabBlockEntity;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
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
                BlockState defaultState = ForgeRegistries.BLOCKS.getValue(defaultStateLocation).defaultBlockState();
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
                if (!ctx.getPlayer().isCreative()){
                    ctx.getPlayer().getItemInHand(ctx.getHand()).setCount(0);
                    ItemHandlerHelper.giveItemToPlayer(ctx.getPlayer(), Items.GLASS_BOTTLE.getDefaultInstance());
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(Component.translatable("desc.paganbless.runic_charge").withStyle(ChatFormatting.GRAY));
    }
}
