package com.pigdad.paganbless.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.NotNull;

public class LogBlock extends FlammableRotatedPillarBlock {
    private final Block strippedBlock;

    public LogBlock(Properties pProperties, Block strippedBlock) {
        super(pProperties);
        this.strippedBlock = strippedBlock;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pStack.canPerformAction(ItemAbilities.AXE_STRIP))  {
            pLevel.setBlockAndUpdate(pPos, strippedBlock.defaultBlockState());
            pPlayer.playSound(SoundEvents.AXE_STRIP);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }
}
