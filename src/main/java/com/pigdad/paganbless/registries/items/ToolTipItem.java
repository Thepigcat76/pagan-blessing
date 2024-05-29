package com.pigdad.paganbless.registries.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToolTipItem extends Item {
    private final Component component;
    public ToolTipItem(Properties properties, Component component) {
        super(properties);
        this.component = component;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(component);
    }
}
