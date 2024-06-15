package com.pigdad.paganbless.registries.blocks;

import com.mojang.serialization.MapCodec;
import com.pigdad.paganbless.PBConfig;
import com.pigdad.paganbless.registries.PBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LavenderIncenseBlock extends IncenseBlock{
    public LavenderIncenseBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void effectTick(Level level, BlockPos blockPos, BlockState blockState) {
        int range = getRange(level, blockPos, blockState);
        for (LivingEntity livingEntity : getNearbyEntities(level, blockPos, range)) {
            if (!livingEntity.hasEffect(MobEffects.REGENERATION)) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 1, false, false));
            }
        }
    }

    private static @NotNull List<LivingEntity> getNearbyEntities(Level level, BlockPos blockPos, int range) {
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos).deflate(range));
    }

    @Override
    public int getRange(Level level, BlockPos blockPos, BlockState blockState) {
        return PBConfig.lavenderIncenseRange;
    }

    @Override
    public Item getIncenseItem() {
        return PBItems.LAVENDER.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(LavenderIncenseBlock::new);
    }
}
