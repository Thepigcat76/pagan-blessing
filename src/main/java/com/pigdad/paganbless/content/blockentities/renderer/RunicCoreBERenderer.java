package com.pigdad.paganbless.content.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pigdad.paganbless.content.blockentities.RunicCoreBlockEntity;
import com.pigdad.paganbless.utils.rendering.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RunicCoreBERenderer implements BlockEntityRenderer<RunicCoreBlockEntity> {
    public RunicCoreBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(RunicCoreBlockEntity runicCoreBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        RenderUtils.renderFloatingItem(runicCoreBlockEntity.getItemHandler().getStackInSlot(0), poseStack, multiBufferSource, combinedLight, combinedOverlay, 1F);
    }


}
