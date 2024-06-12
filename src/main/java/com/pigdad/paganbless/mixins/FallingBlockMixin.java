package com.pigdad.paganbless.mixins;

import com.pigdad.paganbless.registries.blocks.RopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo info) {
        // Prevents anvil from falling down when rope is above anvil
        BlockState blockState = pLevel.getBlockState(pPos.above());
        if (blockState.getBlock() instanceof RopeBlock && blockState.getValue(RopeBlock.HAS_WINCH)) {
            info.cancel();
        }
    }
}
