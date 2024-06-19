package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.entities.WandProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WandItem extends Item {
    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        PaganBless.LOGGER.debug("time: {}", pTimeCharged);
        if (!pLevel.isClientSide() && pTimeCharged <= getUseDuration(pStack, pLivingEntity) - 15) {
            WandProjectileEntity wandProjectile = new WandProjectileEntity(pLevel, pLivingEntity);
            wandProjectile.setItem(PBItems.WAND_PROJECTILE.get().getDefaultInstance());
            wandProjectile.shootFromRotation(pLivingEntity, pLivingEntity.getXRot(), pLivingEntity.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(wandProjectile);
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity p_344979_) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static void applyBonemeal(Level level, BlockPos hitPos, Direction face) {
        BlockPos blockpos1 = hitPos.relative(face);
        if (applyBonemeal(level, hitPos)) {
            if (!level.isClientSide) {
                level.levelEvent(1505, hitPos, 15);
            }
        } else {
            BlockState blockstate = level.getBlockState(hitPos);
            boolean flag = blockstate.isFaceSturdy(level, hitPos, face);
            if (flag && growWaterPlant(level, blockpos1, face)) {
                if (!level.isClientSide) {
                    level.levelEvent(1505, blockpos1, 15);
                }
            }
        }
    }

    private static boolean applyBonemeal(Level p_40629_, BlockPos p_40630_) {
        BlockState blockstate = p_40629_.getBlockState(p_40630_);
        Block var7 = blockstate.getBlock();
        if (var7 instanceof BonemealableBlock) {
            BonemealableBlock bonemealableblock = (BonemealableBlock) var7;
            if (bonemealableblock.isValidBonemealTarget(p_40629_, p_40630_, blockstate)) {
                if (p_40629_ instanceof ServerLevel) {
                    if (bonemealableblock.isBonemealSuccess(p_40629_, p_40629_.random, p_40630_, blockstate)) {
                        bonemealableblock.performBonemeal((ServerLevel) p_40629_, p_40629_.random, p_40630_, blockstate);
                    }
                }

                return true;
            }

        }
        return false;
    }

    public static boolean growWaterPlant(Level p_40633_, BlockPos p_40634_, @javax.annotation.Nullable Direction p_40635_) {
        if (p_40633_.getBlockState(p_40634_).is(Blocks.WATER) && p_40633_.getFluidState(p_40634_).getAmount() == 8) {
            if (p_40633_ instanceof ServerLevel) {
                RandomSource randomsource = p_40633_.getRandom();

                label78:
                for (int i = 0; i < 128; ++i) {
                    BlockPos blockpos = p_40634_;
                    BlockState blockstate = Blocks.SEAGRASS.defaultBlockState();

                    for (int j = 0; j < i / 16; ++j) {
                        blockpos = blockpos.offset(randomsource.nextInt(3) - 1, (randomsource.nextInt(3) - 1) * randomsource.nextInt(3) / 2, randomsource.nextInt(3) - 1);
                        if (p_40633_.getBlockState(blockpos).isCollisionShapeFullBlock(p_40633_, blockpos)) {
                            continue label78;
                        }
                    }

                    Holder<Biome> holder = p_40633_.getBiome(blockpos);
                    if (holder.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                        if (i == 0 && p_40635_ != null && p_40635_.getAxis().isHorizontal()) {
                            blockstate = BuiltInRegistries.BLOCK.getTag(BlockTags.WALL_CORALS)
                                    .flatMap((p_204098_) -> p_204098_.getRandomElement(p_40633_.random))
                                    .map((p_204100_) -> p_204100_.value().defaultBlockState())
                                    .orElse(blockstate);

                            if (blockstate.hasProperty(BaseCoralWallFanBlock.FACING)) {
                                blockstate = blockstate.setValue(BaseCoralWallFanBlock.FACING, p_40635_);
                            }
                        } else if (randomsource.nextInt(4) == 0) {
                            blockstate = BuiltInRegistries.BLOCK.getTag(BlockTags.UNDERWATER_BONEMEALS)
                                    .flatMap((p_204091_) -> p_204091_.getRandomElement(p_40633_.random))
                                    .map((p_204095_) -> p_204095_.value().defaultBlockState())
                                    .orElse(blockstate);
                        }
                    }

                    if (blockstate.is(BlockTags.WALL_CORALS, (p_204093_) -> p_204093_.hasProperty(BaseCoralWallFanBlock.FACING))) {
                        for (int k = 0; !blockstate.canSurvive(p_40633_, blockpos) && k < 4; ++k) {
                            blockstate = blockstate.setValue(BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(randomsource));
                        }
                    }

                    if (blockstate.canSurvive(p_40633_, blockpos)) {
                        BlockState blockstate1 = p_40633_.getBlockState(blockpos);
                        if (blockstate1.is(Blocks.WATER) && p_40633_.getFluidState(blockpos).getAmount() == 8) {
                            p_40633_.setBlock(blockpos, blockstate, 3);
                        } else if (blockstate1.is(Blocks.SEAGRASS) && randomsource.nextInt(10) == 0) {
                            ((BonemealableBlock) Blocks.SEAGRASS).performBonemeal((ServerLevel) p_40633_, randomsource, blockpos, blockstate1);
                        }
                    }
                }

            }
            return true;
        } else {
            return false;
        }
    }
}
