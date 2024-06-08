package com.pigdad.paganbless.registries.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pigdad.paganbless.registries.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RunicCoreBERenderer implements BlockEntityRenderer<RunicCoreBlockEntity> {
    public RunicCoreBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(RunicCoreBlockEntity runicCoreBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        RenderUtils.renderFloatingItem(runicCoreBlockEntity.getItemHandler().get().getStackInSlot(0), poseStack, multiBufferSource, combinedLight, combinedOverlay);
    }


}
