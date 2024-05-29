package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.registries.blockentities.CrankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrankBlock extends BaseEntityBlock {
    public CrankBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CrankBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrankBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, BlockHitResult p_60508_) {
        CrankBlockEntity blockEntity = (CrankBlockEntity) p_60504_.getBlockEntity(p_60505_);
        blockEntity.triggerAnim("controller", "lift_crank");
        return InteractionResult.SUCCESS;
    }
}
