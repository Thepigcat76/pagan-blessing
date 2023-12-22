package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.blockentities.RuneSlabBlockEntity;
import com.pigdad.paganbless.registries.blocks.RuneSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicChargeItem extends Item {
    public RunicChargeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
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
            PaganBless.LOGGER.info("Blockentity: {}", runeSlabBlockEntity);
            String prevBlock = runeSlabBlockEntity.getPrevBlock();
            PaganBless.LOGGER.info("Prev block: {}", prevBlock);
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
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
