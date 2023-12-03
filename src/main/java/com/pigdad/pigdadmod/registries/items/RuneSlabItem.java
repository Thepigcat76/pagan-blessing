package com.pigdad.pigdadmod.registries.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class RuneSlabItem extends BlockItem {
    public RuneSlabItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        if (!context.getLevel().getBlockState(context.getClickedPos().offset(0, 1, 0)).is(Blocks.AIR)) {
            return null;
        }
        return context;
    }
}
