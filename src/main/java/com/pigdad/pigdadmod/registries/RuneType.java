package com.pigdad.pigdadmod.registries;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public interface RuneType {
    RuneType getTypeByBlock(Block block);

    static void test() {

    }
}
