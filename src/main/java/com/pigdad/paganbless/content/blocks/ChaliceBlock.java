package com.pigdad.paganbless.content.blocks;

import com.pigdad.paganbless.utils.Utils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class ChaliceBlock extends Block {
    public ChaliceBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(5, 0, 5, 11, 12, 11);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        IFluidHandler capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (stack.is(Items.BUCKET)) {
            int count = stack.getCount();
            player.awardStat(Stats.ITEM_USED.get(Items.BUCKET));
            player.playSound(NeoForgeMod.BUCKET_FILL_MILK.get());
            level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
            ItemStack itemstack2 = ItemUtils.createFilledResult(stack, player, Items.MILK_BUCKET.getDefaultInstance());
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, itemstack2);
            }

            if (count == 1) {
                player.setItemInHand(hand, itemstack2);
            }
            return ItemInteractionResult.SUCCESS;
        } else if (capability != null && capability.getFluidInTank(0).isEmpty()) {
            capability.fill(new FluidStack(NeoForgeMod.MILK, 1000), IFluidHandler.FluidAction.EXECUTE);
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

}
