package com.pigdad.paganbless.registries.blockentities;

import com.mojang.datafixers.util.Pair;
import com.pigdad.paganbless.api.blocks.ContainerBlockEntity;
import com.pigdad.paganbless.api.io.IOActions;
import com.pigdad.paganbless.registries.PBBlockEntities;
import com.pigdad.paganbless.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getItemIO() {
        return Utils.getBottomExtractOtherInsertSingleSlot();
    }

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getFluidIO() {
        return Map.of();
    }
}
