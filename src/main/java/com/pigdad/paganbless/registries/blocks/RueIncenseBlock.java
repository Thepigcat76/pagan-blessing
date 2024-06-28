package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.api.blocks.IncenseBlock;
import com.pigdad.paganbless.registries.PBItems;
import com.pigdad.paganbless.registries.blockentities.IncenseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class RueIncenseBlock extends IncenseBlock {
    public RueIncenseBlock(Properties pProperties) {
        super(pProperties.randomTicks());
    }

    @Override
    public void effectTick(Level level, BlockPos blockPos, BlockState blockState) {
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        if (((IncenseBlockEntity) pLevel.getBlockEntity(pPos)).isBurning()) {
            growCropsNearby(pLevel, pPos, pState);
        }
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        if (((IncenseBlockEntity) pLevel.getBlockEntity(pPos)).isBurning()) {
            int range = getRange(pLevel, pPos, pState);

            BlockPos.betweenClosed(pPos.offset(-range, -1, -range), pPos.offset(range, range, range)).forEach(cropPos -> {
                if (!pLevel.hasChunkAt(cropPos)) {
                    return;
                }
                BlockState cropState = pLevel.getBlockState(cropPos);
                Block cropBlock = cropState.getBlock();

                if (isAllowedCropBlock(cropBlock) && cropBlock instanceof BonemealableBlock) {
                    if (pRandom.nextInt(0, 12) == 10) {
                        ParticleUtils.spawnParticles(pLevel, cropPos.above(), 1, 0.4, 0, true, ParticleTypes.HAPPY_VILLAGER);
                    }
                }
            });
        }
    }

    @Override
    public Item getIncenseItem() {
        return PBItems.CHOPPED_RUE.get();
    }

    @Override
    public int getRange(Level level, BlockPos blockPos, BlockState blockState) {
        return PBConfig.rueIncenseRange;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(RueIncenseBlock::new);
    }

    @SuppressWarnings("deprecation")
    private void growCropsNearby(ServerLevel level, BlockPos pos, BlockState state) {
        int range = getRange(level, pos, state);
        BlockPos.betweenClosed(pos.offset(-range, -1, -range), pos.offset(range, range, range)).forEach(cropPos -> {
            if (!level.hasChunkAt(cropPos)) {
                return;
            }
            BlockState cropState = level.getBlockState(cropPos);
            Block cropBlock = cropState.getBlock();

            if (isAllowedCropBlock(cropBlock) && cropBlock instanceof BonemealableBlock) {
                double distance = Math.sqrt(cropPos.distSqr(pos));
                tickCropBlock(level, cropPos, cropState, cropBlock, distance, range);
            }
        });
        level.scheduleTick(pos, state.getBlock(), (int) (secondsBetweenGrowthTicks() * 20));
    }

    private void tickCropBlock(ServerLevel world, BlockPos cropPos, BlockState cropState, Block cropBlock, double distance, int range) {
        distance -= fullPotencyRange();
        distance = Math.max(1D, distance);
        double distanceCoefficient = 1D - (distance / range);

        //it schedules the next tick.
        world.scheduleTick(cropPos, cropBlock, (int) (distanceCoefficient * secondsBetweenGrowthTicks() * 20F));
        cropState.randomTick(world, cropPos, world.random);
        world.levelEvent(2005, cropPos, Math.max((int) (range - distance), 1));
    }

    private double fullPotencyRange() {
        return 1;
    }

    private double secondsBetweenGrowthTicks() {
        return 1;
    }

    private boolean isAllowedCropBlock(Block cropBlock) {
        return cropBlock != Blocks.GRASS_BLOCK && !(cropBlock instanceof DoublePlantBlock);
    }
}
