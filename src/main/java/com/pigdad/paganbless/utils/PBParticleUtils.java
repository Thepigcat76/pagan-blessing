package com.pigdad.paganbless.utils;

import com.pigdad.paganbless.registries.PBBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public class PBParticleUtils {
    public static void spawnBreakParticle(BlockPos pos, Block block, int count) {
        for (int i = 0; i < count; i++) {
            Minecraft.getInstance().particleEngine.add(new TerrainParticle(Minecraft.getInstance().level, pos.getX() + 0.5f, pos.above().getY(), pos.getZ() + 0.5f,
                    0 + ((double) i / 10), 0 + ((double) i / 10), 0 + ((double) i / 10), block.defaultBlockState()));
        }
    }

    public static void spawnBreakParticleForRuneSlab(BlockPos blockPos, int rand, int i) {
        Minecraft.getInstance().particleEngine.add(new TerrainParticle(Minecraft.getInstance().level, blockPos.getX() + 0.5f, blockPos.getY(), blockPos.getZ() + 0.5f,
                ((double) i / 10) * rand, ((double) i / 10), ((double) i / 10) * rand, PBBlocks.RUNE_SLAB_INERT.get().defaultBlockState()));
    }
}
