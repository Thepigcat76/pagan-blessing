package com.pigdad.paganbless.registries.items;

import com.pigdad.paganbless.PaganBless;
import com.pigdad.paganbless.registries.PBBlocks;
import com.pigdad.paganbless.registries.items.renderer.JarItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class JarItem extends BlockItem {
    public static final JarItemRenderer RENDERER = new JarItemRenderer();

    public JarItem(Properties pProperties) {
        super(PBBlocks.JAR.get(), pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return RENDERER;
            }
        });
    }
}
