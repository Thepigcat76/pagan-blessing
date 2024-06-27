package com.pigdad.paganbless.api.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class PBAbstractContainerScreen<T extends PBAbstractContainerMenu<?>> extends AbstractContainerScreen<T> {
    public PBAbstractContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(getBackgroundTexture(), x, y, 0, 0, imageWidth, imageHeight);
    }

    public abstract @NotNull ResourceLocation getBackgroundTexture();
}
