package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.items.renderer.JarItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class JarItem extends BlockItem {
    public JarItem(Properties pProperties) {
        super(PBBlocks.JAR.get(), pProperties);
    }
}
