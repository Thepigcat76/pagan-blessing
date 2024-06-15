package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.registries.PBItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class HerbItem extends Item {
    public HerbItem(Properties pProperties) {
        super(pProperties);
    }

    public HerbItem(Properties pProperties, Block incenseBlock) {
        super(pProperties);
        if (incenseBlock != null) {
            PBItems.INCENSES.put(this, incenseBlock);
        }
    }
}
