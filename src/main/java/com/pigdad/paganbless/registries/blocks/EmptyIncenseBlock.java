package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.blockentities.IncenseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class EmptyIncenseBlock extends IncenseBlock {
    public EmptyIncenseBlock(Properties properties) {
        super(properties.lightLevel(blockState -> 0));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(EmptyIncenseBlock::new);
    }

    @Override
    public void clientTick(Level level, BlockPos blockPos, BlockState blockState, IncenseBlockEntity blockEntity) {
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            Block incenseBlock = PBItems.INCENSES.get(pStack.getItem());
            if (incenseBlock != null) {
                pLevel.playSound(null, (double) pPos.getX() + 0.5, (double) pPos.getY() + 0.5, (double) pPos.getZ() + 0.5,
                        SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
                pLevel.setBlockAndUpdate(pPos, incenseBlock.defaultBlockState().setValue(IncenseBlock.ROTATION, pState.getValue(IncenseBlock.ROTATION)));
                if (!pPlayer.hasInfiniteMaterials()) {
                    pStack.shrink(1);
                }
            } else {
                return ItemInteractionResult.FAIL;
            }
        }
        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void effectTick(Level level, BlockPos blockPos, BlockState blockState) {
    }

    @Override
    public Item getIncenseItem() {
        return Items.AIR;
    }

    @Override
    public int getRange(Level level, BlockPos blockPos, BlockState blockState) {
        return 0;
    }
}
