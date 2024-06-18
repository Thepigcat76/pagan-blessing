package com.pigdad.paganbless.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public final class SoundUtils {
    public static void playSound(Level level, BlockPos blockPos, SoundEvent soundEvent, SoundSource soundSource) {
        level.playSound(null, blockPos, soundEvent, soundSource);
    }
}
