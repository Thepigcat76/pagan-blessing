package com.pigdad.paganbless.registries.blockentities;

import com.pigdad.paganbless.registries.PBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class JarBlockEntity extends ContainerBlockEntity {
    public long wobbleStartedAtTick;
    @Nullable
    public DecoratedPotBlockEntity.WobbleStyle lastWobbleStyle;

    public JarBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(PBBlockEntities.JAR.get(), p_155229_, p_155230_);
        addItemHandler(1);
    }

    public void wobble(DecoratedPotBlockEntity.WobbleStyle style) {
        if (this.level != null) {
            this.wobbleStartedAtTick = this.level.getGameTime();
            this.lastWobbleStyle = style;
        }
    }
}
