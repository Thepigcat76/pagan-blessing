package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.api.blocks.TickingBlock;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.registries.blockentities.HangingHerbBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HangingHerbBlock extends BaseHangingHerbBlock implements TickingBlock<HangingHerbBlockEntity> {
    private final Block driedHerbBlock;
    private final Block waxedHerbBlock;

    public HangingHerbBlock(Properties properties, Block driedHerbBlock, Block waxedHerbBlock) {
        super(properties);
        this.driedHerbBlock = driedHerbBlock;
        this.waxedHerbBlock = waxedHerbBlock;
    }

    public Block getDriedHerbBlock() {
        return driedHerbBlock;
    }

    public Block getWaxedHerbBlock() {
        return waxedHerbBlock;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.HONEYCOMB)) {
            if (level.isClientSide()) {
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
            }
            level.playLocalSound(pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            level.setBlockAndUpdate(pos, getWaxedHerbBlock().defaultBlockState());
            if (!player.hasInfiniteMaterials()) {
                stack.shrink(1);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HangingHerbBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return simpleCodec(props -> new HangingHerbBlock(props, driedHerbBlock, waxedHerbBlock));
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
