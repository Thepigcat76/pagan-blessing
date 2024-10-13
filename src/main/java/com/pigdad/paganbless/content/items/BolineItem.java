package com.pigdad.paganbless.content.items;

import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.content.blocks.EssenceLogBlock;
import com.pigdad.paganbless.content.blocks.HerbPlantBlock;
import com.pigdad.paganbless.registries.PBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BolineItem extends SwordItem {
    public BolineItem(Properties pProperties) {
        super(Tiers.STONE, pProperties.attributes(createAttributes(Tiers.STONE, 4, -2.6f)));
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player) {
            if (pTarget instanceof Skeleton && pTarget.getHealth() == 0) {
                RandomSource randomSource = pTarget.getRandom();
                int i = randomSource.nextInt(0, 100 / PBConfig.skeletonSkullFromBolineChance);
                System.out.println(i);
                if (i == 0) {
                    BlockPos onPos = pTarget.getOnPos();
                    Containers.dropItemStack(pTarget.level(), onPos.getX(), onPos.getY(), onPos.getZ(), Items.SKELETON_SKULL.getDefaultInstance());
                }
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
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
                pContext.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(pContext.getHand()));
                return InteractionResult.SUCCESS;
            }
        } else if (blockState.getBlock() instanceof NetherWartBlock wartBlock) {
            if (blockState.getValue(NetherWartBlock.AGE) >= 3) {
                dropCropDrops(level, player, blockPos, blockState);
                level.setBlockAndUpdate(blockPos, wartBlock.defaultBlockState());
                pContext.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(pContext.getHand()));
                return InteractionResult.SUCCESS;
            }
        } else if (blockState.getBlock() instanceof HerbPlantBlock herbPlantBlock) {
            if (blockState.getValue(HerbPlantBlock.AGE) >= 5) {
                dropCropDrops(level, player, blockPos, blockState);
                level.setBlockAndUpdate(blockPos, herbPlantBlock.defaultBlockState().setValue(HerbPlantBlock.AGE, 0));
                pContext.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(pContext.getHand()));
                return InteractionResult.SUCCESS;
            }
        } else if (blockState.getBlock() instanceof EssenceLogBlock && blockState.getValue(EssenceLogBlock.ESSENCE)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(EssenceLogBlock.ESSENCE, false));
            level.playSound(null, blockPos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1, 1.5f);
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(PBItems.ESSENCE_OF_THE_FOREST.get(), level.random.nextInt(1, 3)));
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
