package com.pigdad.paganbless.content.items;

import com.pigdad.paganbless.registries.PBItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ChoppedHerbItem extends Item {
    private final Block incenseBlock;

    public ChoppedHerbItem(Properties pProperties) {
        super(pProperties);
        this.incenseBlock = null;
    }

    public ChoppedHerbItem(Properties pProperties, Block incenseBlock) {
        super(pProperties);
        this.incenseBlock = incenseBlock;
        if (incenseBlock != null) {
            PBItems.INCENSES.put(this, incenseBlock);
        }
    }

    public Block getIncenseBlock() {
        return incenseBlock;
    }
}
