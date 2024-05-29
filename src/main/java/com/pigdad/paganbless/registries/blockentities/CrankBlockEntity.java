package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class CrankBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final RawAnimation CRANK_ANIMS = RawAnimation.begin().thenPlay("crank.lift");
    private static final RawAnimation CRANK_DROP = RawAnimation.begin().thenLoop("crank.drop");
    public boolean lifting = false;

    public CrankBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.CRANK.get(), p_155229_, p_155230_);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 20, state -> PlayState.CONTINUE)
                .triggerableAnim("lift_crank", CRANK_DROP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
