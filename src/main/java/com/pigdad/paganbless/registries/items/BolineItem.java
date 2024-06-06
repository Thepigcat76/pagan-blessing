package com.pigdad.paganbless.registries.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BolineItem extends SwordItem {
    public BolineItem(Properties pProperties) {
        super(Tiers.STONE, pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();

        if (level.isClientSide()) return InteractionResult.PASS;

        Player player = pContext.getPlayer();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.getBlock() instanceof CropBlock cropBlock) {
            if (cropBlock.isMaxAge(blockState)) {
                dropCropDrops(level, player, blockPos, blockState);
                level.setBlockAndUpdate(blockPos, cropBlock.getStateForAge(0));
            }
            return InteractionResult.SUCCESS;
        } else if (blockState.getBlock() instanceof NetherWartBlock wartBlock) {
            if (blockState.getValue(NetherWartBlock.AGE) >= 3) {
                dropCropDrops(level, player, blockPos, blockState);
                level.setBlockAndUpdate(blockPos, wartBlock.defaultBlockState());
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private void dropCropDrops(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        List<ItemStack> drops = Block.getDrops(blockState, (ServerLevel) level, blockPos, null, player, player.getMainHandItem());
        for (ItemStack drop : drops) {
            if (!drop.isEmpty()) {
                Containers.dropItemStack(level, blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, drop);
            }
        }
        level.destroyBlock(blockPos, false, player);
    }
}
