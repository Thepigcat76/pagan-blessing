package com.pigdad.paganbless.registries.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class WaxedHangingHerbBlock extends BaseHangingHerbBlock {
    public static final int MAX_HANGING_AMOUNT = 4;
    public static final IntegerProperty HANGING_AMOUNT = IntegerProperty.create("hanging_amount", 1, MAX_HANGING_AMOUNT);

    public WaxedHangingHerbBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int amount = state.getValue(HANGING_AMOUNT);
        if (Block.byItem(stack.getItem()) == this) {
            if (amount < MAX_HANGING_AMOUNT) {
                level.setBlockAndUpdate(pos, state.setValue(HANGING_AMOUNT, amount + 1));
                level.playSound(player, pos, SoundEvents.BAMBOO_SAPLING_PLACE, SoundSource.BLOCKS);
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                return ItemInteractionResult.SUCCESS;
            }
        } else if (stack.isEmpty()) {
            if (amount > 1) {
                level.setBlockAndUpdate(pos, state.setValue(HANGING_AMOUNT, amount - 1));
                ItemHandlerHelper.giveItemToPlayer(player, this.asItem().getDefaultInstance());
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(HANGING_AMOUNT));
    }
}
