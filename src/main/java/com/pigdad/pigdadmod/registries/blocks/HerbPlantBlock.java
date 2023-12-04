package com.pigdad.pigdadmod.registries.blocks;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.FlowerBlock;

public class HerbPlantBlock extends FlowerBlock {
    public HerbPlantBlock(int size, Properties p_53514_) {
        super(() -> MobEffects.GLOWING, size, p_53514_);
    }
}
