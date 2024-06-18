package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.api.blocks.TickingBlock;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.HangingHerbBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HangingHerbBlock extends BaseHangingHerbBlock implements TickingBlock<HangingHerbBlockEntity> {
    private final Block driedHerbBlock;

    public HangingHerbBlock(Properties properties, Block driedHerbBlock) {
        super(properties);
        this.driedHerbBlock = driedHerbBlock;
    }

    public Block getDriedHerbBlock() {
        return driedHerbBlock;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HangingHerbBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return simpleCodec(props -> new HangingHerbBlock(props, driedHerbBlock));
    }

    @Override
    public void clientTick(Level level, BlockPos blockPos, BlockState blockState, HangingHerbBlockEntity blockEntity) {
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        spawnParticles(pLevel, pPos, pRandom);
    }

    private static void spawnParticles(Level level, BlockPos blockPos, RandomSource randomSource) {
        int i = blockPos.getX();
        int j = blockPos.getY();
        int k = blockPos.getZ();
        double d0 = (double) i + randomSource.nextDouble();
        double d1 = (double) j + 0.7 - randomSource.nextDouble();
        double d2 = (double) k + randomSource.nextDouble();
        if (randomSource.nextInt(0, 5) == 3) {
            level.addParticle(new DustParticleOptions(Vec3.fromRGB24(FastColor.ARGB32.color(255, 0x648262)).toVector3f(), 1.0f), d0, d1, d2, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState, HangingHerbBlockEntity blockEntity) {
        if (blockEntity.getDryingProgress() >= PBConfig.dryingTime) {
            level.setBlockAndUpdate(blockPos, driedHerbBlock.defaultBlockState());
        } else {
            blockEntity.setDryingProgress(blockEntity.getDryingProgress() + 1);
        }
    }

    @Override
    public BlockEntityType<HangingHerbBlockEntity> getBlockEntityType() {
        return PBBlockEntities.HANGING_HERB.get();
    }
}
